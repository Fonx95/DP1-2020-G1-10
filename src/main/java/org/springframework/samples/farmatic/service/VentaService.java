
package org.springframework.samples.farmatic.service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.farmatic.model.Cliente;
import org.springframework.samples.farmatic.model.Comprador;
import org.springframework.samples.farmatic.model.LineaPedido;
import org.springframework.samples.farmatic.model.LineaVenta;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.model.TipoProducto;
import org.springframework.samples.farmatic.model.TipoTasa;
import org.springframework.samples.farmatic.model.Venta;
import org.springframework.samples.farmatic.model.Venta.EstadoVenta;
import org.springframework.samples.farmatic.repository.ClienteRepository;
import org.springframework.samples.farmatic.repository.CompradorRepository;
import org.springframework.samples.farmatic.repository.LineaVentaRepository;
import org.springframework.samples.farmatic.repository.VentaRepository;
import org.springframework.samples.farmatic.service.exception.CompradorEmptyException;
import org.springframework.samples.farmatic.service.exception.LineaEmptyException;
import org.springframework.samples.farmatic.service.exception.VentaClienteEmptyException;
import org.springframework.samples.farmatic.service.exception.VentaCompradorEmptyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VentaService {

	private VentaRepository			ventaRepository;

	private LineaVentaRepository	lineaRepository;

	private CompradorRepository		compradorRepository;

	private ClienteRepository		clienteRepository;

	private PedidoService			pedidoService;

	private ProductoService			productoService;


	@Autowired
	public VentaService(final VentaRepository ventaRepository, final LineaVentaRepository lineaRepository, final CompradorRepository compradorRepository, final ClienteRepository clienteRepository, final PedidoService pedidoService,
		final ProductoService productoService) {
		this.ventaRepository = ventaRepository;
		this.lineaRepository = lineaRepository;
		this.compradorRepository = compradorRepository;
		this.clienteRepository = clienteRepository;
		this.pedidoService = pedidoService;
		this.productoService = productoService;
	}

	//---------METODOS AUXILIARES PRIVADOS---------
	
	private void validateVenta(Venta venta) throws VentaCompradorEmptyException, VentaClienteEmptyException, LineaEmptyException {
		Collection<LineaVenta> lineas = venta.getLineaVenta();
		for(LineaVenta linea : lineas) {
			if(linea.getProducto().getProductType().equals(TipoProducto.ESTUPEFACIENTE) && venta.getComprador() == null) {
				throw new VentaCompradorEmptyException();
			}
		}
		
		if(venta.getImporteTotal() - venta.getPagado() > 0 && venta.getCliente() == null) {
			throw new VentaClienteEmptyException();
		}
		
		if(venta.getLineaVenta().isEmpty()) {
			throw new LineaEmptyException("Venta");
		}
	}

	private Double numberFormatter(final Double number) {
		//metodo auxiliar que establece que los decimales no supere las 2 cifras
		DecimalFormat df = new DecimalFormat("#.00");
		String aux = df.format(number).replace(",", ".");
		return Double.parseDouble(aux);
	}

	private boolean existeEstupefaciente(final Venta venta) {
		//metodo auxiliar que devuelve true si existe un producto estupefaciente en las lineas de la venta o false en caso contrario
		Collection<LineaVenta> lineas = venta.getLineaVenta();
		for (LineaVenta linea : lineas) {
			if (linea.getProducto().getProductType() == TipoProducto.ESTUPEFACIENTE) {
				return true;
			}
		}
		return false;
	}

	private Double getTasaTypes(final TipoTasa TA) throws DataAccessException {
		//metodo auxiliar que establece que descuento se aplica segun la seguridad social del cliente
		switch (TA) {
		case TSI001:
			return 1.0;
		case TSI002:
			return 0.86;
		case TSI003:
			return 0.55;
		case TSI004:
			return 0.13;
		case TSI005:
			return 0.0;
		default:
			return 1.0;
		}
	}

	@Transactional
	private void controlStock(final Venta venta) throws DataAccessException {
		//esta funcion tiene la finalidad de actualizar el stock del producto
		Collection<LineaVenta> lineas = venta.getLineaVenta();
		for (LineaVenta linea : lineas) {														//Por cada linea que tenga la venta realiza:
			Producto producto = linea.getProducto();										//
			producto.setStock(producto.getStock() - linea.getCantidad());					// - resta al stock la cantidad vendida
			VentaService.log.debug("El nuevo stock del producto '" + producto.getCode() 					//
				+ "' es de " + producto.getStock() + " y su stock minimo es de " 			//
				+ producto.getMinStock());													//
			if (producto.getStock() < producto.getMinStock()) {								// - si al hacer la operacion anterior el stock baja por debajo del minimo establecido se hace lo siguiente:
				Integer idLinea = this.pedidoService.existelinea(producto);					//		+ Se obtiene el id de la linea de ese producto
				Integer diferencia = producto.getMinStock() - producto.getStock();			//
				if (idLinea != null) {															//		+ si la linea de pedido existe
					LineaPedido lPedido = this.pedidoService.lineaById(idLinea);			//
					if (lPedido.getCantidad() < diferencia) {								//		+ y la cantidad que ya tiene es menor que la cantida necesaria para tener el minimo de stock
						lPedido.setCantidad(diferencia);									//		+ establece la cantidad de la linea de pedido con la diferencia entre el stock y el minimo de stock establecido
						this.pedidoService.saveLinea(lPedido);								//
						VentaService.log.debug("Se ha actualizado una linea del pedido con " 			//
							+ lPedido.getCantidad() + " unidades");						//
					}																		//
				} else {																		//		+ si la linea de pedido no existe
					LineaPedido lPedido = this.pedidoService.newLinea(producto, diferencia);//		+ crea una nueva
					this.pedidoService.saveLinea(lPedido);
					VentaService.log.debug("Se ha creado una nueva linea de pedido con " + lPedido.getCantidad() + " unidades");
				}
			}
			this.productoService.saveProducto(producto);
		}
		VentaService.log.debug("Se ha actualizado el stock de " + lineas.size() + " productos");
	}

	//---------Metodos referente a VENTAS---------

	@Transactional
	public Venta ventaActual() throws DataAccessException {
		//busqueda de la venta actual
		return this.ventaRepository.ventaActual();
	}

	@Transactional
	public Collection<Venta> findAllVentas() throws DataAccessException {
		//busqueda de todas las ventas
		return this.ventaRepository.findAll();
	}

	@Transactional
	public Venta venta(final int id) throws DataAccessException {
		//busqueda de una venta por su id
		return this.ventaRepository.venta(id);
	}

	@Transactional(rollbackFor = {VentaCompradorEmptyException.class, VentaClienteEmptyException.class, LineaEmptyException.class})
	public void finalizarVenta(Venta venta) throws DataAccessException, VentaCompradorEmptyException, VentaClienteEmptyException, LineaEmptyException {
		//establece una venta en realizada
		venta = this.venta(venta.getId());
																			//actualiza la informacion de la venta:
		venta.setFecha(LocalDate.now());									// -la fecha de la venta

		Double diferencia = venta.getImporteTotal() - venta.getPagado();	// -calcula que se debe pagar en la venta
		venta.setPorPagar(this.numberFormatter(diferencia));
		
		venta.setEstadoVenta(EstadoVenta.Realizada);						// -establece el estado en realizada
		
		validateVenta(venta);
		
		this.ventaRepository.save(venta);
		VentaService.log.debug("La venta se ha a realizado con la fecha " + venta.getFecha() + ", un importe total de " + venta.getImporteTotal() + "€, un pago de " + venta.getPagado() + "€ y " + venta.getLineaVenta().size() + " lineas de venta");
		this.controlStock(venta);//actualiza el stock de los productos
		Venta nuevaVenta = new Venta();//crea una nueva venta borrador
		this.ventaRepository.save(nuevaVenta);
		VentaService.log.debug("Se ha creado una nueva venta actual");
	}

	@Transactional
	public void updateVenta(final Venta venta) throws DataAccessException {
		//actualiza toda la informacion de la venta en funcion de las lineas de venta que tenga
		Collection<LineaVenta> lineas = venta.getLineaVenta();

		Double importeTotal = 0.;
		for (LineaVenta linea : lineas) {
			importeTotal += linea.getImporte();//calcula el importe total a partir del importe de cada linea de venta
		}
		importeTotal = this.numberFormatter(importeTotal);
		venta.setImporteTotal(importeTotal);

		Double diferencia = venta.getImporteTotal() - venta.getPagado();//calcula lo que se debe pagar en la venta
		venta.setPorPagar(this.numberFormatter(diferencia));

		this.ventaRepository.save(venta);
		VentaService.log.debug("Se ha actualizado la venta actual con un importe total de " + venta.getImporteTotal() + "€ y una deuda de " + venta.getPorPagar() + "€");

		if (!this.existeEstupefaciente(venta) && venta.getComprador() != null) { 	//si la venta tiene asignada un comprador estupefaciente pero 
			this.compradorRepository.delete(venta.getComprador());			//no tiene un producto estupefaciente en sus lineas lo elimina
			VentaService.log.debug("Se ha eliminado el registro del comprador estupefaciente con dni '" + venta.getComprador().getDni() + "'");
		}
	}

	//---------Metodos referente a LINEAS DE VENTA---------
	
	@Transactional 
	public LineaVenta lineaById(int id) throws DataAccessException {
		//busqueda de una linea pedido por su id
		return this.lineaRepository.findById(id).get();
	}

	@Transactional
	public Integer existelinea(final Producto producto) {
		//comprueba si existe una linea en la venta actual con el producto dado
		Collection<LineaVenta> lineas = this.ventaActual().getLineaVenta();
		for (LineaVenta linea : lineas) {
			if (linea.getProducto().equals(producto)) {
				return linea.getId();//devuelve el id de la linea o null si no existe
			}
		}
		return null;
	}

	@Transactional
	public void saveLinea(final LineaVenta linea) throws DataAccessException {
		//crea o modifica una lina de venta

		Double pvp = linea.getProducto().getPvp();

		Double descuentoTasa = this.getTasaTypes(linea.getTipoTasa());		//calcula el importe aplicando el descuento por la tasa de aportacion 
		Double importeLinea = pvp * descuentoTasa * linea.getCantidad();//y multiplicando por la cantidad
		linea.setImporte(this.numberFormatter(importeLinea));

		this.lineaRepository.save(linea);
		VentaService.log.debug("Se ha creado o modificado la linea de venta del producto '" + linea.getProducto().getCode() + "' y un importe de " + linea.getImporte() + "€");

		Venta venta = this.ventaActual();
		this.updateVenta(venta);//actualiza la infromacion de la venta
	}

	@Transactional
	public void deleteLinea(final LineaVenta linea) throws DataAccessException {
		//elimina linea de venta
		this.lineaRepository.delete(linea);
		VentaService.log.debug("Se ha eliminado la linea de venta del producto '" + linea.getProducto().getCode() + "'");

		Venta venta = this.ventaActual();
		venta.getLineaVenta().remove(linea);
		this.updateVenta(venta);//actualiza la informacion de la venta
	}

	@Transactional
	public LineaVenta newLinea(final Producto producto) throws DataAccessException {
		//crea una linea de venta java
		Venta venta = this.ventaActual();
		LineaVenta linea = new LineaVenta(producto, venta);
		return linea;
	}

	//---------Metodos referente a CLIENTES---------

	@Transactional(rollbackFor = {VentaCompradorEmptyException.class, VentaClienteEmptyException.class, LineaEmptyException.class})
	public void asignarCliente(final int id) throws DataAccessException, VentaCompradorEmptyException, VentaClienteEmptyException, LineaEmptyException {
		//asigna un cliente a la venta actual
		Venta venta = this.ventaActual();
		Cliente cliente = this.clienteRepository.findById(id);
		cliente.setPorPagarTotal(cliente.getPorPagarTotal() + venta.getPorPagar());//actualiza el total que debe el cliente
		venta.setCliente(cliente);
		this.finalizarVenta(venta);//finaliza la venta
		this.clienteRepository.save(cliente);
		VentaService.log.debug("Se ha asignado la venta al cliente con dni '" + cliente.getDni() + "' con una deuda total de " + cliente.getPorPagarTotal() + "€ en un total de " + cliente.getVenta().size() + " ventas");
	}

	//---------Metodos referente a COMPRADOR---------

	@Transactional(rollbackFor = CompradorEmptyException.class)
	public void saveComprador(final Comprador comprador) throws DataAccessException, CompradorEmptyException  {
		//crea un comprador de estupefaciente
		if(comprador.getDni() == "") {
			throw new CompradorEmptyException();
		}
		this.compradorRepository.save(comprador);
		VentaService.log.debug("se ha registrado a " + comprador.getName() + " " + comprador.getApellidos() + " (" + comprador.getDni() + ") en el libro de estupefaciente");
	}
}

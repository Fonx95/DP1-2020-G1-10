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
import org.springframework.samples.farmatic.repository.CompradorRepository;
import org.springframework.samples.farmatic.repository.LineaVentaRepository;
import org.springframework.samples.farmatic.repository.VentaRepository;
import org.springframework.samples.farmatic.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VentaService {
	
	private VentaRepository ventaRepository;
	
	private LineaVentaRepository lineaRepository;
	
	private CompradorRepository compradorRepository;
	
	private ClienteRepository clienteRepository;
	
	private PedidoService pedidoService;
	
	private ProductoService productoService;
	
	@Autowired
	public VentaService(VentaRepository ventaRepository, LineaVentaRepository lineaRepository, 
			CompradorRepository compradorRepository, ClienteRepository clienteRepository,
			PedidoService pedidoService, ProductoService productoService) {
		this.ventaRepository = ventaRepository;
		this.lineaRepository = lineaRepository;
		this.compradorRepository = compradorRepository;
		this.clienteRepository = clienteRepository;
		this.pedidoService = pedidoService;
		this.productoService = productoService;
	}
	
	@Transactional
	public Venta ventaActual() throws DataAccessException {
		//venta actual
		return this.ventaRepository.ventaActual();
	}
	
	@Transactional
	public Collection<Venta> findAllVentas() throws DataAccessException{
		return this.ventaRepository.findAll();
	}
	
	@Transactional
	public Venta venta(final int id) throws DataAccessException {
		return this.ventaRepository.venta(id);
	}
	
	@Transactional
	public void finalizarVenta(Venta venta) throws DataAccessException {
		venta = this.venta(venta.getId());
		venta.setFecha(LocalDate.now());
		
		Double diferencia = venta.getImporteTotal() - venta.getPagado();
		venta.setPorPagar(numberFormatter(diferencia));
		
		venta.setEstadoVenta(EstadoVenta.Realizada);
		this.ventaRepository.save(venta);
		this.controlStock(venta);
		Venta nuevaVenta = new Venta();
		this.ventaRepository.save(nuevaVenta);
	}
	
	@Transactional
	private void controlStock(Venta venta) throws DataAccessException {
		Collection<LineaVenta> lineas = venta.getLineaVenta();
		for(LineaVenta linea:lineas) {
			Producto producto = linea.getProducto();
			producto.setStock(producto.getStock() - linea.getCantidad());
			if(producto.getStock() < producto.getMinStock()) {
				Integer idLinea = this.pedidoService.existelinea(producto);
				Integer diferencia = producto.getMinStock() - producto.getStock();
				if(idLinea!=null) {
					LineaPedido lPedido = this.pedidoService.lineaById(idLinea);
					if(lPedido.getCantidad() < diferencia) {
						lPedido.setCantidad(diferencia);
						this.pedidoService.saveLinea(lPedido);
					}
				}else {
					LineaPedido lPedido = this.pedidoService.newLinea(producto, diferencia);
					this.pedidoService.saveLinea(lPedido);
				}
			}
			this.productoService.saveProducto(producto);
		}
	}
	
	@Transactional
	public void updateVenta(Venta venta) throws DataAccessException{
		
		Collection<LineaVenta> lineas = venta.getLineaVenta();
		Double importeTotal = 0.;
		for(LineaVenta linea:lineas) importeTotal += linea.getImporte();
		
		importeTotal = numberFormatter(importeTotal);
		venta.setImporteTotal(importeTotal);
		Double diferencia = venta.getImporteTotal() - venta.getPagado();
		venta.setPorPagar(numberFormatter(diferencia));

		this.ventaRepository.save(venta);
		
		if(!existeEstupefaciente(venta) && venta.getComprador() != null) {
			this.compradorRepository.delete(venta.getComprador());
		}
	}
	
	@Transactional
	public Integer existelinea(Producto producto) {
		Collection<LineaVenta> lineas = this.ventaActual().getLineaVenta();
		for(LineaVenta linea:lineas) {
			if(linea.getProducto().equals(producto)) return linea.getId();
		}
		return null;
	}
	
	@Transactional
	public void saveLinea(LineaVenta linea) throws DataAccessException{
		//guardando linea de venta
		Double pvp = linea.getProducto().getPvp();
		
		Double descuentoTasa = getTasaTypes(linea.getTipoTasa());
		Double importeLinea = pvp * descuentoTasa * linea.getCantidad();
		linea.setImporte(numberFormatter(importeLinea));
		
		this.lineaRepository.save(linea);
		
		Venta venta = this.ventaActual();
		this.updateVenta(venta);
	}
	
	private Double numberFormatter(Double number) {
		DecimalFormat df = new DecimalFormat("#.00");
		String aux = df.format(number).replace(",", ".");
		return Double.parseDouble(aux);
	}
	
	private boolean existeEstupefaciente(Venta venta) {
		Collection<LineaVenta> lineas = venta.getLineaVenta();
		for(LineaVenta linea:lineas) {
			if(linea.getProducto().getProductType() == TipoProducto.ESTUPEFACIENTE) return true;
		}
		return false;
	}
	
	@Transactional
	public void deleteLinea(LineaVenta linea) throws DataAccessException{
		//elimina linea de pedido
		lineaRepository.delete(linea);
		
		Venta venta = this.ventaActual();
		venta.getLineaVenta().remove(linea);
		this.updateVenta(venta);
	}
	
	@Transactional
	public LineaVenta newLinea(Producto producto) throws DataAccessException{
		//creando linea de venta vacia
		Venta venta = this.ventaActual();
		LineaVenta linea = new LineaVenta(producto, venta);
		return linea;
	}
	
	@Transactional
	public Cliente cliente(final int id) {
		return this.clienteRepository.findById(id);
	}
	
	@Transactional
	public Cliente clienteDni(String dni) throws DataAccessException {
		return this.clienteRepository.fingByDni(dni);
	}
	
	@Transactional
	public void asignarCliente(Cliente cliente) {
		Venta venta = this.ventaActual();
		cliente = this.cliente(cliente.getId());
		cliente.setPorPagarTotal(cliente.getPorPagarTotal() + venta.getPorPagar());
		venta.setCliente(cliente);
		this.finalizarVenta(venta);
		this.clienteRepository.save(cliente);
	}
	
	@Transactional(readOnly = true)
	public Collection<Cliente> findClientes() {
		return (Collection<Cliente>) this.clienteRepository.findAll();
	}
	
	private Double getTasaTypes(TipoTasa TA) throws DataAccessException {
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
	public void saveComprador(Comprador comprador) {
		//Registrando comprador de estupefaciente
		this.compradorRepository.save(comprador);
	}
}

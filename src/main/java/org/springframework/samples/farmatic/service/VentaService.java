package org.springframework.samples.farmatic.service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.farmatic.model.Cliente;
import org.springframework.samples.farmatic.model.Comprador;
import org.springframework.samples.farmatic.model.LineaVenta;
import org.springframework.samples.farmatic.model.Producto;
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
	
	@Autowired
	public VentaService(VentaRepository ventaRepository, LineaVentaRepository lineaRepository, 
			CompradorRepository compradorRepository, ClienteRepository clienteRepository) {
		this.ventaRepository = ventaRepository;
		this.lineaRepository = lineaRepository;
		this.compradorRepository = compradorRepository;
		this.clienteRepository = clienteRepository;
	}
	
	@Transactional
	public Venta ventaActual() throws DataAccessException {
		//venta actual
		return this.ventaRepository.ventaActual();
	}
	
	@Transactional
	public Venta venta(final int id) throws DataAccessException {
		return this.ventaRepository.venta(id);
	}
	
	@Transactional
	public void finalizarVenta(Venta venta) throws DataAccessException { //TODO: Al realizar una venta debe actualizar el stock en producto
		venta = this.venta(venta.getId());
		venta.setFecha(LocalDate.now());
		if(venta.getImporteTotal() > venta.getPagado()) {
			Double diferencia = venta.getImporteTotal() - venta.getPagado();
			venta.setPorPagar(numberFormatter(diferencia));
		}
		venta.setEstadoVenta(EstadoVenta.Realizada);
		this.ventaRepository.save(venta);
		Venta nuevaVenta = new Venta();
		this.ventaRepository.save(nuevaVenta);
	}
	
	@Transactional
	public void updateVenta(Venta venta) throws DataAccessException{ //TODO: Este metodo podria controlar tambien que si ya no existe una linea de estupefaciente que elimine al comprador
		if(venta.getImporteTotal() > venta.getPagado()) {
			Double diferencia = venta.getImporteTotal() - venta.getPagado();
			venta.setPorPagar(numberFormatter(diferencia));
		}
		this.ventaRepository.save(venta);
	}
	
	@Transactional
	public void saveLinea(LineaVenta linea) throws DataAccessException{
		//guardando linea de venta
		Double pvp = linea.getProducto().getPvp();
		
		Double descuentoTasa = getTasaTypes(linea.getTipoTasa());
		Double importeLinea = pvp * descuentoTasa * linea.getCantidad();
		linea.setImporte(numberFormatter(importeLinea));
		
		Venta venta = this.ventaActual();
		Double importe = numberFormatter(venta.getImporteTotal() + linea.getImporte());
		venta.setImporteTotal(importe);
		venta.setPagado(importe);
		
		this.lineaRepository.save(linea);
		this.ventaRepository.save(venta);
	}
	
	@Transactional
	public void updateLinea(LineaVenta linea) throws DataAccessException {
		Venta venta = this.ventaActual();
		LineaVenta lineaVieja = this.lineaRepository.lineaVenta(linea.getId());
		venta.setImporteTotal(venta.getImporteTotal()-lineaVieja.getImporte());
		
		Double pvp = linea.getProducto().getPvp();
		Double descuentoTasa = getTasaTypes(linea.getTipoTasa());
		Double importeLinea = pvp * descuentoTasa * linea.getCantidad();
		linea.setImporte(numberFormatter(importeLinea));
		
		Double importe = numberFormatter(venta.getImporteTotal() + linea.getImporte());
		venta.setImporteTotal(importe);
		venta.setPagado(importe);
		
		this.lineaRepository.save(linea);
		this.ventaRepository.save(venta);
	}
	
	private Double numberFormatter(Double number) {
		DecimalFormat df = new DecimalFormat("#.00");
		String aux = df.format(number).replace(",", ".");
		return Double.parseDouble(aux);
	}
	
	@Transactional
	public void deleteLinea(LineaVenta linea) throws DataAccessException{
		//elimina linea de pedido
		Venta venta = this.ventaActual();
		Double importe = numberFormatter(venta.getImporteTotal() - linea.getImporte());
		venta.setImporteTotal(importe);
		venta.setPagado(importe);
		
		lineaRepository.delete(linea);
		this.ventaRepository.save(venta);
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
	
	@Transactional(readOnly = true)
	public Collection<Cliente> findClientes() {
		return (Collection<Cliente>) this.clienteRepository.findAll();
	}
	
	private Double getTasaTypes(TipoTasa TA) throws DataAccessException {
		switch (TA) {
		case TSI001:
			return 1.0;
		case TSI002:
			return 0.9;
		case TSI003:
			return 0.85;
		case TSI004:
			return 0.53;
		case TSI005:
			return 0.30;
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

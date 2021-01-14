package org.springframework.samples.farmatic.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.farmatic.model.Cliente;
import org.springframework.samples.farmatic.model.LineaPedido;
import org.springframework.samples.farmatic.model.LineaVenta;
import org.springframework.samples.farmatic.model.Pedido;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.model.Proveedor;
import org.springframework.samples.farmatic.model.TipoProducto;
import org.springframework.samples.farmatic.model.TipoTasa;
import org.springframework.samples.farmatic.model.Venta;
import org.springframework.samples.farmatic.model.Venta.EstadoVenta;
import org.springframework.samples.farmatic.model.Pedido.EstadoPedido;
import org.springframework.samples.farmatic.repository.CompradorRepository;
import org.springframework.samples.farmatic.repository.LineaVentaRepository;
import org.springframework.samples.farmatic.repository.ProveedorRepository;
import org.springframework.samples.farmatic.repository.VentaRepository;
import org.springframework.samples.farmatic.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VentaService {
	
	@Autowired
	private VentaRepository ventaRepository;
	
	@Autowired	
	private LineaVentaRepository lineaRepository;
	
	@Autowired	
	private CompradorRepository compradorRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	public VentaService(VentaRepository ventaRepository) {
		this.ventaRepository = ventaRepository;
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
	
	public Venta crearVenta() throws DataAccessException{
		Venta venta = new Venta();
		venta.setEstadoVenta(EstadoVenta.enProceso);
		venta.setFecha(LocalDate.now());
		venta.setImporteTotal(0.0);
		venta.setPagado(0.0);
		venta.setPorPagar(0.0);
		return venta;
		
	}
	
	@Transactional
	public void finalizarVenta(Venta venta) throws DataAccessException {
		venta = this.venta(venta.getId());
		venta.setFecha(LocalDate.now());
		Double importeTotal = 0.0;
		venta.setImporteTotal(importeTotal);
		if(venta.getPorPagar()==0) {
			venta.setPagado(importeTotal);
		}else {
			venta.setPagado(importeTotal - venta.getPorPagar());
		}
		venta.setEstadoVenta(EstadoVenta.Realizada);
		this.ventaRepository.save(venta);
	}
	
	@Transactional
	public void saveVenta(Venta venta) throws DataAccessException{
		//creando Venta
		ventaRepository.save(venta);
	}	
	
	@Transactional
	public void saveLinea(LineaVenta linea) throws DataAccessException{
		//guardando linea de venta
		lineaRepository.save(linea);
	}
	
	@Transactional
	public void deleteLinea(LineaVenta linea) throws DataAccessException{
		//elimina linea de pedido
		lineaRepository.delete(linea);
	}
	
	@Transactional
	public LineaVenta newLinea(Producto producto) throws DataAccessException{
		//creando linea de venta vacia
		Venta venta = new Venta();
		LineaVenta linea = new LineaVenta();
		linea.addProducto(producto);
		linea.addVenta(venta);
		linea.setCantidad(1);
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
	
	public Collection<TipoTasa> getTasaTypes() throws DataAccessException {
		Collection<TipoTasa> tipoTasa = new ArrayList<TipoTasa>();
		tipoTasa.add(TipoTasa.TSI001);
		tipoTasa.add(TipoTasa.TSI002);
		tipoTasa.add(TipoTasa.TSI003);
		tipoTasa.add(TipoTasa.TSI004);
		tipoTasa.add(TipoTasa.TSI005);
		
		return tipoTasa;
		
	}

}

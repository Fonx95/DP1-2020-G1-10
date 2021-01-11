package org.springframework.samples.farmatic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.farmatic.model.LineaPedido;
import org.springframework.samples.farmatic.model.LineaVenta;
import org.springframework.samples.farmatic.model.Pedido;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.model.Venta;
import org.springframework.samples.farmatic.repository.CompradorRepository;
import org.springframework.samples.farmatic.repository.LineaVentaRepository;
import org.springframework.samples.farmatic.repository.VentaRepository;
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
	public VentaService(VentaRepository ventaRepository) {
		this.ventaRepository = ventaRepository;
	}
	
	@Transactional
	public void saveVenta(Venta venta) throws DataAccessException{
		//creando Venta
		ventaRepository.save(venta);
	}
	
	@Transactional
	public Venta ventaActual() throws DataAccessException{
		//pedido actual
		return ventaRepository.ventaActual();
	}
	
	@Transactional
	public void saveLinea(LineaVenta linea) throws DataAccessException{
		//guardando linea de venta
		lineaRepository.save(linea);
	}
	
	@Transactional
	public LineaVenta newLinea(Producto producto) throws DataAccessException{
		//creando linea de venta vacia
		Venta venta = ventaActual();
		LineaVenta linea = new LineaVenta();
		linea.addProducto(producto);
		linea.addVenta(venta);
		linea.setCantidad(1);
		return linea;
	}

}

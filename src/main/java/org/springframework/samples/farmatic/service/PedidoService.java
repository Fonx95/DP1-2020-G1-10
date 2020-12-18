package org.springframework.samples.farmatic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.farmatic.model.LineaPedido;
import org.springframework.samples.farmatic.model.Pedido;

import org.springframework.samples.farmatic.model.Proveedor;
import org.springframework.samples.farmatic.model.Pedido.EstadoPedido;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.repository.LineaPedidoRepository;

import org.springframework.samples.farmatic.repository.PedidoRepository;
import org.springframework.samples.farmatic.repository.ProductoRepository;
import org.springframework.samples.farmatic.repository.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired	
	private LineaPedidoRepository lineaRepository;
	
	@Autowired	
	private ProveedorRepository proveedorRepository;
	
	@Autowired
	public PedidoService(PedidoRepository pedidoRepository) {
		this.pedidoRepository = pedidoRepository;
	}
	
	@Transactional(readOnly = true)
	public Collection<Pedido> findPedidos() throws DataAccessException{
		//listado pedidos
		return pedidoRepository.findAll();
	}
	@Transactional
	public void savePedido(Pedido pedido) throws DataAccessException{
		//creando Pedido
		pedidoRepository.save(pedido);
	}
	
	@Transactional
	public Pedido pedidoActual() throws DataAccessException{
		//pedido actual
		return pedidoRepository.pedidoActual();
	}
	
	@Transactional
	public Pedido pedido(int id) throws DataAccessException{
		return pedidoRepository.pedido(id);
	}
	
	@Transactional
	public Collection<LineaPedido> lineasPedido(int id) throws DataAccessException{
		//lineas del pedido
		return lineaRepository.lineaPedido(id);
	}
	
	@Transactional
	public Proveedor proveedor(int id) {
		return proveedorRepository.findById(id);
	}
	
	@Transactional(readOnly = true)
	public Collection<Proveedor> findProveedores() {
		return (Collection<Proveedor>) proveedorRepository.findAll();
	}
	
	@Transactional
	public void saveLinea(LineaPedido linea) throws DataAccessException{
		//guardando linea de pedido
		lineaRepository.save(linea);
	}
	
	@Transactional
	public LineaPedido newLinea(Producto producto) throws DataAccessException{
		//creando linea de pedido vacia
		Pedido pedido = pedidoActual();
		LineaPedido linea = new LineaPedido();
		linea.addProducto(producto);
		linea.addPedido(pedido);
		linea.setCantidad(1);
		return linea;
	}
}

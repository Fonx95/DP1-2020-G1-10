package org.springframework.samples.farmatic.service;

import java.time.LocalDate;
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
	
	private PedidoRepository pedidoRepository;
		
	private LineaPedidoRepository lineaRepository;
		
	private ProveedorRepository proveedorRepository;
	
	private ProductoRepository productoRepository;
	
	@Autowired
	public PedidoService(PedidoRepository pedidoRepository, LineaPedidoRepository lineaRepository, 
			ProveedorRepository proveedorRepository, ProductoRepository productoRepository) {
		this.pedidoRepository = pedidoRepository;
		this.lineaRepository = lineaRepository;
		this.proveedorRepository = proveedorRepository;
		this.productoRepository = productoRepository;
	}
	
	//---------Metodos referente a PEDIDOS---------
	
	@Transactional(readOnly = true)
	public Collection<Pedido> findPedidos() throws DataAccessException{
		//listado pedidos
		return pedidoRepository.findAll();
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
	public void pedidoRecibido(Pedido pedido) throws DataAccessException{
		pedido = pedido(pedido.getId());
		pedido.setEstadoPedido(EstadoPedido.Recibido);
		pedido.setFechaEntrega(LocalDate.now());
		for(LineaPedido linea:pedido.getLineaPedido()) {
			Producto producto = linea.getProducto();
			producto.sumaStock(linea.getCantidad());
			productoRepository.save(producto);
		}
		pedidoRepository.save(pedido);
	}
	
	@Transactional
	public void enviarPedido(Proveedor provedor) throws DataAccessException{
		Pedido pedido = pedidoActual();
		pedido.setProveedor(provedor);
		pedido.setEstadoPedido(EstadoPedido.Pedido);
		pedido.setFechaPedido(LocalDate.now());
		pedidoRepository.save(pedido);
		Pedido nuevoPedido = new Pedido();
		nuevoPedido.setCodigo(getCodigoPedidoNuevo(pedido.getCodigo()));
		pedidoRepository.save(nuevoPedido);
	}
	
	private String getCodigoPedidoNuevo(String codigo) {
		String res = "P-";
		String[] aux = codigo.split("P-");
		Integer num = Integer.parseInt(aux[1]);
		int n = num++, cifras = 0;
		while(n!=0) {
			n = n/10;
			cifras++;
		}
		if(cifras == 1) {
			res += "00" + num.toString();
		}else if(cifras == 2) {
			res += "0" + num.toString();
		}else {
			res += num.toString();
		}
		return res;
	}
	
	//---------Metodos referente a LINEAS DE PEDIDOS---------
	
	@Transactional
	public Collection<LineaPedido> lineasPedido(int id) throws DataAccessException{
		//lineas del pedido
		return lineaRepository.lineaPedido(id);
	}
	
	@Transactional
	public void saveLinea(LineaPedido linea) throws DataAccessException{
		//guardando linea de pedido
		lineaRepository.save(linea);
	}
	
	@Transactional
	public void deleteLinea(LineaPedido linea) throws DataAccessException{
		//elimina linea de pedido
		lineaRepository.delete(linea);
	}
	
	@Transactional
	public LineaPedido newLinea(Producto producto, Integer cantidad) throws DataAccessException{
		//creando linea de pedido vacia
		Pedido pedido = pedidoActual();
		LineaPedido linea = new LineaPedido();
		linea.addProducto(producto);
		linea.addPedido(pedido);
		linea.setCantidad(cantidad);
		return linea;
	}
	
	//---------Metodos referente a PROVEEDOR---------
	
	@Transactional
	public Proveedor proveedor(int id) {
		return proveedorRepository.findById(id);
	}
	
	@Transactional(readOnly = true)
	public Collection<Proveedor> findProveedores() {
		return (Collection<Proveedor>) proveedorRepository.findAll();
	}
}

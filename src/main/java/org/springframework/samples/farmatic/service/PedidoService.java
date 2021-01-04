
package org.springframework.samples.farmatic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.farmatic.model.LineaPedido;
import org.springframework.samples.farmatic.model.Pedido;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.model.Proveedor;
import org.springframework.samples.farmatic.model.User;
import org.springframework.samples.farmatic.repository.LineaPedidoRepository;
import org.springframework.samples.farmatic.repository.PedidoRepository;
import org.springframework.samples.farmatic.repository.ProveedorRepository;
import org.springframework.samples.farmatic.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository		pedidoRepository;

	@Autowired
	private LineaPedidoRepository	lineaRepository;

	@Autowired
	private ProveedorRepository		proveedorRepository;

	@Autowired
	private UserRepository			userRepository;


	@Autowired
	public PedidoService(final PedidoRepository pedidoRepository) {
		this.pedidoRepository = pedidoRepository;
	}

	@Transactional(readOnly = true)
	public Collection<Pedido> findPedidos() throws DataAccessException {
		//listado pedidos
		return this.pedidoRepository.findAll();
	}

	@Transactional
	public void savePedido(final Pedido pedido) throws DataAccessException {
		//creando Pedido
		this.pedidoRepository.save(pedido);
	}

	@Transactional
	public Pedido pedidoActual() throws DataAccessException {
		//pedido actual
		return this.pedidoRepository.pedidoActual();
	}

	@Transactional
	public Pedido pedido(final int id) throws DataAccessException {
		return this.pedidoRepository.pedido(id);
	}

	@Transactional
	public Collection<LineaPedido> lineasPedido(final int id) throws DataAccessException {
		//lineas del pedido
		return this.lineaRepository.lineaPedido(id);
	}

	@Transactional
	public Proveedor proveedor(final int id) {
		return this.proveedorRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Collection<Proveedor> findProveedores() {
		return (Collection<Proveedor>) this.proveedorRepository.findAll();
	}

	@Transactional
	public void saveLinea(final LineaPedido linea) throws DataAccessException {
		//guardando linea de pedido
		this.lineaRepository.save(linea);
	}

	@Transactional
	public LineaPedido newLinea(final Producto producto) throws DataAccessException {
		//creando linea de pedido vacia
		Pedido pedido = this.pedidoActual();
		LineaPedido linea = new LineaPedido();
		linea.addProducto(producto);
		linea.addPedido(pedido);
		linea.setCantidad(1);
		return linea;
	}

	public User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();             //Obtiene el nombre del ususario actual
		return this.userRepository.findByUsername(currentPrincipalName);         //Obtiene el usuario con ese nombre

	}

	@Transactional(readOnly = true)
	public Collection<Pedido> findMisPedidos() throws DataAccessException {
		//listado pedidos de un proveedor
		Proveedor p = this.proveedorRepository.findByUser(this.getCurrentUser());
		return p.getPedido();
	}
}


package org.springframework.samples.farmatic.service;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.farmatic.model.LineaPedido;
import org.springframework.samples.farmatic.model.Pedido;
import org.springframework.samples.farmatic.model.Pedido.EstadoPedido;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.model.Proveedor;
import org.springframework.samples.farmatic.model.User;
import org.springframework.samples.farmatic.repository.LineaPedidoRepository;
import org.springframework.samples.farmatic.repository.PedidoRepository;
import org.springframework.samples.farmatic.repository.ProductoRepository;
import org.springframework.samples.farmatic.repository.ProveedorRepository;
import org.springframework.samples.farmatic.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PedidoService {

	private PedidoRepository		pedidoRepository;

	private LineaPedidoRepository	lineaRepository;

	private ProveedorRepository		proveedorRepository;

	private ProductoRepository		productoRepository;

	private UserRepository			userRepository;


	@Autowired
	public PedidoService(final PedidoRepository pedidoRepository, final LineaPedidoRepository lineaRepository, final ProveedorRepository proveedorRepository, final ProductoRepository productoRepository, final UserRepository userRepository) {
		this.pedidoRepository = pedidoRepository;
		this.lineaRepository = lineaRepository;
		this.proveedorRepository = proveedorRepository;
		this.productoRepository = productoRepository;
		this.userRepository = userRepository;
	}

	//---------Metodos referente a PEDIDOS---------

	@Transactional(readOnly = true)
	public Collection<Pedido> findPedidos() throws DataAccessException {
		//listado pedidos
		return this.pedidoRepository.findAll();
	}

	@Transactional
	public Pedido pedidoActual() throws DataAccessException {
		//pedido actual
		Pedido p = this.pedidoRepository.pedidoActual();
		log.debug("El pedido acual tiene el id " + p.getId() + ", el codigo '" + p.getCodigo() + "' y el estado '" + p.getEstadoPedido() + "'");
		return p;
	}

	@Transactional
	public Pedido pedido(final int id) throws DataAccessException {
		Pedido p = this.pedidoRepository.pedido(id);
		log.debug("El pedido tiene el id " + p.getId() + ", el codigo '" + p.getCodigo() + "' y el estado '" + p.getEstadoPedido() + "'");
		return p;
	}

	@Transactional
	public void recibirPedido(Pedido pedido) throws DataAccessException {
		pedido = this.pedido(pedido.getId());
		pedido.setEstadoPedido(EstadoPedido.Recibido);
		pedido.setFechaEntrega(LocalDate.now());
		for (LineaPedido linea : pedido.getLineaPedido()) {
			Producto producto = linea.getProducto();
			producto.sumaStock(linea.getCantidad());
			this.productoRepository.save(producto);
		}
		this.pedidoRepository.save(pedido);
		log.debug("El pedido con codigo '" + pedido.getCodigo() + "' e id " + pedido.getId() + " tiene el estado " + pedido.getEstadoPedido());
	}

	@Transactional
	public void enviarPedido(final Proveedor provedor) throws DataAccessException {
		Pedido pedido = this.pedidoActual();
		pedido.setProveedor(provedor);
		pedido.setEstadoPedido(EstadoPedido.Pedido);
		pedido.setFechaPedido(LocalDate.now());
		this.pedidoRepository.save(pedido);
		log.debug("El pedido con codigo '" + pedido.getCodigo() + "' e id " + pedido.getId() + " tiene el estado " + pedido.getEstadoPedido());
		Pedido nuevoPedido = new Pedido();
		nuevoPedido.setCodigo(this.getCodigoPedidoNuevo(pedido.getCodigo()));
		this.pedidoRepository.save(nuevoPedido);
		log.debug("El nuevo pedido actual con codigo '" + nuevoPedido.getCodigo() + "' e id " + nuevoPedido.getId() + " tiene el estado " + nuevoPedido.getEstadoPedido());
	}

	private String getCodigoPedidoNuevo(final String codigo) {
		String res = "P-";
		String[] aux = codigo.split("P-");
		Integer num = Integer.parseInt(aux[1]);
		int n = num++, cifras = 0;
		while (n != 0) {
			n = n / 10;
			cifras++;
		}
		if (cifras == 1) {
			res += "00" + num.toString();
		} else if (cifras == 2) {
			res += "0" + num.toString();
		} else {
			res += num.toString();
		}
		return res;
	}

	//---------Metodos referente a LINEAS DE PEDIDOS---------

	@Transactional
	public Collection<LineaPedido> lineasPedido(final int id) throws DataAccessException {
		//lineas del pedido
		return this.lineaRepository.lineaPedido(id);
	}
	
	@Transactional 
	public LineaPedido lineaById(int id) throws DataAccessException {
		return this.lineaRepository.findById(id).get();
	}

	@Transactional
	public void saveLinea(final LineaPedido linea) throws DataAccessException {
		//guardando linea de pedido
		this.lineaRepository.save(linea);
		log.debug("La linea con el producto '" + linea.getProducto().getName() + "' se ha creado/modificado con una cantidad de " + linea.getCantidad());
	}
	
	@Transactional
	public Integer existelinea(Producto producto) {
		Collection<LineaPedido> lineas = this.pedidoActual().getLineaPedido();
		for(LineaPedido linea:lineas) {
			if(linea.getProducto().equals(producto)) return linea.getId();
		}
		return null;
	}

	@Transactional
	public void deleteLinea(LineaPedido linea) throws DataAccessException{
		//elimina linea de pedido
		lineaRepository.delete(linea);
		log.debug("La linea con el producto '" + linea.getProducto().getName() + "' se ha eliminado");
	}
	
	@Transactional
	public LineaPedido newLinea(final Producto producto, final Integer cantidad) throws DataAccessException {
		//creando linea de pedido vacia
		Pedido pedido = this.pedidoActual();
		LineaPedido linea = new LineaPedido();
		linea.addProducto(producto);
		linea.addPedido(pedido);
		linea.setCantidad(cantidad);
		return linea;
	}

	//---------Metodos referente a PROVEEDOR---------

	@Transactional
	public Proveedor proveedor(final int id) {
		Proveedor p = this.proveedorRepository.findById(id);
		log.debug("El proveedor tiene el id '" + p.getId() + "', nombre " + p.getEmpresa() + " y cif '" + p.getCif());
		return p;
	}

	@Transactional(readOnly = true)
	public Collection<Proveedor> findProveedores() {
		return (Collection<Proveedor>) this.proveedorRepository.findAll();
	}
	
	@Transactional
	public User getCurrentUser() throws DataAccessException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();             //Obtiene el nombre del ususario actual
		return this.userRepository.findByUsername(currentPrincipalName);         //Obtiene el usuario con ese nombre
	}

	@Transactional(readOnly = true)
	public Collection<Pedido> findMisPedidos(User user) throws DataAccessException {
		//listado pedidos de un proveedor
		Proveedor p = this.proveedorRepository.findByUser(user);
		return p.getPedido();
	}

	@Transactional
	public void pedidoEnviado(Pedido pedido) throws DataAccessException {
		pedido = this.pedido(pedido.getId());
		pedido.setEstadoPedido(EstadoPedido.Enviado);
		this.pedidoRepository.save(pedido);
		log.debug("El pedido con codigo '" + pedido.getCodigo() + "' e id " + pedido.getId() + " tiene el estado " + pedido.getEstadoPedido());
	}
}

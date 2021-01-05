
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
import org.springframework.samples.farmatic.repository.ProveedorRepository;
import org.springframework.samples.farmatic.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoService {
	
	private PedidoRepository pedidoRepository;
		
	private LineaPedidoRepository lineaRepository;
		
	private ProveedorRepository proveedorRepository;
	
	private ProductoRepository productoRepository;
  
  private UserRepository userRepository;
	
	@Autowired
	public PedidoService(PedidoRepository pedidoRepository, LineaPedidoRepository lineaRepository, ProveedorRepository proveedorRepository, ProductoRepository productoRepository, UserRepository userRepository) {
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
	public Collection<LineaPedido> lineasPedido(final int id) throws DataAccessException {
		//lineas del pedido
		return this.lineaRepository.lineaPedido(id);
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
	public void saveLinea(final LineaPedido linea) throws DataAccessException {
		//guardando linea de pedido
		this.lineaRepository.save(linea);
	}

	@Transactional
	public LineaPedido newLinea(Producto producto, Integer cantidad) throws DataAccessException{
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
	public Proveedor proveedor(int id) {
		return proveedorRepository.findById(id);
	}
	
	@Transactional(readOnly = true)
	public Collection<Proveedor> findProveedores() {
		return (Collection<Proveedor>) proveedorRepository.findAll();
  }
  
	private User getCurrentUser() {
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

	@Transactional
	public Pedido proveedorEnviarPedido(final Pedido pedido) {
		if (pedido.getEstadoPedido() == EstadoPedido.Borrador) {
			pedido.setEstadoPedido(EstadoPedido.Enviado);
		}
		return pedido;
	}
}

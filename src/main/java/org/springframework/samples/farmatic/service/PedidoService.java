
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
import org.springframework.samples.farmatic.repository.LineaPedidoRepository;
import org.springframework.samples.farmatic.repository.PedidoRepository;
import org.springframework.samples.farmatic.repository.ProductoRepository;
import org.springframework.samples.farmatic.service.exception.EstadoPedidoException;
import org.springframework.samples.farmatic.service.exception.LineaEmptyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PedidoService {

	private PedidoRepository		pedidoRepository;

	private LineaPedidoRepository	lineaRepository;

	private ProductoRepository		productoRepository;


	@Autowired
	public PedidoService(final PedidoRepository pedidoRepository, final LineaPedidoRepository lineaRepository, final ProductoRepository productoRepository) {
		this.pedidoRepository = pedidoRepository;
		this.lineaRepository = lineaRepository;
		this.productoRepository = productoRepository;
	}

	//---------Metodos referente a PEDIDOS---------

	@Transactional(readOnly = true)
	public Collection<Pedido> findPedidos() throws DataAccessException {
		//busqueda de todos los pedidos
		return this.pedidoRepository.findAll();
	}

	@Transactional
	public Pedido pedidoActual() throws DataAccessException {
		//busqueda del pedido actual
		Pedido p = this.pedidoRepository.pedidoActual();
		PedidoService.log.debug("El pedido acual tiene el id " + p.getId() + ", el codigo '" + p.getCodigo() + "' y el estado '" + p.getEstadoPedido() + "'");
		return p;
	}

	@Transactional
	public Pedido pedido(final int id) throws DataAccessException {
		//busqueda de un pedido por su id
		Pedido p = this.pedidoRepository.pedido(id);
		PedidoService.log.debug("El pedido tiene el id " + p.getId() + ", el codigo '" + p.getCodigo() + "' y el estado '" + p.getEstadoPedido() + "'");
		return p;
	}

	@Transactional(rollbackFor = EstadoPedidoException.class)
	public void recibirPedido(Pedido pedido) throws DataAccessException, EstadoPedidoException {
		//establece un pedido en recibido
		pedido = this.pedido(pedido.getId());

		if (pedido.getEstadoPedido() != EstadoPedido.Enviado) {
			throw new EstadoPedidoException();
		}

		pedido.setEstadoPedido(EstadoPedido.Recibido);//completa la informacion del pedido recibido(actualiza el estado y añade la fecha de entrega)
		pedido.setFechaEntrega(LocalDate.now());
		for (LineaPedido linea : pedido.getLineaPedido()) {//actualiza el stock del producto con las cantidades recibidas
			Producto producto = linea.getProducto();
			producto.sumaStock(linea.getCantidad());
			this.productoRepository.save(producto);
		}
		this.pedidoRepository.save(pedido);
		PedidoService.log.debug("El pedido con codigo '" + pedido.getCodigo() + "' e id " + pedido.getId() + " tiene el estado " + pedido.getEstadoPedido());
	}

	@Transactional
	public void enviarPedido(final Proveedor provedor) throws DataAccessException, LineaEmptyException {
		//establece un pedido en enviado
		Pedido pedido = this.pedidoActual();
		pedido.setProveedor(provedor);//completa la informacion del pedido enviado(asigna un proveedor, actualiza el estado y añade la fecha del pedido)
		pedido.setEstadoPedido(EstadoPedido.Pedido);
		pedido.setFechaPedido(LocalDate.now());

		if (pedido.getLineaPedido() == null || pedido.getLineaPedido().isEmpty()) {
			throw new LineaEmptyException("Pedido");
		}

		this.pedidoRepository.save(pedido);
		PedidoService.log.debug("El pedido con codigo '" + pedido.getCodigo() + "' e id " + pedido.getId() + " tiene el estado " + pedido.getEstadoPedido());
		Pedido nuevoPedido = new Pedido();//crea el nuevo pedido borrador con un codigo de pedido nuevo
		nuevoPedido.setCodigo(this.getCodigoPedidoNuevo(pedido.getCodigo()));
		this.pedidoRepository.save(nuevoPedido);
		PedidoService.log.debug("El nuevo pedido actual con codigo '" + nuevoPedido.getCodigo() + "' e id " + nuevoPedido.getId() + " tiene el estado " + nuevoPedido.getEstadoPedido());
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
	public LineaPedido lineaById(final int id) throws DataAccessException {
		//busqueda de una linea pedido por su id
		return this.lineaRepository.findById(id).get();
	}

	@Transactional
	public void saveLinea(final LineaPedido linea) throws DataAccessException {
		//crea o modifica una linea de pedido
		this.lineaRepository.save(linea);
		PedidoService.log.debug("La linea con el producto '" + linea.getProducto().getName() + "' se ha creado/modificado con una cantidad de " + linea.getCantidad());
	}

	@Transactional
	public Integer existelinea(final Producto producto) {
		//busca si existe un producto en las lineas de pedido del pedido actual, si es asi, devuelve su id y si no null
		Collection<LineaPedido> lineas = this.pedidoActual().getLineaPedido();
		for (LineaPedido linea : lineas) {
			if (linea.getProducto().equals(producto)) {
				return linea.getId();
			}
		}
		return null;
	}

	@Transactional
	public void deleteLinea(final LineaPedido linea) throws DataAccessException {
		//elimina linea de pedido
		this.lineaRepository.delete(linea);
		PedidoService.log.debug("La linea con el producto '" + linea.getProducto().getName() + "' se ha eliminado");
	}

	@Transactional
	public LineaPedido newLinea(final Producto producto, final Integer cantidad) throws DataAccessException {
		//crea una linea de pedido nueva java
		Pedido pedido = this.pedidoActual();
		LineaPedido linea = new LineaPedido();
		linea.addProducto(producto);
		linea.addPedido(pedido);
		linea.setCantidad(cantidad);
		return linea;
	}

	//---------Metodos referente a PROVEEDOR---------

	@Transactional(rollbackFor = EstadoPedidoException.class)
	public void pedidoEnviado(Pedido pedido) throws DataAccessException, EstadoPedidoException {
		//establece un pedido en enviado por el proveedor
		pedido = this.pedido(pedido.getId());

		if (pedido.getEstadoPedido() != EstadoPedido.Pedido) {
			throw new EstadoPedidoException();
		}

		pedido.setEstadoPedido(EstadoPedido.Enviado);//actualiza la informacion del estado del pedido
		this.pedidoRepository.save(pedido);
		PedidoService.log.debug("El pedido con codigo '" + pedido.getCodigo() + "' e id " + pedido.getId() + " tiene el estado " + pedido.getEstadoPedido());
	}
}

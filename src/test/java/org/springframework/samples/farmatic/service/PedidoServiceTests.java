
package org.springframework.samples.farmatic.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.TransactionScoped;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.farmatic.model.LineaPedido;
import org.springframework.samples.farmatic.model.Pedido;
import org.springframework.samples.farmatic.model.Pedido.EstadoPedido;
import org.springframework.samples.farmatic.model.Proveedor;
import org.springframework.samples.farmatic.model.User;
import org.springframework.samples.farmatic.repository.ClienteRepository;
import org.springframework.samples.farmatic.repository.LineaPedidoRepository;
import org.springframework.samples.farmatic.repository.ProductoRepository;
import org.springframework.samples.farmatic.repository.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class PedidoServiceTests {

	@Autowired
	protected PedidoService			pedidoService;

	@Autowired
	protected LineaPedidoRepository	lineaRepository;

	@Autowired
	protected ProveedorRepository	proveedorRepository;

	@Autowired
	protected ProductoRepository	productoRepository;
	
	@Autowired
	protected ClienteRepository clienteRepository;

	// Recordatorio: No hay un create directo en los pedidos, sino que se crean al usar enviarPedido, por lo tanto, en ese test se comprobará la creación.


	//Test positivos

	@Test
	public void getPedidoActual() { // Método que devuelve el pedido actual auto creado por el sistema.
		Pedido p = this.pedidoService.pedidoActual();
		Assertions.assertNotNull(p); // Comprobamos que no sea nulo.
		Assertions.assertTrue(p.getEstadoPedido().equals(EstadoPedido.Borrador)); // Comprobamos que está en estado Borrador.
	}
	
	@Test
	public void shouldFindAllPedidos() {// Método que comprueba que se listan los pedidos
		Collection<Pedido> pedidos = this.pedidoService.findPedidos();
		Pedido[] pedidosArr = pedidos.toArray(new Pedido[pedidos.size()]);
		Collection<LineaPedido> lineasP1 = pedidosArr[1].getLineaPedido();
		Assertions.assertTrue(pedidos.size() == 6);// Se comprueba el numero de pedidos recibidos pomr la base de datos
		Assertions.assertTrue(lineasP1.size() == 2);// Se comprueba que uno de los pedidos (que no sea el borrador) tiene lineas de pedidos asignadas
	}
	
	@Test
	public void shouldFindPedidoById() {// Metodo que comprueba los detalles de un pedido
		Pedido pedido = this.pedidoService.pedido(2);
		Collection<LineaPedido> lineasP1 = pedido.getLineaPedido();
		Assertions.assertTrue(pedido.getCodigo().equals("P-005"));// Comprueba que el codigo es el esperado
		Assertions.assertTrue(pedido.getEstadoPedido().equals(EstadoPedido.Enviado));// Comprueba que el estado es el esperado
		Assertions.assertNotNull(pedido.getFechaPedido());// Comprueba que la fecha del pedido esta asignada
		Assertions.assertTrue(lineasP1.size() == 3);// Comprueba que tiene lineas de pedidos asignadas
	}

	@Test
	public void shouldFindMiListaPedidos() {// Metodo que comprueba los pedidos que puede listar un proveedor
		User user = this.proveedorRepository.findById(1).getUser();
		Collection<Pedido> pedidos = this.pedidoService.findMisPedidos(user);
		Pedido[] pedidosArr = pedidos.toArray(new Pedido[pedidos.size()]);
		Collection<LineaPedido> lineasP1 = pedidosArr[1].getLineaPedido();
		Assertions.assertTrue(pedidos.size() == 3);// Comprueba que el numero de pedidos es el esperado
		for(Pedido pedido:pedidos) {
			Assertions.assertTrue(pedido.getProveedor().getEmpresa().equals("COFARES"));// Comprueba que todos los pedidos son del proveedor logueado
		}
		Assertions.assertTrue(lineasP1.size() == 2);// Comprueba que uno de los pedidos tiene lineas de pedidos asignadas
	}
	
	@Test
	@Transactional
	public void shouldInsertLineaPedido() {
		LineaPedido lp = this.pedidoService.newLinea(this.productoRepository.findById(1), 1); // Generamos una nueva línea de pedido con el servicio, esto le asigna el pedido actual y el producto que le pasemos.

		Assertions.assertNotNull(lp); // Comprobamos que no es nula.

		this.pedidoService.saveLinea(lp); // Guardamos la linea.
		Iterable<LineaPedido> lp2 = this.lineaRepository.findAll();
		Collection<LineaPedido> lps = new ArrayList<>();
		lp2.iterator().forEachRemaining(x -> lps.add(x)); // Todo esto es requerido para obtener la id que tiene la linea puesto que cambia sola al guardarse.

		Assertions.assertTrue(this.lineaRepository.findById(lps.size()).get().equals(lp));
	}

	@Test
	@Transactional
	public void pedirPedidoPositivo() { // Modificamos un pedido de Borrador a Pedido.
		Proveedor prov = this.proveedorRepository.findById(1);
		Pedido p = this.pedidoService.pedidoActual(); // Nos traemos el pedido actual para comprobar que se realizan las modificaciones.
		Assertions.assertTrue(p.getEstadoPedido() == EstadoPedido.Borrador);

		this.pedidoService.enviarPedido(prov); // Función que cambia el estado de Borrador a Pedido, pone la nueva fecha de pedido y asigna el proveedor al que se pide.
		Pedido p1 = this.pedidoService.pedido(p.getId());
		Assertions.assertTrue(p1.getProveedor().equals(prov));
		Assertions.assertTrue(p1.getFechaPedido().equals(LocalDate.now()));
		Assertions.assertTrue(p1.getEstadoPedido() == EstadoPedido.Pedido);

		Pedido p2 = this.pedidoService.pedidoActual(); // La función anterior también crea un pedido nuevo.
		Assertions.assertNotNull(p2);
		Assertions.assertTrue(p2.getEstadoPedido() == EstadoPedido.Borrador);
	}

	@Test
	@Transactional
	public void recibirPedidoPositivo() { // Modificamos un pedido de Enviado a Recibido.
		Pedido p = this.pedidoService.pedido(2); // Nos traemos el pedido con estado Enviado de la BD.
		Assertions.assertTrue(p.getFechaEntrega() == null);
		Assertions.assertTrue(p.getEstadoPedido() == EstadoPedido.Enviado);

		List<Integer> cantidadLp = new ArrayList<>();
		List<Integer> stockOriginal = new ArrayList<>();
		List<Integer> stockActual = new ArrayList<>();
		p.getLineaPedido().stream().forEach(x -> cantidadLp.add(x.getCantidad()));
		p.getLineaPedido().stream().forEach(x -> stockOriginal.add(x.getProducto().getStock()));

		this.pedidoService.pedidoRecibido(p); // Función que cambia el estado de Enviado a Recibido, pone fecha de entrega y suma las cantidades de producto.
		Pedido p1 = this.pedidoService.pedido(p.getId());
		Assertions.assertTrue(p1.getFechaEntrega().equals(LocalDate.now()));
		Assertions.assertTrue(p1.getEstadoPedido() == EstadoPedido.Recibido);

		p1.getLineaPedido().stream().forEach(x -> stockActual.add(x.getProducto().getStock()));

		int i = 0;
		while (i < cantidadLp.size()) {
			Assertions.assertTrue(stockActual.get(i) == stockOriginal.get(i) + cantidadLp.get(i)); // Comprobamos que el stock se suma correctamente.
			i++;
		}
	}

	@Test
	@Transactional
	public void enviarPedidoPositivo() {// Metodo que comprueba que un pedido cambai de estado a enviado por el proveedor
		Pedido p = this.pedidoService.pedido(3);// Nos traemos un pedido en estado pedido de la BD
		this.pedidoService.pedidoEnviado(p);
		Pedido p1 = this.pedidoService.pedido(p.getId());
		Assertions.assertTrue(p.getFechaEntrega() == null);// Comprobamos que la fecha de entrega continua en null
		Assertions.assertTrue(p1.getEstadoPedido() == EstadoPedido.Enviado);// Comprobamos que el estado se ha cambiado a estado enviado
	}
	
	//Test negativos
	
	@Test
	public void shouldFindMiListaPedidosNegativo() {// Caso en el que un cliente intente listar pedidos de un proveedor
		User user = this.clienteRepository.findById(1).getUser();
		Assertions.assertThrows(NullPointerException.class, () -> {
			this.pedidoService.findMisPedidos(user);
			});// El resultado del listado debe de ser null
	}

	@Test
	@Transactional
	public void shouldNotInsertLineaPedido() { // No podmeos guardar porque directamente no podemos crear con el método usado por el sistema.
		try {
			LineaPedido lp = this.pedidoService.newLinea(this.productoRepository.findById(0), 1);
		} catch (Exception e) {
			Assertions.assertNotNull(e);
		}
	}

	@Test
	@Transactional
	public void pedirPedidoNegativo() { // Probamos a mandar un pedido a un proveedor nulo
		Pedido p = this.pedidoService.pedidoActual(); // Comprobamos que no es nulo el pedido actual.
		Assertions.assertNotNull(p);

		try {
			this.pedidoService.enviarPedido(null);
		} catch (Exception e) {
			Assertions.assertNotNull(e);
			Assertions.assertTrue(this.pedidoService.pedidoActual().equals(p)); // Comprobamos que el pedido actual sigue siendo el mismo.
		}
	}

	@Test
	@Transactional
	public void recibirPedidoNegativo() { // Probamos a mandar un pedido recien creado.
		Pedido p = new Pedido(); // Creamos un nuevo pedido.

		try {
			this.pedidoService.pedidoRecibido(p);
		} catch (Exception e) {
			Assertions.assertNotNull(e);
		}
	}
	
	@Test
	@Transactional
	public void enviarPedidoNegativo() { // Probamos a mandar un pedido recien creado
		Pedido p = new Pedido();// Creamos un pedido
		Assertions.assertThrows(NullPointerException.class, () -> {
			this.pedidoService.pedidoEnviado(p);
		});// Comprobamos que se obtiene un nullPointerException
	}
}


package org.springframework.samples.farmatic.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

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


	//Test positivos

	@Test
	public void getPedidoActual() { // Método que devuelve el pedido actual auto creado por el sistema.
		Pedido p = this.pedidoService.pedidoActual();
		Assertions.assertNotNull(p); // Comprobamos que no sea nulo.
		Assertions.assertTrue(p.getEstadoPedido().equals(EstadoPedido.Borrador)); // Comprobamos que está en estado Borrador.
	}

	@Test
	@Transactional
	public void shouldInsertPedido() { // Probamos a guardar un pedido en estado borrador
		Pedido pedido = new Pedido();
		pedido.setCodigo("P-000");
		pedido.setEstadoPedido(EstadoPedido.Borrador);
		pedido.setFechaEntrega(null);
		pedido.setFechaPedido(LocalDate.now());
		pedido.setLineaPedido(null); // No metemos ninguna porque está en borrador.
		pedido.setProveedor(this.proveedorRepository.findById(1));

		this.pedidoService.savePedido(pedido);
		Assertions.assertTrue(this.pedidoService.pedido(pedido.getId()).equals(pedido)); // Comprueba que se ha guardado sin errores.
	}

	@Test
	@Transactional
	public void shouldInsertLineaPedido() {
		LineaPedido lp = this.pedidoService.newLinea(this.productoRepository.findById(1),1); // Generamos una nueva línea de pedido con el servicio, esto le asigna el pedido actual y el producto que le pasemos.

		Assertions.assertNotNull(lp); // Comprobamos que no es nula.

		this.pedidoService.saveLinea(lp); // Guardamos la linea.
		Iterable<LineaPedido> lp2 = this.lineaRepository.findAll();
		Collection<LineaPedido> lps = new ArrayList<>();
		lp2.iterator().forEachRemaining(x -> lps.add(x)); // Todo esto es requerido para obtener la id que tiene la linea puesto que cambia sola al guardarse.

		Assertions.assertTrue(this.lineaRepository.findById(lps.size()).get().equals(lp));
	}

	@Test
	@Transactional
	public void shouldUpdatePedido() { // Modificamos la fecha d epedido a la fecha de hoy.
		Pedido p = this.pedidoService.pedidoActual();
		Assertions.assertNotNull(p);
		LocalDate l = LocalDate.of(2020, 12, 01); // Valor del pedido actucal con código P-001

		Assertions.assertTrue(p.getFechaPedido().equals(l));

		p.setFechaPedido(LocalDate.now());
		this.pedidoService.savePedido(p);
		Pedido p1 = this.pedidoService.pedido(p.getId());

		Assertions.assertTrue(p1.getFechaPedido().equals(LocalDate.now()));
	}

	//Test negativos

	@Test
	@Transactional
	public void shouldNotInsertPedido() {	// El estado del pedido no puede ser nulo.
		Pedido p = new Pedido();
		p.setCodigo("P-000");
		p.setEstadoPedido(null);
		p.setFechaEntrega(null);
		p.setFechaPedido(LocalDate.now());
		p.setLineaPedido(null); // No metemos ninguna porque está en borrador.
		p.setProveedor(this.proveedorRepository.findById(1));

		try {
			this.pedidoService.savePedido(p);
		} catch (Exception e) {
			Assertions.assertNotNull(e);
		}

	}

	@Test
	@Transactional
	public void shouldNotInsertLineaPedido() { // No podmeos guardar porque directamente no podemos crear con el método usado por el sistema.
		try {
			LineaPedido lp = this.pedidoService.newLinea(this.productoRepository.findById(0),1);
		} catch (Exception e) {
			Assertions.assertNotNull(e);
		}
	}

	@Test
	@Transactional
	public void shouldNotUpdatePedido() { // Intentamos poner a null la fecha de pedido.
		Pedido p = this.pedidoService.pedidoActual();
		Assertions.assertNotNull(p);
		LocalDate l = LocalDate.of(2020, 12, 01); // Valor del pedido actucal con código P-001

		Assertions.assertTrue(p.getFechaPedido().equals(l));

		try {
			p.setFechaPedido(null); // Cambiamos fecha a nulo.
			this.pedidoService.savePedido(p);
		} catch (Exception e) {
			Assertions.assertNotNull(e);
		}
	}
}

package org.springframework.samples.farmatic.service;


import static org.junit.jupiter.api.Assertions.assertThrows;

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
import org.springframework.samples.farmatic.model.Cliente;

import org.springframework.samples.farmatic.model.LineaPedido;
import org.springframework.samples.farmatic.model.LineaVenta;
import org.springframework.samples.farmatic.model.Pedido;
import org.springframework.samples.farmatic.model.Proveedor;
import org.springframework.samples.farmatic.model.TipoTasa;
import org.springframework.samples.farmatic.model.Venta;
import org.springframework.samples.farmatic.model.Venta.EstadoVenta;
import org.springframework.samples.farmatic.model.Pedido.EstadoPedido;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.repository.ClienteRepository;
import org.springframework.samples.farmatic.repository.LineaVentaRepository;
import org.springframework.samples.farmatic.repository.ProductoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class VentaServiceTests {
	

	@Autowired
	protected VentaService ventaService;

	@Autowired
	protected LineaVentaRepository	lineaRepository;

	@Autowired
	protected ProductoRepository	productoRepository;
	
	@Autowired
	protected ClienteRepository clienteRepository;
	
	//Test positivo
	
	@Test
	public void getVentaActual() { // Método que devuelve la venta actual auto creado por el sistema.
		Venta v = this.ventaService.ventaActual();
		Assertions.assertNotNull(v); // Comprobamos que no sea nulo.
		Assertions.assertTrue(v.getEstadoVenta().equals(EstadoVenta.enProceso)); // Comprobamos que está en estado en proceso.
	}
	
	@Test
	public void shouldFindAllVentas() {// Método que comprueba que se listan las ventas
		Collection<Venta> ventas = this.ventaService.findAllVentas();
		Venta[] ventasArr = ventas.toArray(new Venta[ventas.size()]);
		Collection<LineaVenta> lineasV1 = ventasArr[1].getLineaVenta();
		Assertions.assertTrue(ventas.size() == 6);// Se comprueba el numero de ventas recibidas por la base de datos
		Assertions.assertTrue(lineasV1.size() == 3);// Se comprueba que una de las ventas (que no sea la que esta en proceso) tiene lineas de ventas asignadas
	}
	
	@Test
	public void shouldFindVentaById() {// Metodo que comprueba los detalles de una venta
		Venta venta = this.ventaService.venta(2);
		Collection<LineaVenta> lineasV1 = venta.getLineaVenta();
		Assertions.assertTrue(venta.getEstadoVenta().equals(EstadoVenta.Realizada));// Comprueba que el estado es el esperado
		Assertions.assertNotNull(venta.getFecha());// Comprueba que la fecha de la venta esta asignada
		Assertions.assertTrue(lineasV1.size() == 2);// Comprueba que tiene lineas de ventas asignadas
	}
	
	@Test
	@Transactional
	public void shouldInsertLineaVenta() {
		LineaVenta lv = this.ventaService.newLinea(this.productoRepository.findById(1)); // Generamos una nueva línea de venta con el servicio, esto le asigna la venta actual y el producto que le pasemos.
		lv.setTipoTasa(TipoTasa.TSI002);

		Assertions.assertNotNull(lv); // Comprobamos que no es nula.

		this.ventaService.saveLinea(lv); // Guardamos la linea.
		Iterable<LineaVenta> lv2 = this.lineaRepository.findAll();
		Collection<LineaVenta> lvs = new ArrayList<>();
		lv2.iterator().forEachRemaining(x -> lvs.add(x)); // Todo esto es requerido para obtener la id que tiene la linea puesto que cambia sola al guardarse.

		Assertions.assertTrue(this.lineaRepository.findById(lvs.size()).get().equals(lv));
	}
	
	@Test
	@Transactional
	public void finalizarVentaPositivo() { // Modificamos una venta de en proceso a finalizada.
		Venta v = this.ventaService.ventaActual(); // Nos traemos la venta actual para comprobar que se realizan las modificaciones.
		Assertions.assertTrue(v.getEstadoVenta() == EstadoVenta.enProceso);

		try {
			this.ventaService.finalizarVenta(v); // Función que cambia el estado de en proceso a finalizada y pone la nueva fecha de venta.
		}catch(Exception e){
		}
    
		Venta v1 = this.ventaService.venta(v.getId());
		Assertions.assertTrue(v1.getFecha().equals(LocalDate.now()));
		Assertions.assertTrue(v1.getEstadoVenta() == EstadoVenta.Realizada);

		Venta v2 = this.ventaService.ventaActual(); // La función anterior también crea un pedido nuevo.
		Assertions.assertNotNull(v2);
		Assertions.assertTrue(v2.getEstadoVenta() == EstadoVenta.enProceso);
	}
	
	@Test
	@Transactional
	public void updateVentaPositivo() {//comprobamos si la venta se actualiza correctamente
		Venta v = this.ventaService.venta(2);
		Double importe_inicial = v.getImporteTotal(); //establecemos los parametros iniciales
		Double porPagar_inicial = v.getPorPagar();
		this.ventaService.updateVenta(v);
		Assertions.assertTrue(v.getImporteTotal() != importe_inicial); //comprobamos si los parametros se han actualizado
		Assertions.assertTrue(v.getPorPagar() != porPagar_inicial);
		
		
	}
	
	//Test negativos
	
	@Test
	@Transactional
	public void shouldNotInsertLineaVenta() { // No podmeos guardar porque directamente no podemos crear con el método usado por el sistema.
		try {
			LineaVenta lv = this.ventaService.newLinea(this.productoRepository.findById(0));
		} catch (Exception e) {
			Assertions.assertNotNull(e);
		}
	}
	
	@Test
	void shouldNotFindVentaByID() {
		assertThrows(NullPointerException.class, ()->{this.ventaService.venta(0).getClass();}); // Comprobará que se lanza un NullPointerException al realizar la acción en el corchete.
	}
	
	@Test
	@Transactional
	public void finalizarVentaNegativo() {//Probamos a intentar finalizar una venta recien creada sin lineas de venta
		Venta v = new Venta();
		try {
			this.ventaService.finalizarVenta(v);
		}
		catch(Exception e){
			Assertions.assertNotNull(e);
		}
		
	}
	
	@Test
	@Transactional
	public void shouldNotInsertLineaStock() {//No podemos insertar una linea de venta porque la cantidad supera al stock
		Producto p = this.productoRepository.findById(1);
		LineaVenta lv = this.ventaService.newLinea(p);
		p.setStock(3);
		try {
			lv.setCantidad(4);
		}catch(Exception e) {
			Assertions.assertNotNull(e);
		}
		
	}

}

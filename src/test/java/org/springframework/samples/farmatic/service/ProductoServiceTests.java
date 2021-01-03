
package org.springframework.samples.farmatic.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.service.ProductoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
class ProductoServiceTests {

	@Autowired
	protected ProductoService productoService;


	//Test positivos
	@Test
	void shouldFindProducts() {
		Iterable<Producto> aux = this.productoService.findProducts();
		System.out.println(aux);
		assertThat(aux.iterator().next() != null); // Comprobará que lo devuelto no sea nulo.

		Collection<Producto> lista = new HashSet<>();
		aux.iterator().forEachRemaining(x -> lista.add(x));
		assertThat(lista.size()).isEqualTo(9); // Comprobará que el número de elementos de la lista sea correcto. Depende de la base de datos.
	}

	@Test
	void shouldFindProductoByID() {
		Producto p = this.productoService.findProductoById(1);
		assertThat(p.getCode().equals("PR-001"));
	}
	
	@Test
	@Transactional
	public void shouldInsertProduct() {
		Collection<Producto> products = new ArrayList<>();
		this.productoService.findProducts().forEach(p->products.add(p));
		int found = products.size();

		Producto producto = new Producto();
		producto.setCode("PR-001");          
		producto.setName("farmaco1");
		producto.setName("FarmacoSinReceta");
		producto.setPvp(5.0);
		producto.setPvf(4.0);
		producto.setStock(15);
		producto.setMinStock(5);
                
		this.productoService.saveProducto(producto);
		
		assertThat(producto.getId().longValue()).isNotEqualTo(0);

		Collection<Producto> products2 = new ArrayList<>();
		this.productoService.findProducts().forEach(p->products2.add(p));
		assertThat(products2.size()).isEqualTo(found + 1);
	}
	
	//Test negativos
	
	@Test
	void shouldNotFindProductoByID() {
		assertThrows(NullPointerException.class, ()->{this.productoService.findProductoById(0).getClass();}); // Comprobará que se lanza un NullPointerException al realizar la acción en el corchete.
	}
}


package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.HashSet;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.service.ProductoService;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
class ProductoServiceTests {

	@Autowired
	protected ProductoService productoService;

/*
	//Test positivos
	@Test
	void shouldFindProducts() {
		Iterable<Producto> aux = this.productoService.findProducts();
		System.out.println(aux);
		Assert.assertNotNull(aux.iterator().next()); // Comprobará que lo devuelto no sea nulo.

		Collection<Producto> lista = new HashSet<>();
		aux.iterator().forEachRemaining(x -> lista.add(x));
		Assertions.assertThat(lista.size()).isEqualTo(2); // Comprobará que el númeor de elementos de la lista sea correcto. Depende de la base de datos.
	}

	@Test
	void shouldFindProductoByID() {
		Producto p = this.productoService.findProductoById(1);

	}
	//Test negativos
*/
}

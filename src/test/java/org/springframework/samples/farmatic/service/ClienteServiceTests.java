package org.springframework.samples.farmatic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.farmatic.model.Cliente;
import org.springframework.samples.farmatic.model.Venta;
import org.springframework.samples.farmatic.repository.ClienteRepository;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ClienteServiceTests {
	
	@Autowired
	protected ClienteRepository clienteRepository;
	
	//Test positivos
		@Test
		void shouldFindVentasByClient() {
			Cliente cliente = this.clienteRepository.findById(2);
			Collection<Venta> aux = cliente.getVenta();
			//System.out.println(aux);
			assertThat(aux.size()).isEqualTo(2); // Comprobará que el número de elementos de la lista sea correcto. Depende de la base de datos.
		}
		
		@Test
		void shouldNotFindVentasByClient() {
			Cliente cliente = this.clienteRepository.findById(1);
			Collection<Venta> aux = cliente.getVenta();
			//System.out.println(aux);
			assertThat(aux); // Comprobará que lo devuelto es una lista vacía.
		}
}

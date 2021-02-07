package org.springframework.samples.farmatic.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;

import javax.validation.ConstraintViolationException;

import org.assertj.core.api.Assertions;
 
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.farmatic.model.Authorities;
import org.springframework.samples.farmatic.model.Cliente;
import org.springframework.samples.farmatic.model.Farmaceutico;
import org.springframework.samples.farmatic.model.User;
import org.springframework.samples.farmatic.model.Venta;
import org.springframework.samples.farmatic.repository.ClienteRepository;
import org.springframework.samples.farmatic.repository.FarmaceuticoRepository;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ClienteServiceTests {
	
	@Autowired
	protected ClienteService clienteService;
	@Autowired
	protected UserService UserService;
	@Autowired
	protected ClienteRepository clienteRepository;
	
	//Test positivos
		@Test
		void shouldFindVentasByClient() {
			Cliente cliente = this.clienteRepository.findById(2);
			Iterable<Venta> aux = cliente.getVenta();
			//System.out.println(aux);
			assertThat(aux.iterator().next() != null); // Comprobará que lo devuelto no sea nulo.

			Collection<Venta> lista = new HashSet<>();
			aux.iterator().forEachRemaining(x -> lista.add(x));
			assertThat(lista.size()).isEqualTo(2); // Comprobará que el número de elementos de la lista sea correcto. Depende de la base de datos.
		}
		
		void shouldNotFindVentasByClient() {
			Cliente cliente = this.clienteRepository.findById(1);
			Collection<Venta> aux = cliente.getVenta();
			//System.out.println(aux);
			assertThat(aux); // Comprobará que lo devuelto es una lista vacía.
		}
		
		//Test positivos
		
		@Test
		void shouldFindById() {
			Assertions.assertThat(this.clienteService.findClienteById(1)).isNotNull();
		}
		
		@Test
		void shouldFindAll() {
			Assertions.assertThat(this.clienteService.findClientes()).isNotEmpty();
		}

		@Test
		void shouldFinClienteUser() {
			User user=this.UserService.findUser("client3").get();
			Assertions.assertThat(this.clienteService.findClienteUser(user)).isNotNull();
		}
		@Test
		public void saveCliente(){

			Cliente cliente = new Cliente();
			cliente.setProvincia("Sevilla");
			cliente.setLocalidad("Alcala");
			cliente.setDireccion("Calle Pinto");
			cliente.setPorPagarTotal(35.0);
			User user= new User();
			user.setCliente(cliente);
			user.setPassword("1234");
			user.setUsername("cliente1");
			cliente.setDni("20035098Y");
			cliente.setSurnames("Maria Polo");
			
			

			this.clienteService.saveCliente(cliente);
			Cliente client1=this.clienteService.clienteDni("20035098Y");
			Assertions.assertThat(this.clienteService.clienteDni("20035098Y")).isNotNull();
			
		}
		//Test Negativos
		@Test
		void shouldNotFindById() {
			Assertions.assertThat(this.clienteService.findClienteById(-1)).isNull();
		}

		@Test
		void shouldNotFinClienteUser() {
			User user=this.UserService.findUser("farm1").get();
			
			Assertions.assertThat(this.clienteService.findClienteUser(user)).isNull();
		
		}
		@Test
		void notSaveCliente(){

			Cliente cliente = new Cliente();
			
			cliente.setProvincia("Sevilla");
			cliente.setLocalidad("Alcala");
			cliente.setDireccion("Calle Pinto");
			cliente.setPorPagarTotal(35.0);
			User user= new User();
			user.setCliente(cliente);
			user.setPassword("1234");
			user.setUsername("cliente1");
			cliente.setDni("20035098Y");
			cliente.setSurnames("");
			

			//this.clienteService.saveCliente(cliente);
			assertThrows(ConstraintViolationException.class, ()->{this.clienteService.saveCliente(cliente);});
			//assertThrows(ConstraintViolationException.class, ()->{this.clienteService.clienteDni("20035098Y").getClass();});
			
		}
}

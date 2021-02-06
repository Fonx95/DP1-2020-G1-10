package org.springframework.samples.farmatic.service;

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
import org.springframework.samples.farmatic.model.User;
import org.springframework.samples.farmatic.repository.ClienteRepository;
import org.springframework.samples.farmatic.repository.ProveedorRepository;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class ProveedorServiceTests {
	
	@Autowired
	protected ProveedorService  proveedorService;
	
	@Autowired
	protected ProveedorRepository	proveedorRepository;
	
	@Autowired
	protected ClienteRepository clienteRepository;
	
	//Test positivos
	
	@Test
	public void shouldFindPedidosProveedor() {// Metodo que comprueba los pedidos que puede listar un proveedor
		User user = this.proveedorRepository.findById(1).getUser();
		Collection<Pedido> pedidos = this.proveedorService.findPedidosProveedor(user);
		Pedido[] pedidosArr = pedidos.toArray(new Pedido[pedidos.size()]);
		Collection<LineaPedido> lineasP1 = pedidosArr[1].getLineaPedido();
		Assertions.assertTrue(pedidos.size() == 3);// Comprueba que el numero de pedidos es el esperado
		for(Pedido pedido:pedidos) {
			Assertions.assertTrue(pedido.getProveedor().getEmpresa().equals("COFARES"));// Comprueba que todos los pedidos son del proveedor logueado
		}
		Assertions.assertTrue(lineasP1.size() == 2);// Comprueba que uno de los pedidos tiene lineas de pedidos asignadas
	}
	
	//Test negativos
	
	@Test
	public void shouldFindPedidosProveedorNegativo() {// Caso en el que un cliente intente listar pedidos de un proveedor
		User user = this.clienteRepository.findById(1).getUser();
		Assertions.assertThrows(NullPointerException.class, () -> {
			this.proveedorService.findPedidosProveedor(user);
			});// El resultado del listado debe de ser null
	}
	
}

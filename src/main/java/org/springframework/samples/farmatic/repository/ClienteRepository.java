package org.springframework.samples.farmatic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.farmatic.model.Cliente;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.model.Proveedor;
import org.springframework.samples.farmatic.model.User;


public interface ClienteRepository extends  CrudRepository<Cliente, String>{

	@Query("SELECT cliente FROM Cliente cliente WHERE cliente.id =:id")
	Cliente findById(@Param("id") int id);
	
	@Query("SELECT cliente FROM Cliente cliente WHERE cliente.user =:user")
	Cliente findByUser(@Param("user") User user);
	
	@Query("SELECT cliente FROM Cliente cliente WHERE cliente.dni =:dni")
	Cliente fingByDni(@Param("dni") String dni);
}

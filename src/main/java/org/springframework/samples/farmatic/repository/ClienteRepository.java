package org.springframework.samples.farmatic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.farmatic.model.Cliente;
import org.springframework.samples.farmatic.model.User;

public interface ClienteRepository extends  CrudRepository<Cliente, Integer>{

	@Query("SELECT cliente FROM Cliente cliente WHERE cliente.id =:id")
	Cliente findById(@Param("id") int id);
	
	@Query("SELECT cliente FROM Cliente cliente WHERE cliente.user =:user")
	Cliente findByUser(@Param("user") User user);
	
	@Query("SELECT cliente FROM Cliente cliente WHERE cliente.dni =:dni")
	Cliente fingByDni(@Param("dni") String dni);
	
	@Query("SELECT cliente FROM Cliente cliente")
	Collection<Cliente> findAll() throws DataAccessException;
}

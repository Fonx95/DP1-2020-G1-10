package org.springframework.samples.farmatic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.samples.farmatic.model.Pedido;

public interface PedidoRepository extends CrudRepository<Pedido, String>{
	
	Collection<Pedido> findAll() throws DataAccessException;

}

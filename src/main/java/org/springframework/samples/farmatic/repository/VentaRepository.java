package org.springframework.samples.farmatic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.farmatic.model.Pedido;
import org.springframework.samples.farmatic.model.Venta;

public interface VentaRepository extends CrudRepository<Venta, String>{
	
	Collection<Venta> findAll() throws DataAccessException;
	
	

}

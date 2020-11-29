package org.springframework.samples.farmatic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.farmatic.model.Farmaceutico;
import org.springframework.samples.farmatic.model.Producto;

public interface ProductoRepository extends CrudRepository<Producto, String>{
	
	Collection<Producto> findAll() throws DataAccessException;

	@Query("SELECT producto FROM Producto producto WHERE producto.id =:id")
	public Producto findById(@Param("id") int id);
}

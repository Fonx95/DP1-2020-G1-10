package org.springframework.samples.farmatic.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.farmatic.model.Farmaceutico;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.model.TipoProducto;

public interface ProductoRepository extends CrudRepository<Producto, String>{
	

	Iterable<Producto> findAll() throws DataAccessException;


	@Query("SELECT producto FROM Producto producto WHERE producto.id =:id")
	public Producto findById(@Param("id") int id);
	

}

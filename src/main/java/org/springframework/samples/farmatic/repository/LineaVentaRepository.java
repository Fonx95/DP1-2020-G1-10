package org.springframework.samples.farmatic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.farmatic.model.LineaVenta;

public interface LineaVentaRepository extends CrudRepository<LineaVenta, String>{
	
	@Query("SELECT linea FROM LineaVenta linea WHERE linea.venta = :venta_id")
	Collection<LineaVenta> lineaVenta(@Param("venta_id") int id) throws DataAccessException;

}

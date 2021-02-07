package org.springframework.samples.farmatic.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.farmatic.model.LineaVenta;

public interface LineaVentaRepository extends CrudRepository<LineaVenta, Integer>{
	
	@Query("SELECT linea FROM LineaVenta linea WHERE linea.id = :venta_id")
	LineaVenta lineaVenta(@Param("venta_id") int id) throws DataAccessException;

}

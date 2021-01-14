package org.springframework.samples.farmatic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.farmatic.model.Pedido;
import org.springframework.samples.farmatic.model.Venta;

public interface VentaRepository extends CrudRepository<Venta, String>{
	
	Collection<Venta> findAll() throws DataAccessException;
	
	@Query("SELECT venta FROM Venta venta WHERE venta.estadoVenta = 0")
	Venta ventaActual() throws DataAccessException;
	
	@Query("SELECT venta FROM Venta venta WHERE venta.id = :id")
	Venta venta(@Param("id") int id) throws DataAccessException;

}

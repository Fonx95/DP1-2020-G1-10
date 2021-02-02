package org.springframework.samples.farmatic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.farmatic.model.Comprador;
import org.springframework.samples.farmatic.model.Proveedor;

public interface CompradorRepository extends CrudRepository<Comprador, Integer>{
	
	@Query("SELECT proveedor FROM Proveedor proveedor WHERE proveedor.id =:id")
	public Proveedor findById(@Param("id") int id);


}

package org.springframework.samples.farmatic.repository;

import java.util.Collection;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.model.TipoMedicamento;

public interface ProductoRepository extends CrudRepository<Producto, Integer>{
	

	Iterable<Producto> findAll() throws DataAccessException;


	@Query("SELECT producto FROM Producto producto WHERE producto.id =:id")
	public Producto findById(@Param("id") int id);
	
	@Query("SELECT producto FROM Producto producto WHERE producto.code LIKE :codigo")
	public Producto findByCode(@Param("codigo") String codigo);
	
	@Query("SELECT producto FROM Producto producto WHERE (INSTR(producto.name,:name) > 0)")
	public Collection<Producto> findAllByName(@Param("name") String name);

	public Collection<Producto> findByTipoMedicamento(TipoMedicamento tipo);
}

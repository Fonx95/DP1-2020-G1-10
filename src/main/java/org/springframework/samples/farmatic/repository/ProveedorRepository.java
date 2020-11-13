package org.springframework.samples.farmatic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.farmatic.model.Proveedor;

public interface ProveedorRepository extends CrudRepository<Proveedor,Integer >{
	
//	void save(Proveedor proveedor) throws DataAccessException;
//
//
//	@Query("SELECT DISTINCT proveedor FROM Proveedor proveedor left join fetch proveedor.empresa WHERE proveedor.empresa LIKE :empresa%")
//	public Collection<Proveedor> findByEmpresa(@Param("empresa") String empresa);
//
//
//
//	@Query("SELECT empresa FROM Proveedor proveedor left join fetch proveedor.empresas WHERE empresa.id =:id")
//	public Proveedor findById(@Param("id") int id);

}

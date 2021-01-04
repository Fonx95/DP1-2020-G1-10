
package org.springframework.samples.farmatic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.farmatic.model.Proveedor;
import org.springframework.samples.farmatic.model.User;

public interface ProveedorRepository extends CrudRepository<Proveedor, String> {

	@Query("SELECT proveedor FROM Proveedor proveedor WHERE proveedor.id =:id")
	Proveedor findById(@Param("id") int id);

	@Query("SELECT proveedor FROM Proveedor proveedor WHERE proveedor.user =:user")
	Proveedor findByUser(@Param("user") User user);

}

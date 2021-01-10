package org.springframework.samples.farmatic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.farmatic.model.Farmaceutico;
import org.springframework.samples.farmatic.model.User;


public interface FarmaceuticoRepository extends  CrudRepository<Farmaceutico, String>{
	@Query("SELECT farmaceutico FROM Farmaceutico farmaceutico WHERE farmaceutico.id =:id")
	Farmaceutico findById(@Param("id") int id);

	@Query("SELECT farmaceutico FROM Farmaceutico farmaceutico WHERE farmaceutico.user =:user")
	Farmaceutico findByUser(@Param("user") User user);
}

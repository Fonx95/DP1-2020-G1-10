package org.springframework.samples.farmatic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.farmatic.model.Registrado;
import org.springframework.samples.farmatic.model.User;


public interface RegistradoRepository extends  CrudRepository<Registrado, String>{
	
}

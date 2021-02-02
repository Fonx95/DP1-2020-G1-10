package org.springframework.samples.farmatic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.farmatic.model.Authorities;



public interface AuthoritiesRepository extends  CrudRepository<Authorities, Integer>{
	
}

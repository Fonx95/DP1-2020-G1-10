package org.springframework.samples.farmatic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.farmatic.model.Authorities;
import org.springframework.samples.farmatic.model.User;



public interface AuthoritiesRepository extends  CrudRepository<Authorities, Integer>{
	
	Authorities findByUser(User user);
	
}


package org.springframework.samples.farmatic.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.farmatic.model.User;

public interface UserRepository extends CrudRepository<User, String> {

	User findByUsername(String currentPrincipalName);
	
	Collection<User> findAll();

}

package org.springframework.samples.farmatic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.farmatic.model.Cliente;


public interface ClienteRepository extends  CrudRepository<Cliente, String>{

}

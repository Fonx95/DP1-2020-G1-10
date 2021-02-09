package org.springframework.samples.farmatic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.farmatic.model.TipoMedicamento;

public interface TipoMedicamentoRepository extends CrudRepository<TipoMedicamento, Integer>{
	
	Collection<TipoMedicamento> findAll() throws DataAccessException;
	
	@Query("SELECT tipo.tipo FROM TipoMedicamento tipo")
	Collection<TipoMedicamento> findAllTypes() throws DataAccessException;
}

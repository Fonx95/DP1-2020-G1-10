package org.springframework.samples.farmatic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.farmatic.model.Pedido;
import org.springframework.samples.farmatic.model.Producto;

public interface PedidoRepository extends Repository<Pedido, String>{
	
	void save(Pedido pedido) throws DataAccessException;
	
	Collection<Pedido> findAll() throws DataAccessException;
	
	@Query("SELECT pedido FROM Pedido pedido WHERE pedido.id =:id")
	public Pedido findById(@Param("id") int id);
}

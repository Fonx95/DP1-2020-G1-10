package org.springframework.samples.farmatic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.farmatic.model.Pedido;
import org.springframework.samples.farmatic.model.Pedido.EstadoPedido;


public interface PedidoRepository extends CrudRepository<Pedido, String>{
	
	//void save(Pedido pedido) throws DataAccessException;
	
	Collection<Pedido> findAll() throws DataAccessException;
	
	@Query("SELECT pedido FROM Pedido pedido WHERE pedido.estadoPedido = 2")
	Pedido pedidoActual() throws DataAccessException;
	
//	@Query("SELECT pedido,proveedor FROM Pedido pedido INNER JOIN Proveedor proveedor ON pedido.proveedor = proveedor.id WHERE pedido.id = :id")
//	Pedido pedido(@Param("id") int id) throws DataAccessException;
	
	@Query("SELECT pedido FROM Pedido pedido WHERE pedido.id = :id")
	Pedido pedido(@Param("id") int id) throws DataAccessException;

}

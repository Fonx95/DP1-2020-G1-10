
package org.springframework.samples.farmatic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.farmatic.model.LineaPedido;

public interface LineaPedidoRepository extends CrudRepository<LineaPedido, Integer> {

	@Query("SELECT linea FROM LineaPedido linea WHERE linea.pedido = :pedido_id")
	Collection<LineaPedido> lineaPedido(@Param("pedido_id") int id) throws DataAccessException;

}

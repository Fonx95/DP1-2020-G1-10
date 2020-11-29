package org.springframework.samples.farmatic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.farmatic.model.Pedido;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.repository.PedidoRepository;
import org.springframework.samples.farmatic.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoService {
	
	private PedidoRepository pedidoRepository;
	
	@Autowired
	public PedidoService(PedidoRepository pedidoRepository) {
		this.pedidoRepository = pedidoRepository;
	}
	
	@Transactional(readOnly = true)
	public Collection<Pedido> findPedidos() throws DataAccessException{
		//listado pedidos
		return pedidoRepository.findAll();
	}
	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	public Pedido findPedidoById(int id) throws DataAccessException {
		//detalles pedidos
		return pedidoRepository.findById(id);
	}
	@Transactional
	public void savePedido(Pedido pedido) throws DataAccessException{
		//creando Pedido
		pedidoRepository.save(pedido);
	}

}

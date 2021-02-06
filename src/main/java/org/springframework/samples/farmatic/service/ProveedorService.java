package org.springframework.samples.farmatic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.farmatic.model.Pedido;
import org.springframework.samples.farmatic.model.Proveedor;
import org.springframework.samples.farmatic.model.User;
import org.springframework.samples.farmatic.repository.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProveedorService {
	
	private ProveedorRepository proveedorRepository;
	
	@Autowired
	public ProveedorService(ProveedorRepository proveedorRepository) {
		this.proveedorRepository = proveedorRepository;
	}
	
	@Transactional
	public Proveedor proveedor(final int id) {
		//busqueda de un proveedor por su id
		Proveedor p = this.proveedorRepository.findById(id);
		log.debug("El proveedor tiene el id '" + p.getId() + "', nombre " + p.getEmpresa() + " y cif '" + p.getCif());
		return p;
	}
	
	@Transactional
	public Proveedor findProveedorUser(User user) {
		//busqueda de un proveedor por su user
		return this.proveedorRepository.findByUser(user);
	}

	@Transactional(readOnly = true)
	public Collection<Proveedor> findProveedores() {
		//busqueda de todos los proveedores
		return (Collection<Proveedor>) this.proveedorRepository.findAll();
	}

	@Transactional()
	public Collection<Pedido> findPedidosProveedor(User user) throws DataAccessException {
		//busqueda de todos los pedidos de un proveedor
		Proveedor p = this.proveedorRepository.findByUser(user);
		log.debug("El proveedor '" + user.getProveedor().getEmpresa() + "' ha listado " + p.getPedido().size() + " pedidos");
		return p.getPedido();
	}
	
}

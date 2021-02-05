package org.springframework.samples.farmatic.service;

import java.util.Collection;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.farmatic.model.Cliente;
import org.springframework.samples.farmatic.model.User;
import org.springframework.samples.farmatic.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClienteService {
	
	private ClienteRepository clienteRepository;
	
	@Autowired
	public ClienteService(final ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}	

	@Transactional
	public Collection<Cliente> findClientes() throws DataAccessException {
		//busqueda de todos los clientes
		return this.clienteRepository.findAll();
	}

	@Transactional
	public Cliente findClienteById(final int id) throws DataAccessException {
		//busqueda de un cliente por su id
		return this.clienteRepository.findById(id);
	}
	
	@Transactional(rollbackFor = EntityNotFoundException.class)
	public Cliente clienteDni(String dni) throws DataAccessException, EntityNotFoundException {
		//busqueda de un cliente por su dni
		Cliente cliente = this.clienteRepository.fingByDni(dni);
		if(cliente == null) {
			log.warn("El dni introducido no existe en la BD o es invalido");
			throw new EntityNotFoundException();
		}
		return cliente;
	}
	
	@Transactional
	public Cliente findClienteUser(User user) throws DataAccessException {
		//busqueda de un clinete por su user
		return this.clienteRepository.findByUser(user);
	}
	
	@Transactional
	public void saveCliente(final Cliente cliente) throws DataAccessException {
		//crea o modifica un cliente
		this.clienteRepository.save(cliente);
		log.debug("El cliente " + cliente.getName() + " " + cliente.getSurnames() + " (" + cliente.getDni() + ") se ha creado o modificado");
	}
}

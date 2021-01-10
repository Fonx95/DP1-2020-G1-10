package org.springframework.samples.farmatic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.farmatic.model.Authorities;
import org.springframework.samples.farmatic.model.Cliente;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.model.User;
import org.springframework.samples.farmatic.repository.ClienteRepository;
import org.springframework.samples.farmatic.repository.ProductoRepository;
import org.springframework.samples.farmatic.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {
	
	
	private ClienteRepository clienteRepo;
	
	private UserRepository			userRepository;
	@Transactional
	public int clienteCount() {
		return (int) clienteRepo.count();
	}
	

	@Transactional
	public Iterable<Cliente> findClientes() throws DataAccessException {
		//lista productos

		return this.clienteRepo.findAll();

	}

	@Transactional
	public Cliente findClienteById(final int id) throws DataAccessException {
	
		return this.clienteRepo.findById(id);
	}

	@Autowired
	public ClienteService(final ClienteRepository clienteRepo, final UserRepository userRepository) {
		this.clienteRepo = clienteRepo;
		this.userRepository = userRepository;
	}
	
	@Transactional
	public Cliente findClienteData() throws DataAccessException {
		//detalles cliente
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();             //Obtiene el nombre del ususario actual
		User user = this.userRepository.findByUsername(currentPrincipalName);         //Obtiene el usuario con ese nombre
		return this.clienteRepo.findByUser(user);
	}
}

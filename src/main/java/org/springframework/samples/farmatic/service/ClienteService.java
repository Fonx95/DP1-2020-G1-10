package org.springframework.samples.farmatic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.farmatic.model.Cliente;
import org.springframework.samples.farmatic.model.User;
import org.springframework.samples.farmatic.repository.ClienteRepository;
import org.springframework.samples.farmatic.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {
	
	private ClienteRepository clienteRepository;
	
	private UserRepository userRepository;
	
	private UserService	userService;
	
	private final AuthoritiesService authoritiesService;
	
	@Autowired
	public ClienteService(final ClienteRepository clienteRepo, final UserService userService, final AuthoritiesService	authoritiesService,final UserRepository userRepository) {
		this.clienteRepository = clienteRepo;
		this.userService = userService;
		this.authoritiesService = authoritiesService;
		this.userRepository=userRepository;
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
	
	@Transactional
	public Cliente clienteDni(String dni) throws DataAccessException {
		//busqueda de un cliente por su dni
		return this.clienteRepository.fingByDni(dni);
	}
	
	@Transactional
	public void saveCliente(final Cliente cliente) throws DataAccessException {
		//creating cliente
		cliente.setPorPagarTotal(0.0);
		this.clienteRepository.save(cliente);
		this.userService.saveUser(cliente.getUser());
		this.authoritiesService.saveAuthorities(cliente.getUser().getUsername(), "cliente");
	}
	
	@Transactional
	public Cliente findClienteData() throws DataAccessException {
		//detalles cliente
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();             //Obtiene el nombre del ususario actual
		User user = this.userService.findUser(currentPrincipalName).get();         //Obtiene el usuario con ese nombre
		return this.clienteRepository.findByUser(user);
	}
}

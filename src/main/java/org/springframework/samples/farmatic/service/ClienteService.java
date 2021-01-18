package org.springframework.samples.farmatic.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.farmatic.model.Authorities;
import org.springframework.samples.farmatic.model.Cliente;
import org.springframework.samples.farmatic.model.Pedido;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.model.Proveedor;
import org.springframework.samples.farmatic.model.User;
import org.springframework.samples.farmatic.repository.AuthoritiesRepository;
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
	
	private UserService			userService;
	
	@Autowired
	public ClienteService(final ClienteRepository clienteRepo, final UserService userService, final AuthoritiesService	authoritiesService,final UserRepository userRepository) {
		this.clienteRepo = clienteRepo;
		this.userService = userService;
		this.authoritiesService = authoritiesService;
		this.userRepository=userRepository;
	}
	
	private final AuthoritiesService	authoritiesService;
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
	
	@Transactional
	public void saveCliente(final Cliente cliente) throws DataAccessException {
		//creating cliente
		cliente.setPorPagarTotal(0.0);
		this.clienteRepo.save(cliente);
		this.userService.saveUser(cliente.getUser());
		this.authoritiesService.saveAuthorities(cliente.getUser().getUsername(), "cliente");
		
		
	}
	@Transactional
	public User getCurrentUser() throws DataAccessException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();             //Obtiene el nombre del ususario actual
		return this.userRepository.findByUsername(currentPrincipalName);         //Obtiene el usuario con ese nombre
	}

	@Transactional(readOnly = true)
	public Cliente findClienteDetalles(User user) throws DataAccessException {

		Cliente c = this.clienteRepo.findByUser(user);
		return c;
	}

	
	@Transactional
	public Cliente findClienteData() throws DataAccessException {
		//detalles cliente
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();             //Obtiene el nombre del ususario actual
		User user = this.userService.findUser(currentPrincipalName).get();         //Obtiene el usuario con ese nombre
		return this.clienteRepo.findByUser(user);
	}
}

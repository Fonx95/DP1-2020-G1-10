package org.springframework.samples.farmatic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.farmatic.repository.ClienteRepository;
import org.springframework.transaction.annotation.Transactional;

public class ClienteService {
	@Autowired
	private ClienteRepository clienteRepo;
	@Transactional
	public int proveedorCount() {
		return (int) clienteRepo.count();
	}
}

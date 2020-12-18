package org.springframework.samples.farmatic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.farmatic.model.Proveedor;
import org.springframework.samples.farmatic.repository.ProveedorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProveedorService {
	
	@Autowired
	private ProveedorRepository proveedorRepo;
	
	@Transactional
	public int proveedorCount() {
		return (int) proveedorRepo.count();
	}

}

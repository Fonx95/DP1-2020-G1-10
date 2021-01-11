package org.springframework.samples.farmatic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.farmatic.model.Comprador;
import org.springframework.samples.farmatic.repository.CompradorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompradorService {
	
	@Autowired
	private CompradorRepository compradorRepo;
	
	@Transactional
	public int compradorCount() {
		return (int) compradorRepo.count();
	}

}

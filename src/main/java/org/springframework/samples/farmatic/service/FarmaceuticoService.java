package org.springframework.samples.farmatic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.farmatic.model.Farmaceutico;
import org.springframework.samples.farmatic.model.User;
import org.springframework.samples.farmatic.repository.FarmaceuticoRepository;
import org.springframework.stereotype.Service;

@Service
public class FarmaceuticoService {
	
	private FarmaceuticoRepository farmaceuticoRepository;
	
	@Autowired
	public FarmaceuticoService(FarmaceuticoRepository farmaceuticoRepository) {
		this.farmaceuticoRepository = farmaceuticoRepository;
	}
	
	public Farmaceutico findFarmaceuticoByUser (User user) {
		//busqueda de un farmaceutico por su user
		return this.farmaceuticoRepository.findByUser(user);
	}
}

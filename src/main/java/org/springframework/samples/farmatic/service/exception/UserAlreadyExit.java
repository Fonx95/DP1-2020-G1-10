package org.springframework.samples.farmatic.service.exception;

import org.springframework.samples.farmatic.model.User;

public class UserAlreadyExit extends Exception {
	
	public UserAlreadyExit(User user) {
		super(user.getUsername() + " ya existe como usuario");
	}
	
}

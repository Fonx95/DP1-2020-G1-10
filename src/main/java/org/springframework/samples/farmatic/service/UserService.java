/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.farmatic.service;


import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.farmatic.model.User;
import org.springframework.samples.farmatic.repository.UserRepository;
import org.springframework.samples.farmatic.service.exception.MatchPasswordException;
import org.springframework.samples.farmatic.service.exception.UserAlreadyExit;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Slf4j
@Service
public class UserService {

	private UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional(rollbackFor = {UserAlreadyExit.class, MatchPasswordException.class})
	public void createUser(User user) throws DataAccessException, UserAlreadyExit, MatchPasswordException {
		Collection<User> users = this.userRepository.findAll();
		for(User user1 : users) {
			if(user1.getUsername().equals(user.getUsername())) {
				throw new UserAlreadyExit(user);
			}
		}
		validatePassword(user.getPassword());
		user.setEnabled(true);
		userRepository.save(user);
	}
	
	@Transactional
	public void updateUser (User user) throws DataAccessException {
		userRepository.save(user);
	}
	
	public Optional<User> findUser(String username) {
		return userRepository.findById(username);
	}
	
	@Transactional
	public User getCurrentUser() throws DataAccessException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();             //Obtiene el nombre del ususario actual
		log.debug("El usuario logueado es '" + currentPrincipalName + "'");
		return this.userRepository.findByUsername(currentPrincipalName);         //Obtiene el usuario con ese nombre
	}
	
	private void validatePassword(String password) throws MatchPasswordException {
		String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)\\S{5,16}$";
		if(password.isEmpty() || password == null) {
			throw new MatchPasswordException();
		}else if(!password.matches(regex)){
			throw new MatchPasswordException();
		}
	}
}

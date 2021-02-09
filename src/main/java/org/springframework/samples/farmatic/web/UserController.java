/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.farmatic.web;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.farmatic.model.Authorities;
import org.springframework.samples.farmatic.model.Cliente;
import org.springframework.samples.farmatic.model.Farmaceutico;
import org.springframework.samples.farmatic.model.Proveedor;
import org.springframework.samples.farmatic.model.User;
import org.springframework.samples.farmatic.model.UserValidate;
import org.springframework.samples.farmatic.model.validator.UserValidator;
import org.springframework.samples.farmatic.service.AuthoritiesService;
import org.springframework.samples.farmatic.service.ClienteService;
import org.springframework.samples.farmatic.service.FarmaceuticoService;
import org.springframework.samples.farmatic.service.ProveedorService;
import org.springframework.samples.farmatic.service.UserService;
import org.springframework.samples.farmatic.service.exception.MatchPasswordException;
import org.springframework.samples.farmatic.service.exception.UserAlreadyExit;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Slf4j
@Controller
public class UserController {

	private UserService userService;
	
	private AuthoritiesService authoritiesService;
	
	private ClienteService clienteService;
	
	private FarmaceuticoService farmaceuticoService;
	
	private ProveedorService proveedorService;
	
	@Autowired
	public UserController (UserService userService, AuthoritiesService authoritiesService, 
			ClienteService clienteService, FarmaceuticoService farmaceuticoService, ProveedorService proveedorService) {
		this.userService = userService;
		this.authoritiesService = authoritiesService;
		this.clienteService = clienteService;
		this.farmaceuticoService = farmaceuticoService;
		this.proveedorService = proveedorService;
	}
	
	@InitBinder("user")
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setValidator(new UserValidator(this.userService.getCurrentUser()));
	}
	
	@GetMapping("users")
	private String showUserDetails(ModelMap model) {
		User user = this.userService.getCurrentUser();
		Authorities authority = this.authoritiesService.findAuthoritiyByUser(user);
		
		if(authority.getAuthority().equals("cliente")) {
			Cliente cliente = this.clienteService.findClienteUser(user);
			model.addAttribute("cliente", cliente);
		}else if(authority.getAuthority().equals("proveedor")) {
			Proveedor proveedor = this.proveedorService.findProveedorUser(user);
			model.addAttribute("proveedor", proveedor);
		}else if(authority.getAuthority().equals("farmaceutico")) {
			Farmaceutico farmaceutico = this.farmaceuticoService.findFarmaceuticoByUser(user);
			model.addAttribute("farmaceutico", farmaceutico);
		}

		log.info("El usuario '" + user.getUsername() + "' ha mostrado su informacion personal");
		return "users/userDetails";
	}
	
	@GetMapping("/users/new")
	public String newUser(ModelMap model) {
		Cliente cliente = new Cliente();
		model.addAttribute("cliente", cliente);
		return "users/userRegister";
	}
	
	@PostMapping("/users/new")
	public String creationUser(@ModelAttribute("cliente") Cliente cliente, final BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "users/userRegister";
		} else if(cliente.getUser() == null) {
			try {
				cliente = this.clienteService.clienteDni(cliente.getDni());
			}catch(EntityNotFoundException ex) {
				result.rejectValue("dni", "clienteNotFound");
				return "users/userRegister";
			}
			if(cliente.getUser() != null) {
				result.rejectValue("dni", "clienteAlreadyExit", "Este cliente ya tiene cuenta de usuario");
				return "users/userRegister";
			}else {
				cliente.setUser(new User());
				model.addAttribute("cliente", cliente);
				return "users/userRegister";
			}
		}else {
			try {
				this.userService.createUser(cliente.getUser());
				this.authoritiesService.saveAuthorities(cliente.getUser().getUsername(), "cliente");
				this.clienteService.saveCliente(cliente);
				log.info("El cliente con dni '" + cliente.getDni() + "' se ha registrado como usuario");
				return "redirect:../";
			}catch(UserAlreadyExit ex) {
				result.rejectValue("user.username", "UserFault", ex.getMessage());
				model.addAttribute("cliente", cliente);
				log.warn("Se ha introducido un username ya existente");
				return "users/userRegister";
			}catch(MatchPasswordException ex) {
				result.rejectValue("user.password", "required");
				model.addAttribute("cliente", cliente);
				log.warn("Se ha introducido una contraseña incorrecta");
				return "users/userRegister";
			}
		}
	}
	
	@GetMapping("/users/password")
	public String initChangePassword(ModelMap model) {
		User currentUser = this.userService.getCurrentUser();
		UserValidate user = new UserValidate(currentUser.getUsername(), "");
		model.addAttribute("user", user);
		return "users/passwordEdit";
	}
	
	@PostMapping("/users/password")
	public String changePassword(@ModelAttribute("user") @Valid UserValidate user, final BindingResult result, ModelMap model) {
		if(result.hasErrors()) {
			log.warn("El usuario '" + user.getUsername() + "' ha tenido " + result.getFieldErrorCount() + " errores al intentar cambiar la contraseña");
			return "users/passwordEdit";
		}else {
			User CurrentUser = this.userService.getCurrentUser();
			CurrentUser.setPassword(user.getNewPassword());
			this.userService.updateUser(CurrentUser);
			log.info("El usuario '" + CurrentUser.getUsername() + "' ha cambiado satisfactoriamente su contraseña");
			return "redirect:../";
		}
	}
}

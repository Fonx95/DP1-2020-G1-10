package org.springframework.samples.farmatic.model.validator;

import org.springframework.samples.farmatic.model.User;
import org.springframework.samples.farmatic.model.UserValidate;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserValidator implements Validator {
	
	private User currentUser;
	
	public UserValidator(User currentUser) {
		this.currentUser = currentUser;
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return UserValidate.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserValidate user = (UserValidate) target;
		
		//username validator
		if(!user.getUsername().equals(this.currentUser.getUsername())) {
			errors.rejectValue("username", "usernameFault", "No puedes cambiar el nombre de ususario");
		}
		
		//password validator
		if(user.getPassword() == null || user.getPassword() == "") {
			errors.rejectValue("password", "required");
		}else if(!user.getPassword().equals(this.currentUser.getPassword())) {
			errors.rejectValue("password", "PassException", "Contraseña incorrecta");
		}
		
		//newPassword validator
		if(user.getNewPassword() == null || user.getNewPassword() == "") {
			errors.rejectValue("newPassword", "required");
		}
		
		//validPassword validator
		if(user.getValidPassword() == null || user.getValidPassword() == "") {
			errors.rejectValue("validPassword", "required");
		}else if(!user.getNewPassword().equals(user.getValidPassword())) {
			errors.rejectValue("validPassword", "PassException", "Las contraseñas no coinciden");
		}
	}
	
}

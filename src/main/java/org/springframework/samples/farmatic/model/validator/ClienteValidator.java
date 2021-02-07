package org.springframework.samples.farmatic.model.validator;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.springframework.samples.farmatic.model.Cliente;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ClienteValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Cliente.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Cliente cliente = (Cliente) target;
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		javax.validation.Validator validator = factory.getValidator();
		Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);
		
		for(ConstraintViolation<Cliente> error : violations) {
			errors.rejectValue(error.getPropertyPath().toString(), "clienteFault", error.getMessage());
		}
	}

}

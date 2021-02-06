package org.springframework.samples.farmatic.model.validator;

import org.springframework.samples.farmatic.model.LineaPedido;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class LineaPedidoValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return LineaPedido.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		LineaPedido linea = (LineaPedido) target;
		
		if(linea.getProducto() != null) {
			
		}
	}

}

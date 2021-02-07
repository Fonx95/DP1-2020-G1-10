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
			
			//cantidad validator
			if(linea.getCantidad() == null) {
				errors.rejectValue("cantidad", "CantidadBlank");
			}else if(linea.getCantidad() < 0) {
				errors.rejectValue("cantidad", "CantidadNegativa");
			}
		}
	}

}

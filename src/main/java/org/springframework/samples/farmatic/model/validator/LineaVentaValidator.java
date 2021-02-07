package org.springframework.samples.farmatic.model.validator;

import org.springframework.samples.farmatic.model.LineaVenta;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class LineaVentaValidator implements Validator {

	

	@Override
	public void validate(Object target, Errors errors) {
		LineaVenta linea = (LineaVenta) target;
		if(linea.getProducto() != null) {
		
			//cantidad validator
			if(linea.getCantidad() == null) {
				errors.rejectValue("cantidad", "CantidadBlank");
			}else if(linea.getCantidad() < 0) {
				errors.rejectValue("cantidad", "CantidadNegativa");
			}else if (linea.getProducto().getStock() - linea.getCantidad() < 0) {
				errors.rejectValue("cantidad", "StockInsuficiente");
			}
		}
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return LineaVenta.class.isAssignableFrom(clazz);
	}
}

package org.springframework.samples.farmatic.model.validator;

import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.model.TipoProducto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ProductoValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Producto.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Producto producto = (Producto) target;
		
		//nombre validator
		if(producto.getName() == null || producto.getName().isEmpty()) {
			errors.rejectValue("name", "required");
		}
		
		//pvf validator
		if(producto.getPvf() == null) {
			errors.rejectValue("pvf", "required");
		}else if(producto.getPvf() < 0) {
			errors.rejectValue("pvf", "pvfValue", "no puede ser negativo");
		}
		
		//pvp validator
		if(producto.getPvp() == null) {
			errors.rejectValue("pvp", "required");
		}else if(producto.getPvp() < 0) {
			errors.rejectValue("pvp", "pvpValue", "no puede ser negativo");
		}else if(producto.getPvf() != null && producto.getPvp() < producto.getPvf()) {
			errors.rejectValue("pvp", "pvpValue", "no puede ser menor que el PvF");
		}
		
		//stock validator
		if(producto.getStock() == null) {
			errors.rejectValue("stock", "required");
		}else if(producto.getStock() < 0) {
			errors.rejectValue("stock", "stockValue", "no puede ser negativo");
		}
		
		//stock validator
		if(producto.getMinStock() == null) {
			errors.rejectValue("minStock", "required");
		}else if(producto.getMinStock() < 0) {
			errors.rejectValue("minStock", "minStockValue", "no puede ser negativo");
		}
		
		//tipo producto validator
		if(producto.getProductType() == null) {
			errors.rejectValue("productType", "required");
		}else if(producto.getProductType() == TipoProducto.PARAFARMACIA && producto.getTipoMedicamento() != null) {
			errors.rejectValue("productType", "productTypeValue", "Un tipo parafarmacia no puede tener tipo Medicamento");
		}
	}

}

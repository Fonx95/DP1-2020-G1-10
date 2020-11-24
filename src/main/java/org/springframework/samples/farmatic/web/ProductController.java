package org.springframework.samples.farmatic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.model.Productos;
import org.springframework.samples.farmatic.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProductController {
	
	private static final String VIEWS_PRODUCT_CREATE_OR_UPDATE_FORM = "products/createOrUpdateProductForm";

	private final ProductoService productService;

	@Autowired
	public ProductController(ProductoService productService) {
		this.productService = productService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@GetMapping(value = {"/products"})
	public String showProductList(Map<String, Object> model) {
		Productos productos = new Productos();
		productos.getProductoLista().addAll(this.productService.findProducts());
		model.put("productos", productos);
		return "products/listaProductos";
	}
	
	@GetMapping("/products/{idProducto}")
	public ModelAndView showProducts(@PathVariable("idProducto") int idProducto) {
		ModelAndView mav = new ModelAndView("Productos/detallesProducto");
		Producto product = this.productService.findProductoById(idProducto);
		mav.addObject(product);
		return mav;
		
	}
	
	@GetMapping(value = "/products/new")
	public String initCreationForm(Map<String, Object> model) {
		Producto product = new Producto();
		model.put("product", product);
		return VIEWS_PRODUCT_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping(value = "/products/new")
	public String processCreationForm(@Valid Producto product, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_PRODUCT_CREATE_OR_UPDATE_FORM;
		}
		else {
			//creating owner, user and authorities
			this.productService.saveProducto(product);
			
			return "redirect:/products/" + product.getId();
		}
	}

}

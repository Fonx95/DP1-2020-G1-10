
package org.springframework.samples.farmatic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProductController {

	private static final String		VIEWS_PRODUCT_CREATE_OR_UPDATE_FORM	= "products/createOrUpdateProductForm";

	private final ProductoService	productService;


	@Autowired
	public ProductController(final ProductoService productService) {
		this.productService = productService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = {
		"/products"
	})
	public String listadoProductos(final ModelMap modelMap) {
		Iterable<Producto> productos = this.productService.findProducts();
		modelMap.addAttribute("productos", productos);
		return "products/productList";
	}

	@GetMapping("/products/productList/{idProducto}")
	public ModelAndView showProducts(@PathVariable("idProducto") final int idProducto) {
		ModelAndView mav = new ModelAndView("products/productDetails");
		Producto product = this.productService.findProductoById(idProducto);
		mav.addObject(product);
		return mav;

	}

	@GetMapping(value = "/products/new")
	public String initCreationForm(final Map<String, Object> model) {
		Producto product = new Producto();
		model.put("product", product);
		return ProductController.VIEWS_PRODUCT_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/products/new")
	public String processCreationForm(@Valid final Producto product, final BindingResult result) {
		if (result.hasErrors()) {
			return ProductController.VIEWS_PRODUCT_CREATE_OR_UPDATE_FORM;
		} else {
			//creating owner, user and authorities
			this.productService.saveProducto(product);

			return "redirect:/products/" + product.getId();
		}
	}

}


package org.springframework.samples.farmatic.web;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.farmatic.model.Farmaceutico;
import org.springframework.samples.farmatic.model.LineaPedido;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.model.TipoProducto;
import org.springframework.samples.farmatic.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
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
	
	@ModelAttribute("types")
	public Collection<TipoProducto> populatePetTypes() {
		return this.productService.getProductTypes();
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

			return "redirect:/products/productList/" + product.getId();
		}
	}

	@GetMapping(value = {"/products/productList/{idProducto}/edit"})
	public String showProductoEdit(@PathVariable("idProducto") int productId, final ModelMap model) {
		//Producto product = producto;
		
		//model.put("product", producto);
		Producto product = this.productService.findProductoById(productId);
		model.put("product", product);
		return ProductController.VIEWS_PRODUCT_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = {"/products/productList/{idProducto}/edit"})
	public String ProductoEdit(@ModelAttribute("idProducto") final Producto producto, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			return "/products/productList/";
		} else if (producto.getCode() != null) {
			return this.processCreationForm(producto, result);
		} else {
			this.productService.saveProducto(producto);;
			return "redirect:/products/productList/" + producto.getId();
		}
	}

	
}

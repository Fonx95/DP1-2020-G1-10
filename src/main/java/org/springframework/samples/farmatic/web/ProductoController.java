
package org.springframework.samples.farmatic.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.model.TipoMedicamento;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ProductoController {

	private static final String		VIEWS_PRODUCT_CREATE_OR_UPDATE_FORM	= "productos/createOrUpdateProductoForm";

	private final ProductoService	productoService;


	@Autowired
	public ProductoController(final ProductoService productService) {
		this.productoService = productService;
	}
	
	@ModelAttribute("tipoMedicamento")
	public Collection<TipoMedicamento> populateMedicamentoTypes() {
		return this.productoService.getMedicamentoTypes();
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = {"/productos"})
	public String listadoProductos(final ModelMap modelMap) {
		Iterable<Producto> productos = this.productoService.findProducts();
		modelMap.addAttribute("producto", new Producto());
		modelMap.addAttribute("productos", productos);
		log.info("Se han mostrado todos los productos");
		return "productos/productoList";
	}
	
	@PostMapping(value = {"/productos","/productos/tipo/{idTipo}"})
	public String searchProducto(@ModelAttribute("producto") Producto producto, final BindingResult result, final ModelMap model) {
		if(result.hasErrors()) {
			return "productos/productoList";
		}else if(producto.getCode() != null && producto.getCode() != "") {
			Collection<Producto> productos = new ArrayList<Producto>();
			producto = this.productoService.findProductoByCode(producto.getCode().toUpperCase());
			productos.add(producto);
			if (producto.isNew()) {
				model.addAttribute("vacio", true); 
				productos.clear();
				return "productos/productoList";
			}
			log.info("Se ha buscado el producto por el codigo '" + producto.getCode() + "'");
			model.addAttribute("productos", productos);
			return "productos/productoList";
		}else if(producto.getName() != null && producto.getName() != "") {
			Collection<Producto> productos = this.productoService.productoPorNombre(producto.getName().toUpperCase());
			if (productos.isEmpty()) model.addAttribute("vacio", true);
			model.addAttribute("productos", productos);
			log.info("Se ha buscado el producto por el nombre '" + producto.getName() + "' y se han encontrado " + productos.size() + " coincidencias");
			return "productos/productoList";
		}else {
			return "redirect:/productos";
		}
	}

	@GetMapping(value = {"/productos/{idProducto}"})
	public ModelAndView showProductos(@PathVariable("idProducto") final int idProducto) {
		ModelAndView mav = new ModelAndView("productos/productoDetails");
		Producto producto = this.productoService.findProductoById(idProducto);
		mav.addObject(producto);
		log.info("Se ha mostrado los detalles del producto con el codigo '" + producto.getCode() + "'");
		return mav;
	}

	@GetMapping(value = {"/productos/new"})
	public String initCreationForm(final Map<String, Object> model) {
		Producto producto = new Producto();
		List<TipoProducto> tipo = Arrays.asList(TipoProducto.values());
		model.put("tipoProducto", tipo);
		model.put("producto", producto);
		return ProductoController.VIEWS_PRODUCT_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = {"/productos/new"})
	public String processCreationForm(@Valid final Producto producto, final BindingResult result) {
		if (result.hasErrors()) {
			return ProductoController.VIEWS_PRODUCT_CREATE_OR_UPDATE_FORM;
		} else {
			this.productoService.saveProducto(producto);
			log.info("Se ha creado un nuevo producto");
			return "redirect:/productos/" + producto.getId();
		}
	}

	@GetMapping(value = {"/productos/{idProducto}/edit"})
	public String showProductoEdit(@PathVariable("idProducto") int productoId, final ModelMap model) {
		Producto producto = this.productoService.findProductoById(productoId);
		List<TipoProducto> tipo = Arrays.asList(TipoProducto.values());
		model.put("tipoProducto", tipo);
		model.put("producto", producto);
		return ProductoController.VIEWS_PRODUCT_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = {"/productos/{idProducto}/edit"})
	public String ProductoEdit(@Valid final Producto producto, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			return "/productos/productoList/";
		}else {
			this.productoService.saveProducto(producto);
			log.info("Se ha modificado el producto de codigo '" + producto.getCode() + "'");
			return "redirect:/productos/" + producto.getId();
		}
	}
	
	@GetMapping(value = {"/productos/tipo/{idTipo}"})
	public String showProductoTipo(@PathVariable("idTipo") TipoMedicamento tipo, final ModelMap model) {
		Collection<Producto> productos = this.productoService.findProductosByTipo(tipo);
		model.addAttribute("productos", productos);
		log.info("Se han mostrado " + productos.size() + " productos del tipo " + tipo.getTipo());
		return "productos/productoList";
	}
	
}


package org.springframework.samples.farmatic.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.model.TipoMedicamento;
import org.springframework.samples.farmatic.model.TipoProducto;
import org.springframework.samples.farmatic.model.validator.ProductoValidator;
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

	@InitBinder("producto")
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setValidator(new ProductoValidator());
	}

	@GetMapping(value = {
		"/productos"
	})
	public String listadoProductos(final ModelMap modelMap) {
		Iterable<Producto> productos = this.productoService.findProducts();
		modelMap.addAttribute("producto", new Producto());
		modelMap.addAttribute("productos", productos);
		ProductoController.log.info("Se han mostrado todos los productos");
		return "productos/productoList";
	}

	@PostMapping(value = {
		"/productos", "/productos/tipo/{idTipo}"
	})
	public String searchProducto(@ModelAttribute("producto") Producto producto, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			return "productos/productoList";
		} else if (producto.getCode() != null && producto.getCode() != "") {
			try {
				Collection<Producto> productos = new ArrayList<Producto>();
				producto = this.productoService.findProductoByCode(producto.getCode().toUpperCase());
				productos.add(producto);
				ProductoController.log.info("Se ha buscado el producto por el codigo '" + producto.getCode() + "'");
				model.addAttribute("productos", productos);
				return "productos/productoList";
			} catch (EntityNotFoundException ex) {
				result.rejectValue("code", "productNotFound", "El producto no existe");
				return "productos/productoList";
			}

		} else if (producto.getName() != null && producto.getName() != "") {
			try {
				Collection<Producto> productos = this.productoService.productoPorNombre(producto.getName().toUpperCase());
				model.addAttribute("productos", productos);
				ProductoController.log.info("Se ha buscado el producto por el nombre '" + producto.getName() + "' y se han encontrado " + productos.size() + " coincidencias");
				return "productos/productoList";
			} catch (EntityNotFoundException ex) {
				result.rejectValue("name", "productNotFound", "No existe ningun producto");
				return "productos/productoList";
			}
		} else {
			return "redirect:/productos";
		}
	}

	@GetMapping(value = {
		"/productos/{idProducto}"
	})
	public ModelAndView showProductos(@PathVariable("idProducto") final int idProducto) {
		ModelAndView mav = new ModelAndView("productos/productoDetails");
		Producto producto = this.productoService.findProductoById(idProducto);
		mav.addObject(producto);
		ProductoController.log.info("Se ha mostrado los detalles del producto con el codigo '" + producto.getCode() + "'");
		return mav;
	}

	@GetMapping(value = {
		"/productos/new"
	})
	public String initCreationForm(final ModelMap model) {
		Producto producto = new Producto();
		List<TipoProducto> tipo = Arrays.asList(TipoProducto.values());
		model.put("tipoProducto", tipo);
		model.put("producto", producto);
		return ProductoController.VIEWS_PRODUCT_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = {
		"/productos/new"
	})
	public String processCreationForm(@Valid final Producto producto, final BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			List<TipoProducto> tipo = Arrays.asList(TipoProducto.values());
			model.put("tipoProducto", tipo);
			model.put("tipoMedicamento", this.populateMedicamentoTypes());
			model.put("producto", producto);
			return ProductoController.VIEWS_PRODUCT_CREATE_OR_UPDATE_FORM;
		} else {
			this.productoService.saveProducto(producto);
			ProductoController.log.info("Se ha creado un nuevo producto");
			return "redirect:/productos/" + producto.getId();
		}
	}

	@GetMapping(value = {
		"/productos/{idProducto}/edit"
	})
	public String showProductoEdit(@PathVariable("idProducto") final int productoId, final ModelMap model) {
		Producto producto = this.productoService.findProductoById(productoId);
		List<TipoProducto> tipo = Arrays.asList(TipoProducto.values());
		model.put("tipoProducto", tipo);
		model.put("producto", producto);
		return ProductoController.VIEWS_PRODUCT_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = {
		"/productos/{idProducto}/edit"
	})
	public String ProductoEdit(@Valid final Producto producto, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			List<TipoProducto> tipo = Arrays.asList(TipoProducto.values());
			model.put("tipoProducto", tipo);
			model.put("tipoMedicamento", this.populateMedicamentoTypes());
			return VIEWS_PRODUCT_CREATE_OR_UPDATE_FORM;
		} else {
			this.productoService.saveProducto(producto);
			ProductoController.log.info("Se ha modificado el producto de codigo '" + producto.getCode() + "'");
			return "redirect:/productos/" + producto.getId();
		}
	}

	@GetMapping(value = {
		"/productos/tipo/{idTipo}"
	})
	public String showProductoTipo(@PathVariable("idTipo") final TipoMedicamento tipo, final ModelMap model) {
		Collection<Producto> productos = this.productoService.findProductosByTipo(tipo);
		model.addAttribute("productos", productos);
		model.addAttribute("producto", new Producto());
		ProductoController.log.info("Se han mostrado " + productos.size() + " productos del tipo " + tipo.getTipo());
		return "productos/productoList";
	}

}

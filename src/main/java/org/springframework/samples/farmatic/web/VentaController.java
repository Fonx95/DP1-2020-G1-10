package org.springframework.samples.farmatic.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.farmatic.model.LineaPedido;
import org.springframework.samples.farmatic.model.LineaVenta;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.service.ProductoService;
import org.springframework.samples.farmatic.service.VentaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class VentaController {
	
	private final VentaService ventaService;
	
	private final ProductoService productoService;
	
	@Autowired
	public VentaController(VentaService ventaService, ProductoService productoService) {
		this.ventaService = ventaService;
		this.productoService = productoService;
	}
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@GetMapping(value= {"/ventas/actual"})
	public String showVentaActual(Map<String, Object> model) {
		Producto producto = new Producto();
		model.put("producto", producto);
		return "ventas/ventaActual";
	}
	
	@PostMapping(value= {"/ventas/actual"})
	public String ventaProcessCreation(@ModelAttribute("producto") Producto producto, @ModelAttribute("nuevaLinea") LineaVenta linea, 
			BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "/ventas/ventaActual";
		}else if(producto.getCode()!=null){
			producto = this.productoService.findProductoByCode(producto.getCode());
			linea = ventaService.newLinea(producto);
			model.addAttribute("nuevaLinea", linea);
			model.addAttribute("producto", producto);
			return "ventas/ventaActual";
		}else {
			this.ventaService.saveLinea(linea);
			model.addAttribute("producto", producto);
			return "ventas/ventaActual";
		}
	}

}

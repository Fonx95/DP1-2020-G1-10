package org.springframework.samples.farmatic.web;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.farmatic.model.Cliente;
import org.springframework.samples.farmatic.model.Comprador;
import org.springframework.samples.farmatic.model.LineaVenta;
import org.springframework.samples.farmatic.model.Pedido;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.model.TipoProducto;
import org.springframework.samples.farmatic.model.TipoTasa;
import org.springframework.samples.farmatic.model.Venta;
import org.springframework.samples.farmatic.service.ProductoService;
import org.springframework.samples.farmatic.service.VentaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@ModelAttribute("types")
	public Collection<TipoTasa> tasaTypes() {
		return this.ventaService.getTasaTypes();
	}
	
	@ModelAttribute("pedidoActual")
	public Pedido getVentaActual(){
		Pedido pedido = this.ventaService.pedidoActual();
		return pedido;
	}
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@GetMapping(value= {"/ventas/actual"})
	public String showVentaActual(ModelMap model) {
		Producto producto = new Producto();
		model.put("producto", producto);
		return "ventas/ventaActual";
	}
	
	@PostMapping(value= {"/ventas/actual"})
	public String ventaProcessCreation(@ModelAttribute("producto") Producto producto, @ModelAttribute("nuevaLinea") LineaVenta linea, 
			final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
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
	
	@GetMapping(value = {"/ventas/actual/{lineaId}"})
	public String showLineaEdit(@PathVariable("lineaId") final LineaVenta linea, final ModelMap model) {
		Producto producto = new Producto();
		model.put("producto", producto);
		model.put("editaLinea", linea);
		return "ventas/editarLinea";
	}
	
	@PostMapping(value= {"/ventas/actual/{lineaId}"})
	public String LineaEdit(@ModelAttribute("producto") Producto producto, @ModelAttribute("editarLinea") LineaVenta linea, 
			BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "/ventas/editarLinea";
		}else if(producto.getCode()!=null){
			return ventaProcessCreation(producto, linea, result, model);
		}else if(linea.getCantidad() == 0){
			this.ventaService.deleteLinea(linea);
			return "redirect:/ventas/actual";
		}else {
			this.ventaService.saveLinea(linea);
			return "redirect:/ventas/actual";
		}
	}
	
	
	@PostMapping(value={"/ventas/actual/pagar"})
	public String createVenta(@Valid Venta venta, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "/ventas/actual/pagar";
		}else {
			this.ventaService.saveVenta(venta);;
			return "redirect:/ventas";
		}
	}
	

}

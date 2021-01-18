package org.springframework.samples.farmatic.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.farmatic.model.Cliente;
import org.springframework.samples.farmatic.model.Comprador;
import org.springframework.samples.farmatic.model.LineaVenta;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.model.TipoProducto;
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
	
	@ModelAttribute("ventaActual")
	public Venta getVentaActual(){
		Venta venta = this.ventaService.ventaActual();
		return venta;
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
			if(producto.getCode() == "") return "redirect:/ventas/actual";
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
	
	@GetMapping(value={"/ventas/actual/pagar"})
	public String finalizarVenta(ModelMap model) {
		Comprador comprador = new Comprador();
		Venta venta = this.ventaService.ventaActual();
		Boolean existeEstupe = false;
		for(LineaVenta linea:venta.getLineaVenta()) {
			if(linea.getProducto().getProductType() == TipoProducto.ESTUPEFACIENTE) existeEstupe = true;
		}
		model.addAttribute("estupefaciente", existeEstupe);
		model.addAttribute("comprador", comprador);
		return "ventas/finalizarVenta";
	}
	
	@PostMapping(value={"/ventas/actual/pagar"})
	public String createVenta(@Valid Venta venta, @ModelAttribute("comprador") Comprador comprador, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "ventas/finalizarVenta";
		}else if(comprador.getDni() != null) {
			if(comprador.getDni() == "") {
				model.put("estupefaciente", true);
				return "ventas/finalizarVenta";
			}else {
				this.ventaService.saveComprador(comprador);
				model.put("estupefaciente", false);
				return "ventas/finalizarVenta";
			}
		}else if(venta.getPagado() < venta.getImporteTotal()){
			this.ventaService.updateVenta(venta);
			return "redirect:/ventas/actual/cliente";
		}else {
			this.ventaService.finalizarVenta(venta);
			return "redirect:/ventas/actual";
		}
	}
	
	@GetMapping(value= {"/ventas/actual/cliente"})
	public String debeCliente(ModelMap model) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		return "ventas/asignarCliente";
	}
	
	@PostMapping(value= {"/ventas/actual/cliente"})
	public String AsignarCliente(@ModelAttribute("cliente") Cliente cliente, ModelMap model) {
		if(cliente.getDni() != null) {
			cliente = this.ventaService.clienteDni(cliente.getDni());
			model.put("cliente", cliente);
			return "ventas/asignarCliente";
		}else {
			this.ventaService.asignarCliente(cliente);
			return "redirect:/ventas/actual";
		}
	}
}

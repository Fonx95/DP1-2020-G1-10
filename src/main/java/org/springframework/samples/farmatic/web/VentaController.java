package org.springframework.samples.farmatic.web;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.farmatic.model.Cliente;
import org.springframework.samples.farmatic.model.Comprador;
import org.springframework.samples.farmatic.model.LineaVenta;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.model.TipoProducto;
import org.springframework.samples.farmatic.model.Venta;
import org.springframework.samples.farmatic.model.Venta.EstadoVenta;
import org.springframework.samples.farmatic.service.ClienteService;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class VentaController {
	
	private final VentaService ventaService;
	
	private final ProductoService productoService;
	
	private final ClienteService clienteService;
	
	@Autowired
	public VentaController(VentaService ventaService, ProductoService productoService, ClienteService clienteService) {
		this.ventaService = ventaService;
		this.productoService = productoService;
		this.clienteService = clienteService;
	}
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@ModelAttribute("ventaActual")
	public Venta getVentaActual(){
		Venta venta = this.ventaService.ventaActual();
		return venta;
	}
	
	@GetMapping(value="/ventas")
	public String listVentas(ModelMap model) {
		Collection<Venta> ventas = this.ventaService.findAllVentas();
		model.put("ventas", ventas);
		log.info("Se han mostrado " + ventas.size() + " ventas");
		return "ventas/ventaList";
	}
	
	@GetMapping(value="/ventas/{ventaId}")
	public String detallesVentas(@PathVariable("ventaId") Venta venta, ModelMap model) {
		if(venta.getEstadoVenta() == EstadoVenta.enProceso) return "redirect:/ventas/actual";
		else {
			model.put("venta", venta);
			log.info("Se ha mostrado la venta con el id " + venta.getId() + " y " + venta.getLineaVenta().size() + " lineas");
			return "ventas/ventaDetails";
		}
	}
	
	@GetMapping(value= {"/ventas/actual"})
	public String showVentaActual(ModelMap model) {
		Producto producto = new Producto();
		model.put("producto", producto);
		log.info("Se ha mostrado la venta actual");
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
			if(producto.getCode() == "") return "redirect:/ventas/actual";
			if(this.ventaService.existelinea(producto) != null) {
				log.info("Se ha buscado el producto '" + producto.getCode() + "' - " + producto.getName());
				return "redirect:/ventas/actual/" + this.ventaService.existelinea(producto);
			}
			linea = ventaService.newLinea(producto);
			model.addAttribute("nuevaLinea", linea);
			model.addAttribute("producto", producto);
			log.info("Se ha buscado el producto '" + producto.getCode() + "' - " + producto.getName());
			return "ventas/ventaActual";
		}else {
			this.ventaService.saveLinea(linea);
			model.addAttribute("producto", producto);
			log.info("Se ha guardado la linea con el producto '" + linea.getProducto().getCode() + "' en la venta actual");
			return "ventas/ventaActual";
		}
	}
	
	@GetMapping(value = {"/ventas/actual/{lineaId}"})
	public String showLineaEdit(@PathVariable("lineaId") final LineaVenta linea, final ModelMap model) {
		Producto producto = new Producto();
		model.put("producto", producto);
		model.put("editaLinea", linea);
		log.info("Se ha mostrado la linea con id " + linea.getId() + " para ser modificada");
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
			log.info("La linea con id: '" + linea.getId() + "' se ha eliminado");
			return "redirect:/ventas/actual";
		}else {
			this.ventaService.saveLinea(linea);
			log.info("La linea con id: '" + linea.getId() + "' se ha modificado");
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
		log.info("Se procede al pago de la venta actual");
		return "ventas/finalizarVenta";
	}
	
	@PostMapping(value={"/ventas/actual/pagar"})
	public String createVenta(@Valid Venta venta, @ModelAttribute("comprador") Comprador comprador, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "ventas/finalizarVenta";
		}else if(comprador.getDni() != null) {
			if(comprador.getDni() == "") {
				model.put("estupefaciente", true);
				log.warn("No se ha introducido correctamente los datos del comprador estupefaciente");
				return "ventas/finalizarVenta";
			}else {
				this.ventaService.saveComprador(comprador);
				model.put("estupefaciente", false);
				log.info("Se ha registrado el comprador estupefaceinte con DNI '" + comprador.getDni() + "'");
				return "ventas/finalizarVenta";
			}
		}else if(venta.getPagado() < venta.getImporteTotal()){
			this.ventaService.updateVenta(venta);
			return "redirect:/ventas/actual/cliente";
		}else {
			this.ventaService.finalizarVenta(venta);
			log.info("Se ha completado la venta satisfactoriamente");
			return "redirect:/ventas/actual";
		}
	}
	
	@GetMapping(value= {"/ventas/actual/cliente"})
	public String debeCliente(ModelMap model) {
		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		log.info("la venta debe asignarse a un cliente");
		return "ventas/asignarCliente";
	}
	
	@PostMapping(value= {"/ventas/actual/cliente"})
	public String AsignarCliente(@ModelAttribute("cliente") Cliente cliente, ModelMap model) {
		if(cliente.getDni() != null) {
			cliente = this.clienteService.clienteDni(cliente.getDni());
			model.put("cliente", cliente);
			log.info("Se ha buscado un cliente con DNI '" + cliente.getDni() + "'");
			return "ventas/asignarCliente";
		}else {
			this.ventaService.asignarCliente(cliente.getId());
			log.info("Se ha asignado el cliente satisfactoriamente");
			return "redirect:/ventas/actual";
		}
	}
}

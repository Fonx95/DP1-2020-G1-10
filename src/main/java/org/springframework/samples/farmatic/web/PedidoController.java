package org.springframework.samples.farmatic.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.farmatic.model.LineaPedido;
import org.springframework.samples.farmatic.model.Pedido;
import org.springframework.samples.farmatic.model.Pedido.EstadoPedido;
import org.springframework.samples.farmatic.model.Pedidos;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.model.Proveedor;
import org.springframework.samples.farmatic.service.PedidoService;
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
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class PedidoController {
	
	private final PedidoService pedidoService;
	
	private final ProductoService productoService;
	
	@Autowired
	public PedidoController(PedidoService pedidoService, ProductoService productoService) {
		this.pedidoService = pedidoService;
		this.productoService = productoService;
	}
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@ModelAttribute("pedidoActual")
	public Pedido initnuevalinea(@ModelAttribute("producto") Producto producto, ModelMap model){
		Pedido pedido = this.pedidoService.pedidoActual();
		return pedido;
	}
	
	@GetMapping(value = {"/pedidos"})
	public String showListaPedidos(Map<String, Object> model) {
		Pedidos pedidos = new Pedidos();
		pedidos.getPedidoLista().addAll(this.pedidoService.findPedidos());
		model.put("pedidos", pedidos);
		return "pedidos/pedidoList";
	}
	
	@GetMapping(value = {"/pedidos/{id}"})
	public String showPedido(@PathVariable("id") int pedidoId, Map<String, Object> model) {
		Pedido pedido = this.pedidoService.pedido(pedidoId);
		if(pedido.getEstadoPedido() == EstadoPedido.Borrador) {
			return "redirect:/pedidos/actual";
		}else {
			model.put("pedido", pedido);
			return "pedidos/pedidoDetails";
		}
		
	}
	
	@GetMapping(value= {"/pedidos/actual"})
	public String showPedidoActual(Map<String, Object> model) {
		Producto producto = new Producto();
		model.put("producto", producto);
		return "pedidos/pedidoActual";
	}
	
	@PostMapping(value= {"/pedidos/actual"})
	public String pedidoProcessCreation(@ModelAttribute("producto") Producto producto, @ModelAttribute("nuevaLinea") LineaPedido linea, 
			BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "/pedidos/pedidoActual";
		}else if(producto.getCode()!=null){
			producto = this.productoService.findProductoByCode(producto.getCode());
			linea = pedidoService.newLinea(producto);
			model.addAttribute("nuevaLinea", linea);
			model.addAttribute("producto", producto);
			return "pedidos/pedidoActual";
		}else {
			this.pedidoService.saveLinea(linea);
			model.addAttribute("producto", producto);
			return "pedidos/pedidoActual";
		}
	}
	
	@GetMapping(value= {"/pedidos/actual/{lineaId}"})
	public String showLineaEdit(@PathVariable("lineaId") LineaPedido linea, ModelMap model) {
		Producto producto = new Producto();
		model.put("producto", producto);
		model.put("editaLinea", linea);
		return "pedidos/editarLinea";
	}
	
	@PostMapping(value= {"/pedidos/actual/{lineaId}"})
	public String LineaEdit(@ModelAttribute("producto") Producto producto, @ModelAttribute("editarLinea") LineaPedido linea, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "/pedidos/editarLinea";
		}else if(producto.getCode()!=null){
			return pedidoProcessCreation(producto, linea, result, model);
		}else {
			this.pedidoService.saveLinea(linea);
			return "redirect:/pedidos/actual";
		}
	}
	
	/*
	@GetMapping(value = {"/pedidos/new"})
	public String initCreationForm(Map<String, Object> model) {
			Pedido pedido = new Pedido();
			model.put("pedido", pedido);
			return VIEWS_ORDER_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping(value = {"/pedidos/new"})
	public String processCreationForm(@Valid Pedido pedido, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_ORDER_CREATE_OR_UPDATE_FORM;
		}
		else {
			//creating order
			this.pedidoService.savePedido(pedido);
			
			return "redirect:/pedidos/" + pedido.getId();
		}
	}*/
}

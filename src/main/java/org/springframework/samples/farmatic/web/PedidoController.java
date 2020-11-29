package org.springframework.samples.farmatic.web;

import java.time.LocalDate;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.farmatic.model.EstadoPedido;
import org.springframework.samples.farmatic.model.Pedido;
import org.springframework.samples.farmatic.model.Pedidos;
import org.springframework.samples.farmatic.service.PedidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PedidoController {
	
	
	private final PedidoService pedidoService;
	
	@Autowired
	public PedidoController(PedidoService pedidoService) {
		this.pedidoService = pedidoService;
	}
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@GetMapping(value = {"/pedidos"})
	public String showListaPedidos(Map<String, Object> model) {
		Pedidos pedidos = new Pedidos();
		pedidos.getPedidoLista().addAll(this.pedidoService.findPedidos());
		model.put("pedidos", pedidos);
		return "pedidos/pedidoList";
	}
	
	@GetMapping(value = {"/pedidos/new"})
	public String initCreationForm(ModelMap modelMap) {
			Pedido pedido = new Pedido();
			pedido.setEstadoPedido(EstadoPedido.Borrador);
			pedido.setFechaEntrega(LocalDate.now());
			modelMap.put("pedido", pedido);
			modelMap.put("mensaje", "he llegado");
			return "pedidos/createOrUpdatePedido";
	}
	
	@PostMapping(value = {"/pedidos/new"})
	public String processCreationForm(@Valid Pedido pedido, BindingResult result,ModelMap model) {
		
		pedido.setEstadoPedido(EstadoPedido.Borrador);
		pedido.setFechaEntrega(LocalDate.now());
		
		if (result.hasErrors()) {
			model.put("mensaje", result.getAllErrors());
			model.put("pedido", pedido);
			return "pedidos/createOrUpdatePedido";
		}
		else {
			//creating order
			this.pedidoService.savePedido(pedido);
			
			//return "redirect:/pedidos/" + pedido.getId();
			return "redirect:/pedidos/pedidosList";
		}
	}
}

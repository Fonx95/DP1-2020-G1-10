package org.springframework.samples.farmatic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.farmatic.model.Pedido;
import org.springframework.samples.farmatic.model.Pedidos;
import org.springframework.samples.farmatic.service.PedidoService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PedidoController {
	
	private static final String VIEWS_ORDER_CREATE_OR_UPDATE_FORM = "products/createOrUpdateOrderForm";
	
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
		return "pedidos/pedidoLista";
	}
	
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
	}
}

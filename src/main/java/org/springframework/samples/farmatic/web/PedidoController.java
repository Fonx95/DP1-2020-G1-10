
package org.springframework.samples.farmatic.web;

import java.util.Collection;
import javax.validation.Valid;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class PedidoController {

	private final PedidoService		pedidoService;

	private final ProductoService	productoService;


	@Autowired
	public PedidoController(final PedidoService pedidoService, final ProductoService productoService) {
		this.pedidoService = pedidoService;
		this.productoService = productoService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@ModelAttribute("pedidoActual")
	public Pedido getPedidoActual(){
		Pedido pedido = this.pedidoService.pedidoActual();
		return pedido;
	}
	
	@GetMapping(value = {"/pedidos"})
	public String showListaPedidos(ModelMap model) {
		Pedidos pedidos = new Pedidos();
		pedidos.getPedidoLista().addAll(this.pedidoService.findPedidos());
		model.put("pedidos", pedidos);
		return "pedidos/pedidoList";
	}

	@GetMapping(value = {"/mispedidos"})
	public String miListaPedidos(final Map<String, Object> model) {
		Pedidos pedidos = new Pedidos();
		pedidos.getPedidoLista().addAll(this.pedidoService.findMisPedidos());
		model.put("pedidos", pedidos);
		return "pedidos/pedidoList";
	}
	
	@GetMapping(value = {"/mispedidos/{id}"})
	public String miPedido(@PathVariable("id") final int pedidoId, final Map<String, Object> model) {
		Pedido pedido = this.pedidoService.pedido(pedidoId);
		model.put("pedido", pedido);
		return "pedidos/pedidoDetails";
	}

	@GetMapping(value = {"/pedidos/{id}"})
	public String showPedido(@PathVariable("id") Pedido pedido, ModelMap model) {
		if(pedido.getEstadoPedido() == EstadoPedido.Borrador) {
			return "redirect:/pedidos/actual";
		} else {
			model.put("pedido", pedido);
			return "pedidos/pedidoDetails";
		}
	}
	
	@PostMapping(value="/pedidos/{id}")
	public String pedidoRecibido(@ModelAttribute("pedido") Pedido pedido, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "/pedidos/" + pedido.getId();
		}else {
			this.pedidoService.pedidoRecibido(pedido);
			return "redirect:/pedidos/" + pedido.getId();
		}
	}
	
	@GetMapping(value= {"/pedidos/actual"})
	public String showPedidoActual(ModelMap model) {
		Producto producto = new Producto();
		model.put("producto", producto);
		return "pedidos/pedidoActual";
	}

	@PostMapping(value = {"/pedidos/actual"})
	public String pedidoProcessCreation(@ModelAttribute("producto") Producto producto, @ModelAttribute("nuevaLinea") LineaPedido linea, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			return "/pedidos/pedidoActual";
		} else if (producto.getCode() != null) {
			producto = this.productoService.findProductoByCode(producto.getCode());
			linea = pedidoService.newLinea(producto,1);
			model.addAttribute("nuevaLinea", linea);
			model.addAttribute("producto", producto);
			return "pedidos/pedidoActual";
		} else {
			this.pedidoService.saveLinea(linea);
			model.addAttribute("producto", producto);
			return "pedidos/pedidoActual";
		}
	}

	@GetMapping(value = {"/pedidos/actual/{lineaId}"})
	public String showLineaEdit(@PathVariable("lineaId") final LineaPedido linea, final ModelMap model) {
		Producto producto = new Producto();
		model.put("producto", producto);
		model.put("editaLinea", linea);
		return "pedidos/editarLinea";
	}
	
	@PostMapping(value= {"/pedidos/actual/{lineaId}"})
	public String LineaEdit(@ModelAttribute("producto") Producto producto, @ModelAttribute("editarLinea") LineaPedido linea, 
			BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "/pedidos/editarLinea";
		}else if(producto.getCode()!=null){
			return pedidoProcessCreation(producto, linea, result, model);
		}else if(linea.getCantidad() == 0){
			this.pedidoService.deleteLinea(linea);
			return "redirect:/pedidos/actual";
		}else {
			this.pedidoService.saveLinea(linea);
			return "redirect:/pedidos/actual";
		}
	}

	@GetMapping(value={"/pedidos/actual/pedir"})
	public String sendPedido(ModelMap model) {
		Collection<Proveedor> proveedores = pedidoService.findProveedores();
		Proveedor proveedor = new Proveedor();
		model.addAttribute("proveedores", proveedores);
		model.addAttribute("proveedor", proveedor);
		return "pedidos/enviarPedido";
	}
	
	@PostMapping(value={"/pedidos/actual/pedir"})
	public String createPedido(@Valid Proveedor proveedor, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "/pedidos/actual/pedir";
		}else {
			this.pedidoService.enviarPedido(proveedor);
			return "redirect:/pedidos";
		}
	}
	
	
	@PostMapping(value="/mispedidos/{id}")
	public String pedidoEnviado(@ModelAttribute("pedido") Pedido pedido, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "/mispedidos/" + pedido.getId();
		}else {
			this.pedidoService.pedidoEnviado(pedido);
			return "redirect:/mispedidos/" + pedido.getId();
		}
	}
  
}

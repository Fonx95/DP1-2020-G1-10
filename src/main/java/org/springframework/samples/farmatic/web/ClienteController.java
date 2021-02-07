
package org.springframework.samples.farmatic.web;

import java.util.Collection;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.farmatic.model.Cliente;
import org.springframework.samples.farmatic.model.User;
import org.springframework.samples.farmatic.model.Venta;
import org.springframework.samples.farmatic.model.validator.ClienteValidator;
import org.springframework.samples.farmatic.service.ClienteService;
import org.springframework.samples.farmatic.service.UserService;
import org.springframework.samples.farmatic.service.VentaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ClienteController {

	private static final String VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM	= "clientes/createOrUpdateClienteForm";

	private final ClienteService clienteService;
	
	private final VentaService ventaService;
	
	private UserService userService;

	@Autowired
	public ClienteController(final ClienteService clienteService, final VentaService ventaService, final UserService userService) {
		this.clienteService = clienteService;
		this.ventaService = ventaService;
		this.userService = userService;
	}

	@InitBinder("cliente")
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setValidator(new ClienteValidator());
	}

	//==================== COMO FARMACEUTICO ====================
	
	@GetMapping(value = {"/clientes"})
	public String listadoClientes(final ModelMap model) {
		Collection<Cliente> clientes = this.clienteService.findClientes();
		model.addAttribute("clientes", clientes);
		log.info("Se han mostrado todos los clientes: " + clientes.size());
		return "clientes/clienteList";
	}

	@GetMapping(value = {"/clientes/{idCliente}"})
	public String showClientes(@PathVariable("idCliente") final int idCliente, final ModelMap model) {
		Cliente cliente = this.clienteService.findClienteById(idCliente);
		model.addAttribute("cliente", cliente);
		log.info("Se ha mostrado los detalles del cliente " + cliente.getName() + " " + cliente.getSurnames() + " (" + cliente.getDni() + ")");
		return "clientes/clienteDetails";
	}
	
	@GetMapping(value = {"/clientes/{idCliente}/edit"})
	public String initEditCliente(@PathVariable("idCliente") Cliente cliente, ModelMap model) {
		model.addAttribute("cliente", cliente);
		return VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM;
	}
	
	@PostMapping(value = {"/clientes/{idCliente}/edit"})
	public String editCliente(@Valid final Cliente cliente, final BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM;
		} else {
			this.clienteService.saveCliente(cliente);
			log.info("Se ha modificado los datos de un cliente ");
			return "redirect:/clientes/" + cliente.getId();
		}
	}
	
	@GetMapping(value = {"/clientes/new"})
	public String initCreationForm(final ModelMap model) {
		Cliente cliente = new Cliente();
		model.addAttribute("cliente", cliente);
		return VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = {"/clientes/new"})
	public String processCreationForm(@Valid final Cliente cliente, final BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM;
		} else {
			this.clienteService.saveCliente(cliente);
			log.info("Se ha creado un nuevo cliente");
			return "redirect:/clientes/" + cliente.getId();
		}
	}
	
	//==================== COMO CLIENTE ====================
	
	@GetMapping(value = {"/cliente/ventas"})
	public String listadoVentas(ModelMap model) {
		User user = this.userService.getCurrentUser();
		Cliente cliente = this.clienteService.findClienteUser(user);
		model.addAttribute("cliente", cliente);
		log.info("Se ha mostrado las ventas del usuario '" + user.getUsername() + "' - " + cliente.getName() + " " + cliente.getSurnames());
		return "clientes/clienteVentas";
	}

	@GetMapping(value = {"/cliente/ventas/{idVenta}"})
	public String showVenta(@PathVariable("idVenta") final int idVenta, ModelMap model) {
		Venta venta = this.ventaService.venta(idVenta);
		model.addAttribute("venta", venta);
		log.info("Se ha mostrado los detalles de la venta " + venta.getId() + " del usuario '"  
				+ venta.getCliente().getUser().getUsername() + "' - " + venta.getCliente().getName() + " " + venta.getCliente().getSurnames());
		return "ventas/ventaDetails";
	}
}


package org.springframework.samples.farmatic.web;

import java.util.Collection;
import java.util.Map;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.farmatic.model.Cliente;
import org.springframework.samples.farmatic.model.Venta;
import org.springframework.samples.farmatic.service.ClienteService;
import org.springframework.samples.farmatic.service.VentaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ClienteController {

	private static final String VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM	= "clientes/createOrUpdateClienteForm";

	private final ClienteService clienteService;
	
	private final VentaService ventaService;

	@Autowired
	public ClienteController(final ClienteService clienteService, final VentaService ventaService) {
		this.clienteService = clienteService;
		this.ventaService = ventaService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	//==================== COMO FARMACEUTICO ====================
	
	@GetMapping(value = {"/clientes"})
	public String listadoClientes(final ModelMap modelMap) {
		Collection<Cliente> clientes = this.clienteService.findClientes();
		modelMap.addAttribute("clientes", clientes);
		return "clientes/clienteList";
	}

	@GetMapping("/clientes/{idCliente}")
	public ModelAndView showClientes(@PathVariable("idCliente") final int idCliente) {
		ModelAndView mav = new ModelAndView("clientes/clienteDetails");
		Cliente cliente = this.clienteService.findClienteById(idCliente);
		mav.addObject(cliente);
		return mav;

	}
	
	@GetMapping(value = "/clientes/new")
	public String initCreationForm(final Map<String, Object> model) {
		Cliente cliente = new Cliente();	
		
		model.put("cliente", cliente);
		return VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/clientes/new")
	public String processCreationForm(@Valid final Cliente cliente, final BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_CLIENTE_CREATE_OR_UPDATE_FORM;
		} else {
			//creating owner, user and authorities
			
			this.clienteService.saveCliente(cliente);

			return "redirect:/clientes/clienteList/" + cliente.getId();
		}
	}
	
	//==================== COMO CLIENTE ====================
	
	@GetMapping("/profile/me")
	public ModelAndView showCliente() {
		ModelAndView mav = new ModelAndView("clientes/clienteDetails");
		Cliente cliente = this.clienteService.findClienteData();
		mav.addObject(cliente);
		return mav;

	}
	
	@GetMapping(value = {"/misVentas"})
	public String listadoVentas( final ModelMap modelMap) {
		Cliente cliente=this.clienteService.findClienteData();
		modelMap.addAttribute("cliente", cliente);
		return "clientes/clienteVentas";
	}

	@GetMapping(value="/misVentas/{idVenta}")
	public ModelAndView showVenta(@PathVariable("idVenta") final int idVenta) {
		ModelAndView mav = new ModelAndView("ventas/ventaDetails");
		Venta venta = this.ventaService.venta(idVenta);
		mav.addObject(venta);
		return mav;
	}
}

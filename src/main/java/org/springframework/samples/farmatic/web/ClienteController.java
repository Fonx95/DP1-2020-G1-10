
package org.springframework.samples.farmatic.web;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.farmatic.model.Cliente;
import org.springframework.samples.farmatic.service.ClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ClienteController {

	private static final String		VIEWS_PRODUCT_CREATE_OR_UPDATE_FORM	= "clientes/createOrUpdateClienteForm";

	private final ClienteService	clienteService;


	@Autowired
	public ClienteController(final ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = {
		"/clientes"
	})
	public String listadoClientes(final ModelMap modelMap) {
		Iterable<Cliente> clientes = this.clienteService.findClientes();
		modelMap.addAttribute("clientes", clientes);
		return "clientes/clienteList";
	}

	@GetMapping("/clientes/me")
	public ModelAndView showClientes() {
		ModelAndView mav = new ModelAndView("clientes/clienteDetails");
		Cliente cliente = this.clienteService.findClienteData();
		mav.addObject(cliente);
		return mav;

	}
	
	



}

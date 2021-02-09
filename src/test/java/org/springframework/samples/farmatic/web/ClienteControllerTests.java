package org.springframework.samples.farmatic.web;

import java.util.ArrayList;
import java.util.Collection;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.farmatic.configuration.SecurityConfiguration;
import org.springframework.samples.farmatic.model.Cliente;

import org.springframework.samples.farmatic.model.User;
import org.springframework.samples.farmatic.model.Venta;
import org.springframework.samples.farmatic.model.Venta.EstadoVenta;
import org.springframework.samples.farmatic.service.ClienteService;
import org.springframework.samples.farmatic.service.UserService;
import org.springframework.samples.farmatic.service.VentaService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
@WebMvcTest(controllers = ClienteController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class ClienteControllerTests {
	private static final int	TEST_CLIENTE_ID	= 1;
	private static final int	TEST_VENTA_ID	= 1;
	@MockBean
	private UserService			userService;
	@MockBean
	private ClienteService 		clienteService;
	@MockBean
	private VentaService 		ventaService;
	@Autowired
	private MockMvc				mockMvc;
	
	private Cliente	clienteTest;
	
	
	@BeforeEach
	void setup() {
		Cliente cliente = new Cliente();
		cliente.setId(1);
		cliente.setProvincia("Sevilla");
		cliente.setLocalidad("Alcala");
		cliente.setDireccion("Calle Pinto");
		cliente.setPorPagarTotal(35.0);
		User user= new User();
		user.setCliente(cliente);
		user.setPassword("1234");
		user.setUsername("cliente");
		
		cliente.setDni("20035098Y");
		cliente.setSurnames("Maria Polo");
		cliente.setName("Marta");
		cliente.setUser(user);
		cliente.setVenta(new ArrayList<Venta>());
		this.clienteTest=cliente;
		Venta venta=new Venta();
		venta.setId(1);
		venta.setCliente(cliente);
	
	
//		venta.setImporteTotal(50.0);
//		venta.setPorPagar(35.5);
//		
//		venta.setEstadoVenta(EstadoVenta.Realizada);
//		venta.setLineaVenta(null);
		
		BDDMockito.given(this.clienteService.findClienteById(1)).willReturn(cliente);
		BDDMockito.given(this.clienteService.findClienteUser(user)).willReturn(cliente);
		BDDMockito.given(this.userService.getCurrentUser()).willReturn(user);
		BDDMockito.given(this.ventaService.venta(1)).willReturn(venta);
	}
	
//Positivos
	
	@WithMockUser(value = "spring", authorities = "farmaceutico") 
	@Test
	void testShowClientesSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/clientes/{idCliente}", ClienteControllerTests.TEST_CLIENTE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("cliente"))
			.andExpect(MockMvcResultMatchers.view().name("clientes/clienteDetails"));
	}
	
	
	@WithMockUser(value = "spring", authorities = "farmaceutico")
	@Test
	void testCreateClienteSuccess() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/clientes/new", TEST_CLIENTE_ID))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("cliente"))
		.andExpect(MockMvcResultMatchers.view().name("clientes/createOrUpdateClienteForm"));
	}
	@WithMockUser(value = "spring", authorities = "farmaceutico")
	@Test
	void initEditClienteSuccess() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/clientes/{idCliente}/edit", TEST_CLIENTE_ID))
		.andExpect(MockMvcResultMatchers.model().attributeExists("cliente"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("clientes/createOrUpdateClienteForm"));
	}
//	@WithMockUser(value = "spring", authorities = "farmaceutico")
//	@Test
//	void postEditClienteSuccess() throws Exception {
//
//		mockMvc.perform(MockMvcRequestBuilders.post("/clientes/{idCliente}/edit", TEST_CLIENTE_ID))
//		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
//		.andExpect(MockMvcResultMatchers.view().name("redirect:/clientes/" + TEST_CLIENTE_ID));
//	}
	@WithMockUser(value = "spring", authorities = "farmaceutico")
	@Test
	void initCreationFormSuccess() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.get("/clientes/new")
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(MockMvcResultMatchers.model().attributeExists("cliente"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view()
						.name("clientes/createOrUpdateClienteForm"));
	}

	@WithMockUser(value = "spring", authorities = "farmaceutico")
	@Test
	void processCreationFormSuccess() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.post("/clientes/new", 3)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("Nombre", "Marta")
				.param("Apellidos", "Conde Ma")
				.param("DNI", "30025639Q")
				.param("Provincia", "Malaga")
				.param("Localidad", "Alcala")
				)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("clientes/createOrUpdateClienteForm"));
	}
	@WithMockUser(value = "spring", authorities = "cliente")
	@Test
	void testlistadoVentasSuccess() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/cliente/ventas"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("cliente"))
		.andExpect(MockMvcResultMatchers.model().attribute("cliente",Matchers.hasProperty("venta",Matchers.is(new ArrayList<Venta>()))))
		.andExpect(MockMvcResultMatchers.view().name("clientes/clienteVentas"));
	}
	@WithMockUser(value = "spring", authorities = "cliente") 
	@Test
	void testShowVentaSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/ventas/{idVenta}", 1)
				.with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk())
			
			.andExpect(MockMvcResultMatchers.view().name("ventas/ventaDetails"));
	}
	
//Negativos
	
	@WithMockUser(value = "spring", authorities = "farmaceutico") 
	@Test
	void testClienteNoExiste() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/clientes/{idCliente}", -10)
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring", authorities = "farmaceutico")
	@Test
	void testNotCreateClienteSuccess() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/clientes/new", 10))
		.andExpect(MockMvcResultMatchers.status().isOk())
		
		.andExpect(MockMvcResultMatchers.view().name("clientes/createOrUpdateClienteForm"));
	}

	@WithMockUser(value = "spring", authorities = "farmaceutico")
	@Test
	void testClienteEditError() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/clientes/{idCliente}/edit", -10)
				.flashAttr("editarCliente", this.clienteTest))
	
		.andExpect(MockMvcResultMatchers.status().isOk())
		//.andExpect(MockMvcResultMatchers.model().attributeExists("cliente"))
		.andExpect(MockMvcResultMatchers.view().name("clientes/createOrUpdateClienteForm"));
	}
	@WithMockUser(value = "spring", authorities = "farmaceutico")
	@Test
	void processCreationFormError() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders.post("/clientes/new")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("Nombre", "Marta")
				.param("Apellidos", "Conde Ma")
				.param("DNI", "30025639Q")
				.param("Provincia", "Malaga")
				.param("Localidad", "")
				)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("clientes/createOrUpdateClienteForm"));
	}
	@WithMockUser(value = "spring", authorities = "cliente")
	@Test
	void testlistadoVentasError() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/clientes/ventas", -10))
		.andExpect(MockMvcResultMatchers.status().isOk())
	
		.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
	@WithMockUser(value = "spring", authorities = "cliente") 
	@Test
	void testShowVentaError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/cliente/ventas/{idVenta}",-10)
			.with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
	
	
}

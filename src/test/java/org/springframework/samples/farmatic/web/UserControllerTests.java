package org.springframework.samples.farmatic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.farmatic.configuration.SecurityConfiguration;
import org.springframework.samples.farmatic.model.Authorities;
import org.springframework.samples.farmatic.model.Cliente;
import org.springframework.samples.farmatic.model.Farmaceutico;
import org.springframework.samples.farmatic.model.Proveedor;
import org.springframework.samples.farmatic.model.User;
import org.springframework.samples.farmatic.model.UserValidate;
import org.springframework.samples.farmatic.repository.UserRepository;
import org.springframework.samples.farmatic.service.AuthoritiesService;
import org.springframework.samples.farmatic.service.ClienteService;
import org.springframework.samples.farmatic.service.FarmaceuticoService;
import org.springframework.samples.farmatic.service.ProveedorService;
import org.springframework.samples.farmatic.service.UserService;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.persistence.EntityNotFoundException;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = UserController.class, 
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), 
	excludeAutoConfiguration = SecurityConfiguration.class)
public class UserControllerTests {
	
	private static final int AUTHORITY_ID = 1;
	
	private static final int CLIENTE_ID = 1;
	
	private static final int FARMACEUTICO_ID = 1;
	
	private static final int PROVEEDOR_ID = 1;
	
	private User userTest;
	
	private Authorities authorityTest;
	
	private Cliente clienteTest;
	
	private Farmaceutico farmaceuticoTest;
	
	private Proveedor proveedorTest;
	
	@Autowired
	private UserController userController;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private UserRepository userRepository;
	
	@MockBean
	private AuthoritiesService AuthoritiesService;
	
	@MockBean
	private ClienteService clienteService;
	
	@MockBean
	private FarmaceuticoService farmaceuticoService;
	
	@MockBean
	private ProveedorService proveedorService;
	
	@BeforeEach
	void setup() {
		User user = new User();
		Authorities authority = new Authorities();
		Cliente cliente = new Cliente();
		Farmaceutico farmaceutico = new Farmaceutico();
		Proveedor proveedor = new Proveedor();
		
		user.setUsername("usernameTest");
		user.setPassword("passwordTest");
		user.setEnabled(true);
		user.setCliente(cliente);
		user.setFarmaceutico(farmaceutico);
		user.setProveedor(proveedor);
		//user.getAuthorities().add(authority);
		
		authority.setId(AUTHORITY_ID);
		authority.setUser(user);
		authority.setAuthority("cliente");
		
		cliente.setId(CLIENTE_ID);
		cliente.setDni("dniTest");
		cliente.setName("nameTest");
		cliente.setSurnames("surnameTest");
		cliente.setPorPagarTotal(0.0);
		cliente.setDireccion("direccionTest");
		cliente.setLocalidad("localidadTest");
		cliente.setProvincia("provinciaTest");
		cliente.setUser(user);
		
		farmaceutico.setId(FARMACEUTICO_ID);
		farmaceutico.setName("nameTest");
		farmaceutico.setSurnames("surnameTest");
		farmaceutico.setDni("dniTest");
		farmaceutico.setPharmacyAddress("addresTest");
		farmaceutico.setUser(user);
		
		proveedor.setId(PROVEEDOR_ID);
		proveedor.setEmpresa("empresaTest");
		proveedor.setCif("cifTest");
		proveedor.setDireccion("direccionTest");
		proveedor.setUser(user);
		
		this.userTest = user;
		
		this.authorityTest = authority;
		
		this.clienteTest = cliente;
		
		this.farmaceuticoTest = farmaceutico;
		
		this.proveedorTest = proveedor;
		
		given(this.userService.getCurrentUser()).willReturn(this.userTest);
		given(this.AuthoritiesService.findAuthoritiyByUser(this.userTest)).willReturn(this.authorityTest);
		given(this.clienteService.findClienteUser(this.userTest)).willReturn(this.clienteTest);
		given(this.farmaceuticoService.findFarmaceuticoByUser(this.userTest)).willReturn(this.farmaceuticoTest);
		given(this.proveedorService.findProveedorUser(this.userTest)).willReturn(this.proveedorTest);
	}
	
	@WithMockUser(value = "spring", authorities = {"cliente", "farmaceutico", "proveedor"})
	@Test
	void testShowUserDetails1() throws Exception {
		this.mockMvc.perform(get("/users").with(csrf()))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("cliente"))
			.andExpect(view().name("users/userDetails"));
	}
	
	@WithMockUser(value = "spring", authorities = "farmaceutico")
	@Test
	void testShowUserDetails2() throws Exception {
		this.authorityTest.setAuthority("farmaceutico");
		this.mockMvc.perform(get("/users").with(csrf()))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("farmaceutico"))
			.andExpect(view().name("users/userDetails"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testNewUser() throws Exception {
		this.mockMvc.perform(get("/users/new").with(csrf()))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("cliente"))
			.andExpect(view().name("users/userRegister"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testCreationUserSuccess1() throws Exception {
		given(this.clienteService.clienteDni(this.clienteTest.getDni())).willReturn(this.clienteTest);
		this.clienteTest.setUser(null);
		this.mockMvc.perform(post("/users/new").with(csrf()).param("dni", "dniTest"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("cliente"))
			.andExpect(view().name("users/userRegister"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testCreationUserSuccess2() throws Exception {
		this.clienteTest.setUser(new User());
		this.mockMvc.perform(post("/users/new").with(csrf())
				.flashAttr("cliente", this.clienteTest)
				.param("user.username", "usernameTest1")
				.param("user.password", "passwordTest1"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("../"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testCreationUserFail() throws Exception {
		given(this.clienteService.clienteDni("")).willThrow(EntityNotFoundException.class);
		this.clienteTest.setUser(null);
		this.mockMvc.perform(post("/users/new").with(csrf()).param("dni", ""))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("cliente"))
			.andExpect(model().attributeHasFieldErrors("cliente", "dni"))
			.andExpect(view().name("users/userRegister"));
	}
	
	@WithMockUser(value = "spring", authorities = "cliente")
	@Test
	void testInitChangePassword() throws Exception {
		this.mockMvc.perform(get("/users/password").with(csrf()))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("user"))
			.andExpect(view().name("users/passwordEdit"));
	}
	
	@WithMockUser(value = "spring", authorities = "cliente")
	@Test
	void testInitChangePasswordSuccess() throws Exception {
		UserValidate user = new UserValidate();
		user.setNewPassword("passwordTest1");
		user.setPassword("passwordTest");
		user.setUsername("usernameTest");
		user.setValidPassword("passwordTest1");
		this.mockMvc.perform(post("/users/password").with(csrf())
				.flashAttr("user", user))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("../"));
	}
	
	@WithMockUser(value = "spring", authorities = "cliente")
	@Test
	void testInitChangePasswordFail() throws Exception {
		UserValidate user = new UserValidate();
		user.setNewPassword("");
		user.setPassword("");
		user.setUsername("");
		user.setValidPassword("");
		this.mockMvc.perform(post("/users/password").with(csrf())
				.flashAttr("user", user))
			.andExpect(status().isOk())
			.andExpect(model().attributeErrorCount("user", 4))
			.andExpect(view().name("users/passwordEdit"));
	}
}

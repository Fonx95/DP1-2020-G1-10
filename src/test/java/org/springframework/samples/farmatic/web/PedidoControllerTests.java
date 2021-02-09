
package org.springframework.samples.farmatic.web;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.EntityNotFoundException;

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
import org.springframework.samples.farmatic.model.LineaPedido;
import org.springframework.samples.farmatic.model.Pedido;
import org.springframework.samples.farmatic.model.Pedido.EstadoPedido;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.model.Proveedor;
import org.springframework.samples.farmatic.service.PedidoService;
import org.springframework.samples.farmatic.service.ProductoService;
import org.springframework.samples.farmatic.service.ProveedorService;
import org.springframework.samples.farmatic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = PedidoController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class PedidoControllerTests {

	private static final int	TEST_LINEA_ID	= 1;
	private static final int	TEST_PEDIDO_ID	= 1;
	private static final int	TEST_PROV_ID	= 1;

	@Autowired
	private PedidoController	pedidoController;

	@MockBean
	private PedidoService		pedidoService;

	@MockBean
	private ProductoService		productoService;

	@MockBean
	private ProveedorService	proveedorService;

	@MockBean
	private UserService			userService;

	@Autowired
	private MockMvc				mockMvc;

	private Pedido				pedidoTest;

	private Producto			productoTest;

	private LineaPedido			lineaTest;

	private Proveedor			provTest;


	// Recordatorio: no se puede crear como tal un pedido por lo que se comprabará en la función de enviarPedido.
	@BeforeEach
	void setup() {
		Proveedor prov = new Proveedor();
		prov.setCif("CIFtest");
		prov.setDireccion("test");
		prov.setEmpresa("empresaTest");
		prov.setId(PedidoControllerTests.TEST_PROV_ID);
		this.provTest = prov;
		Collection<Proveedor> pl = new ArrayList<>();
		pl.add(prov);
		BDDMockito.given(this.proveedorService.findProveedores()).willReturn(pl);
		Pedido p = new Pedido();
		Collection<LineaPedido> lp = new ArrayList<>();
		p.setCodigo("P-test");
		p.setEstadoPedido(EstadoPedido.Borrador);
		p.setId(PedidoControllerTests.TEST_PEDIDO_ID);
		p.setLineaPedido(lp);
		this.pedidoTest = p;
		BDDMockito.given(this.pedidoService.pedido(PedidoControllerTests.TEST_PEDIDO_ID)).willReturn(this.pedidoTest);
		Producto producto = new Producto();
		producto.setCode("Pr-test");
		producto.setId(1);
		this.productoTest = producto;
		BDDMockito.given(this.productoService.findProductoByCode(this.productoTest.getCode())).willReturn(this.productoTest);
		LineaPedido l = new LineaPedido();
		l.setId(PedidoControllerTests.TEST_LINEA_ID);
		l.setPedido(this.pedidoTest);
		l.setCantidad(1);
		l.setProducto(producto);
		this.lineaTest = l;
		BDDMockito.given(this.pedidoService.pedidoActual()).willReturn(this.pedidoTest);
		BDDMockito.given(this.pedidoService.newLinea(this.productoTest, 1)).willReturn(this.lineaTest);
		BDDMockito.given(this.pedidoService.existelinea(this.productoTest)).willReturn(PedidoControllerTests.TEST_LINEA_ID);
		BDDMockito.given(this.pedidoService.lineaById(PedidoControllerTests.TEST_LINEA_ID)).willReturn(this.lineaTest);
	}

	//---------------------- Tests linea de pedido --------------------------------------
	@WithMockUser(value = "spring", authorities = "farmaceutico")
	@Test
	void testShowLineaEditSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/pedidos/actual/{lineaId}", PedidoControllerTests.TEST_LINEA_ID).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("producto")).andExpect(MockMvcResultMatchers.model().attributeExists("editaLinea")).andExpect(MockMvcResultMatchers.view().name("pedidos/editarLinea"));
	}

	@WithMockUser(value = "spring", authorities = "farmaceutico") // Se le pasa una ID de linea inexistente
	@Test
	void testShowLineaEditError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/pedidos/actual/{lineaId}", 3).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring", authorities = "farmaceutico") // En este test comprabamos el segundo if de lineaEdit, producto.getCode() != null
	@Test
	void testLineaEditSuccess1() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/pedidos/actual/{lineaId}", PedidoControllerTests.TEST_LINEA_ID).flashAttr("producto", this.productoTest).flashAttr("editaLinea", this.lineaTest).with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/pedidos/actual/" + PedidoControllerTests.TEST_LINEA_ID));
	}

	@WithMockUser(value = "spring", authorities = "farmaceutico") // En este test comprabamos el tercer if de lineaEdit, linea.getCantidad() == 0
	@Test
	void testLineaEditSuccess2() throws Exception {
		this.lineaTest.setCantidad(0);
		this.mockMvc.perform(MockMvcRequestBuilders.post("/pedidos/actual/{lineaId}", PedidoControllerTests.TEST_LINEA_ID).flashAttr("editaLinea", this.lineaTest).with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/pedidos/actual"));
	}

	@WithMockUser(value = "spring", authorities = "farmaceutico") // En este test comprabamos la cuarta salida del if de lineaEdit
	@Test
	void testLineaEditSuccess3() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/pedidos/actual/{lineaId}", PedidoControllerTests.TEST_LINEA_ID).flashAttr("editaLinea", this.lineaTest).with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/pedidos/actual"));
	}

	@WithMockUser(value = "spring", authorities = "farmaceutico")
	@Test
	void testLineaEditError() throws Exception { // La cantidad no puede ser nula
		this.lineaTest.setCantidad(null);
		this.mockMvc.perform(MockMvcRequestBuilders.post("/pedidos/actual/{lineaId}", PedidoControllerTests.TEST_LINEA_ID).flashAttr("editarLinea", this.lineaTest).with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	//--------------------------- Test de pedido ----------------------------------------------
	@WithMockUser(value = "spring", authorities = "farmaceutico")
	@Test
	void testPedidoRecibidoSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/pedidos/{id}", PedidoControllerTests.TEST_PEDIDO_ID).flashAttr("pedido", this.pedidoTest).with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/pedidos/" + PedidoControllerTests.TEST_PEDIDO_ID));
	}

	@WithMockUser(value = "spring", authorities = "farmaceutico") // El pedido no existe, por lo que redirige a un pedido en blanco
	@Test
	void testPedidoRecibidoError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/pedidos/{id}", 2).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/pedidos/null"));
	}

	@WithMockUser(value = "spring", authorities = "farmaceutico") // En este test comprabamos la primera salida del segundo if de pedidoProcessCreation, producto.getCode() != null y this.pedidoService.existelinea(producto) != null
	@Test
	void testPedidoProcessCreationSuccess1() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/pedidos/actual").flashAttr("producto", this.productoTest).flashAttr("nuevaLinea", this.lineaTest).with(SecurityMockMvcRequestPostProcessors.csrf()))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/pedidos/actual/" + PedidoControllerTests.TEST_LINEA_ID));
	}

	@WithMockUser(value = "spring", authorities = "farmaceutico") // En este test comprabamos la segunda salida del segundo if de pedidoProcessCreation, producto.getCode() != null
	@Test
	void testPedidoProcessCreationSuccess2() throws Exception {
		Producto p = new Producto();
		p.setId(2);
		p.setCode("PR-test2");
		BDDMockito.given(this.pedidoService.existelinea(p)).willReturn(null);
		BDDMockito.given(this.productoService.findProductoByCode(p.getCode())).willReturn(p);
		this.lineaTest.setId(2);
		this.lineaTest.setProducto(p);
		this.lineaTest.setCantidad(1);
		BDDMockito.given(this.pedidoService.newLinea(p, 1)).willReturn(this.lineaTest);
		this.mockMvc.perform(MockMvcRequestBuilders.post("/pedidos/actual").flashAttr("producto", p).flashAttr("nuevaLinea", new LineaPedido()).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("producto")).andExpect(MockMvcResultMatchers.model().attribute("producto", Matchers.hasProperty("code", Matchers.is("PR-test2"))))
			.andExpect(MockMvcResultMatchers.model().attributeExists("nuevaLinea")).andExpect(MockMvcResultMatchers.model().attribute("nuevaLinea", Matchers.hasProperty("id", Matchers.is(2))))
			.andExpect(MockMvcResultMatchers.view().name("pedidos/pedidoActual"));
	}

	@WithMockUser(value = "spring", authorities = "farmaceutico") // En este test comprabamos la salida del tercer if de pedidoProcessCreation
	@Test
	void testPedidoProcessCreationSuccess3() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/pedidos/actual").flashAttr("producto", new Producto()).flashAttr("nuevaLinea", this.lineaTest).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("producto")).andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("nuevaLinea")).andExpect(MockMvcResultMatchers.view().name("pedidos/pedidoActual"));
	}

	@WithMockUser(value = "spring", authorities = "farmaceutico") // En este test comprabamos el catch de pedidoProcessCreation para un producto inexistente
	@Test
	void testPedidoProcessCreationError() throws Exception {
		Producto p = new Producto();
		p.setCode("12234");
		BDDMockito.given(this.productoService.findProductoByCode(p.getCode())).willThrow(EntityNotFoundException.class);
		this.mockMvc.perform(MockMvcRequestBuilders.post("/pedidos/actual").with(SecurityMockMvcRequestPostProcessors.csrf()).flashAttr("producto", p).flashAttr("nuevaLinea", this.lineaTest)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("errorProducto", Matchers.is("El producto no existe"))).andExpect(MockMvcResultMatchers.view().name("/pedidos/pedidoActual"));
	}

	@WithMockUser(value = "spring", authorities = "farmaceutico") // No se puede hacer negativo por lo que solo estará este
	@Test
	void testSendPedidoSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/pedidos/actual/pedir").with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("proveedores"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("proveedor")).andExpect(MockMvcResultMatchers.view().name("pedidos/enviarPedido"));
	}

	@WithMockUser(value = "spring", authorities = "farmaceutico")
	@Test
	void testCreatePedidoSuccess() throws Exception {
		Collection<LineaPedido> lp = this.pedidoTest.getLineaPedido();
		lp.add(this.lineaTest);
		this.pedidoTest.setLineaPedido(lp);
		this.mockMvc.perform(MockMvcRequestBuilders.post("/pedidos/actual/pedir").with(SecurityMockMvcRequestPostProcessors.csrf()).flashAttr("proveedor", this.provTest)).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/pedidos"));
	}

	@WithMockUser(value = "spring", authorities = "farmaceutico") // Proveedor con el CIF a null, es decir, un proveedor no válido. 
	@Test
	void testCreatePedidoError() throws Exception {
		this.provTest.setCif(null);
		this.mockMvc.perform(MockMvcRequestBuilders.post("/pedidos/actual/pedir").with(SecurityMockMvcRequestPostProcessors.csrf()).flashAttr("proveedor", this.provTest)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pedidos/enviarPedido"));
	}

}

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
import org.springframework.samples.farmatic.model.Cliente;
import org.springframework.samples.farmatic.model.LineaVenta;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.model.TipoTasa;
import org.springframework.samples.farmatic.model.Venta;
import org.springframework.samples.farmatic.model.Venta.EstadoVenta;
import org.springframework.samples.farmatic.model.validator.LineaVentaValidator;
import org.springframework.samples.farmatic.service.ClienteService;
import org.springframework.samples.farmatic.service.ProductoService;
import org.springframework.samples.farmatic.service.UserService;
import org.springframework.samples.farmatic.service.VentaService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = VentaController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class VentaControllerTests {
	
	private static final int	TEST_LINEA_ID	= 1;
	private static final int	TEST_VENTA_ID	= 1;
	private static final int	TEST_CLT_ID		= 1;

	@Autowired
	private VentaController	ventaController;

	@MockBean
	private VentaService		ventaService;

	@MockBean
	private ProductoService		productoService;

	@MockBean
	private ClienteService		clienteService;

	@MockBean
	private UserService			userService;

	@Autowired
	private MockMvc				mockMvc;

	private Venta				ventaTest;

	private Producto			productoTest;

	private LineaVenta			lineaTest;
	
	private Cliente				cltTest;
	
	@BeforeEach
	void setup() {
		Cliente clt = new Cliente();
		clt.setDireccion("direccionTest");
		clt.setLocalidad("localidadTest");
		clt.setProvincia("provinciaTest");
		clt.setId(VentaControllerTests.TEST_CLT_ID);
		this.cltTest = clt;
		Collection<Cliente> lc = new ArrayList<>();
		lc.add(clt);
		BDDMockito.given(this.clienteService.findClientes()).willReturn(lc);
		Venta v = new Venta();
		Collection<LineaVenta> lv = new ArrayList<>();
		v.setEstadoVenta(EstadoVenta.enProceso);
		v.setId(VentaControllerTests.TEST_VENTA_ID);
		v.setLineaVenta(lv);
		this.ventaTest = v;
		BDDMockito.given(this.ventaService.venta(VentaControllerTests.TEST_VENTA_ID)).willReturn(this.ventaTest);
		Producto producto = new Producto();
		producto.setCode("Pr-test");
		producto.setId(1);
		producto.setStock(15);
		this.productoTest = producto;
		BDDMockito.given(this.productoService.findProductoByCode(this.productoTest.getCode())).willReturn(this.productoTest);
		LineaVenta l = new LineaVenta();
		l.setId(VentaControllerTests.TEST_LINEA_ID);
		l.setVenta(this.ventaTest);
		l.setCantidad(1);
		l.setProducto(producto);
		l.setImporte(1.0);
		l.setTipoTasa(TipoTasa.TSI001);
		this.lineaTest = l;
		BDDMockito.given(this.ventaService.ventaActual()).willReturn(this.ventaTest);
		BDDMockito.given(this.ventaService.newLinea(this.productoTest)).willReturn(this.lineaTest);
		BDDMockito.given(this.ventaService.existelinea(this.productoTest)).willReturn(VentaControllerTests.TEST_LINEA_ID);
		BDDMockito.given(this.ventaService.lineaById(VentaControllerTests.TEST_LINEA_ID)).willReturn(this.lineaTest);
		
	}
	
	//---------------------- Tests linea de venta --------------------------------------
		@WithMockUser(value = "spring", authorities = "farmaceutico")
		@Test
		void testShowLineaEditSuccess() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.get("/ventas/actual/{lineaId}", VentaControllerTests.TEST_LINEA_ID).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("producto")).andExpect(MockMvcResultMatchers.model().attributeExists("editaLinea")).andExpect(MockMvcResultMatchers.view().name("ventas/editarLinea"));
		}
		
		@WithMockUser(value = "spring", authorities = "farmaceutico") // probamos con una id que no exista para la linea
		@Test
		void testShowLineaEditError() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.get("/ventas/actual/{lineaId}", 10).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
		}
	//---------------------- Tests de venta --------------------------------------
		
		@WithMockUser(value = "spring", authorities = "farmaceutico") // vamos a comprobar la primera salida del segundo if de ventaProcessCreation
		@Test
		void testVentaProcessCreationSuccess1() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.post("/ventas/actual").flashAttr("producto", this.productoTest).flashAttr("nuevaLinea", this.lineaTest).with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/ventas/actual/" + VentaControllerTests.TEST_LINEA_ID));
		}
		
		@WithMockUser(value = "spring", authorities = "farmaceutico") // testeamos la finalizacion de una venta
		@Test
		void testfinalizarVentaSuccess() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.get("/ventas/actual/pagar").with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("estupefaciente"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("comprador")).andExpect(MockMvcResultMatchers.view().name("ventas/finalizarVenta"));
		}
		
		@WithMockUser(value = "spring", authorities = "farmaceutico") // vamos a comprobar la segunda salida del segundo if de ventaProcessCreation
		@Test
		void testVentaProcessCreationSuccess2() throws Exception {
			Producto p = new Producto();
			p.setId(2);
			p.setCode("PR-test2");
			BDDMockito.given(this.ventaService.existelinea(p)).willReturn(null);
			BDDMockito.given(this.productoService.findProductoByCode(p.getCode())).willReturn(p);
			this.lineaTest.setId(2);
			this.lineaTest.setProducto(p);
			this.lineaTest.setCantidad(1);
			BDDMockito.given(this.ventaService.newLinea(p)).willReturn(this.lineaTest);
			this.mockMvc.perform(MockMvcRequestBuilders.post("/ventas/actual").flashAttr("producto", p).flashAttr("nuevaLinea", new LineaVenta()).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("producto")).andExpect(MockMvcResultMatchers.model().attribute("producto", Matchers.hasProperty("code", Matchers.is("PR-test2"))))
				.andExpect(MockMvcResultMatchers.model().attributeExists("nuevaLinea")).andExpect(MockMvcResultMatchers.model().attribute("nuevaLinea", Matchers.hasProperty("id", Matchers.is(2))))
				.andExpect(MockMvcResultMatchers.view().name("ventas/ventaActual"));
		}
		
		@WithMockUser(value = "spring", authorities = "farmaceutico") // vamos a comprobar la salida del tercer if de ventaProcessCreation
		@Test
		void testVentaProcessCreationSuccess3() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.post("/ventas/actual").flashAttr("producto", new Producto()).flashAttr("nuevaLinea", this.lineaTest).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("producto")).andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("editaLinea")).andExpect(MockMvcResultMatchers.view().name("ventas/ventaActual"));
		}
		
		@WithMockUser(value = "spring", authorities = "farmaceutico") // por ultimo vamos a comprobar el catch de ventaProcessCreation para ver si lidia con el error
		@Test
		void testVentaProcessCreationError() throws Exception {
			Producto p = new Producto();
			p.setCode("12234");
			BDDMockito.given(this.productoService.findProductoByCode(p.getCode())).willThrow(EntityNotFoundException.class);
			this.mockMvc.perform(MockMvcRequestBuilders.post("/ventas/actual").with(SecurityMockMvcRequestPostProcessors.csrf()).flashAttr("producto", p).flashAttr("nuevaLinea", this.lineaTest)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("errorProducto", Matchers.is("El producto no existe"))).andExpect(MockMvcResultMatchers.view().name("/ventas/ventaActual"));
		}
		

}

package org.springframework.samples.farmatic.web;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.farmatic.configuration.SecurityConfiguration;
import org.springframework.samples.farmatic.model.LineaVenta;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.model.Venta;
import org.springframework.samples.farmatic.model.Venta.EstadoVenta;
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

@WebMvcTest(controllers = PedidoController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class VentaControllerTests {
	
	private static final int	TEST_LINEA_ID	= 1;
	private static final int	TEST_VENTA_ID	= 1;

	@Autowired
	private VentaController	ventaController;

	@MockBean
	private VentaService		ventaService;

	@MockBean
	private ProductoService		productoService;

	@MockBean
	private ClienteService	clienteService;

	@MockBean
	private UserService			userService;

	@Autowired
	private MockMvc				mockMvc;

	private Venta				ventaTest;

	private Producto			productoTest;

	private LineaVenta			lineaTest;
	
	@BeforeEach
	void setup() {
		Venta v = new Venta();
		Collection<LineaVenta> lv = new ArrayList<>();
		v.setEstadoVenta(EstadoVenta.enProceso);
		v.setId(VentaControllerTests.TEST_VENTA_ID);
		v.setLineaVenta(lv);
		this.ventaTest = v;
		BDDMockito.given(this.ventaService.ventaActual()).willReturn(this.ventaTest);
		BDDMockito.given(this.ventaService.venta(VentaControllerTests.TEST_VENTA_ID)).willReturn(this.ventaTest);
		Producto producto = new Producto();
		producto.setCode("Pr-test");
		producto.setId(1);
		this.productoTest = producto;
		BDDMockito.given(this.productoService.findProductoByCode(this.productoTest.getCode())).willReturn(this.productoTest);
		LineaVenta l = new LineaVenta();
		l.setId(VentaControllerTests.TEST_LINEA_ID);
		l.setVenta(this.ventaTest);
		l.setCantidad(1);
		l.setProducto(producto);
		this.lineaTest = l;
		BDDMockito.given(this.ventaService.newLinea(this.productoTest)).willReturn(this.lineaTest);
		BDDMockito.given(this.ventaService.existelinea(this.productoTest)).willReturn(VentaControllerTests.TEST_LINEA_ID);
		BDDMockito.given(this.ventaService.lineaById(VentaControllerTests.TEST_LINEA_ID)).willReturn(this.lineaTest);
		
	}
	
	//---------------------- Tests linea de venta --------------------------------------
		@WithMockUser(value = "spring", authorities = "farmaceutico")
		@Test
		void testShowLineaEditSuccess() throws Exception {
			this.mockMvc.perform(MockMvcRequestBuilders.get("/ventas/actual/{lineaId}", VentaControllerTests.TEST_LINEA_ID).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("producto")).andExpect(MockMvcResultMatchers.view().name("ventas/editarLinea"));
		}

}


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
import org.springframework.samples.farmatic.model.LineaPedido;
import org.springframework.samples.farmatic.model.Pedido;
import org.springframework.samples.farmatic.model.Pedido.EstadoPedido;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.repository.LineaPedidoRepository;
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


	// Recordatorio: no se puede crear como tal un pedido por lo que se comprabará en la función de enviarPedido.
	@BeforeEach
	void setup() {
		Pedido p = new Pedido();
		Collection<LineaPedido> lp = new ArrayList<>();
		p.setCodigo("P-test");
		p.setEstadoPedido(EstadoPedido.Borrador);
		p.setId(PedidoControllerTests.TEST_PEDIDO_ID);
		p.setLineaPedido(lp);
		this.pedidoTest = p;
		BDDMockito.given(this.pedidoService.pedidoActual()).willReturn(this.pedidoTest);
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
		BDDMockito.given(this.pedidoService.newLinea(this.productoTest, 1)).willReturn(this.lineaTest);
		BDDMockito.given(this.pedidoService.existelinea(this.productoTest)).willReturn(PedidoControllerTests.TEST_LINEA_ID);
		BDDMockito.given(this.pedidoService.lineaById(PedidoControllerTests.TEST_LINEA_ID)).willReturn(this.lineaTest);
		
	}

	//---------------------- Tests linea de pedido --------------------------------------
	@WithMockUser(value = "spring", authorities = "farmaceutico")
	@Test
	void testShowLineaEditSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/pedidos/actual/{lineaId}", PedidoControllerTests.TEST_LINEA_ID).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("producto")).andExpect(MockMvcResultMatchers.view().name("pedidos/editarLinea"));
	}
}
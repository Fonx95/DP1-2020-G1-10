package org.springframework.samples.farmatic.web;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.farmatic.configuration.SecurityConfiguration;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.model.TipoMedicamento;
import org.springframework.samples.farmatic.model.TipoProducto;
import org.springframework.samples.farmatic.repository.TipoMedicamentoRepository;
import org.springframework.samples.farmatic.service.ClienteService;
import org.springframework.samples.farmatic.service.FarmaceuticoService;
import org.springframework.samples.farmatic.service.ProductoService;
import org.springframework.samples.farmatic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ProductoController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class ProductoControllerTests {

	private static final int	TEST_TIPO_ID	= 1;
	private static final int	TEST_PRODUCTO_ID	= 1;

	@Autowired
	private ProductoController	productoController;

	

	@MockBean
	private ProductoService		productoService;
	
	@MockBean
	private TipoMedicamentoRepository		tipMedRepository;

	@MockBean
	private ClienteService	clienteService;
	
	@MockBean
	private FarmaceuticoService	farmaceuticoService;

	@MockBean
	private UserService			userService;

	@Autowired
	private MockMvc				mockMvc;

	private Producto			productoTest;
	
	private TipoMedicamento tipMedTest;

	

		@BeforeEach
		void setup() {
			Producto producto = new Producto();
			producto.setCode("PR-001");          
			producto.setName("farmaco1");
			producto.setProductType(TipoProducto.FARMACOSINRECETA);
			producto.setPvp(5.0);
			producto.setPvf(4.0);
			producto.setStock(15);
			producto.setMinStock(5);
			producto.setId(TEST_PRODUCTO_ID);
			TipoMedicamento tm = new TipoMedicamento();
			tm.setId(TEST_TIPO_ID);
			tm.setTipo("Anticoagulante");
			tm.setDescripcion("Son medicamentos que previenen la formación de coágulos sanguíneos. También evitan que los coágulos de sangre ya existentes se hagan más grandes.");
			Collection<Producto> prod = new ArrayList<Producto>();
			List<TipoMedicamento> tip = new ArrayList<TipoMedicamento>();
			prod.add(producto);
			
			producto.setTipoMedicamento(tip);
			tm.setProducto(prod);
			this.productoTest = producto;
			this.tipMedTest = tm;
			BDDMockito.given(this.tipMedRepository.findById(TEST_TIPO_ID)).willReturn(Optional.of(this.tipMedTest));
			BDDMockito.given(this.productoService.findProductoById(TEST_PRODUCTO_ID)).willReturn(this.productoTest);
			BDDMockito.given(this.productoService.findProductoByCode(this.productoTest.getCode())).willReturn(this.productoTest);
			BDDMockito.given(this.productoService.findProductoByCode(this.productoTest.getCode())).willReturn(this.productoTest);
			
			
		}
		
		@WithMockUser(value = "spring", authorities = "farmaceutico")
		@Test
		void testShowProducto() throws Exception {
			
			mockMvc.perform(get("/productos/{idProducto}", TEST_PRODUCTO_ID)).andExpect(status().isOk())
					.andExpect(model().attribute("producto", hasProperty("code", is("PR-001"))))
					.andExpect(model().attribute("producto", hasProperty("name", is("farmaco1"))))
					.andExpect(model().attribute("producto", hasProperty("productType", is(TipoProducto.FARMACOSINRECETA))))
					.andExpect(model().attribute("producto", hasProperty("pvp", is(5.0))))
					.andExpect(model().attribute("producto", hasProperty("pvf", is(4.0))))
					.andExpect(model().attribute("producto", hasProperty("stock", is(15))))
					.andExpect(model().attribute("producto", hasProperty("minStock", is(5))))
					.andExpect(view().name("productos/productoDetails"));
		}
		
		@WithMockUser(value = "spring", authorities = "farmaceutico")
		@Test
		void testNotShowProducto() throws Exception {
			mockMvc.perform(get("/productos/{idProducto}", 999)).andExpect(status().isOk())
					.andExpect(view().name("exception"));
		}
		
		@WithMockUser(value = "spring", authorities = "farmaceutico")
		@Test
		void testShowProductoTipo() throws Exception {
			Collection<Producto> lista = Lists.newArrayList(this.productoTest);
			given(this.productoService.findProductosByTipo(this.tipMedTest)).willReturn(lista);
			
			mockMvc.perform(get("/productos/tipo/{idTipo}", TEST_TIPO_ID)).andExpect(status().isOk())
					.andExpect(model().attributeExists("productos"))
					.andExpect(model().attributeExists("producto"))
					.andExpect(model().attribute("productos", is(lista)))
					.andExpect(view().name("productos/productoList"));
		}
		
		@WithMockUser(value = "spring", authorities = "farmaceutico")
		@Test
		void testNotShowProductoTipo() throws Exception {
			mockMvc.perform(get("/productos/tipo/{idTipo}", 999)).andExpect(status().isOk())
					.andExpect(view().name("exception"));
		}
		
		@WithMockUser(value = "spring", authorities = "farmaceutico")
		@Test
		void testListProducto() throws Exception {
			Collection<Producto> lista = Lists.newArrayList(this.productoTest);
			given(this.productoService.findProducts()).willReturn(lista);
			
			mockMvc.perform(get("/productos/")).andExpect(status().isOk())
					.andExpect(model().attributeExists("productos"))
					.andExpect(model().attributeExists("producto"))
					.andExpect(model().attribute("productos", is(lista)))
					.andExpect(view().name("productos/productoList"));
		}
		
		@WithMockUser(value = "spring", authorities = "farmaceutico")
		@Test
		void testNotListProducto() throws Exception {
			Collection<Producto> lista = Lists.newArrayList();
			given(this.productoService.findProducts()).willReturn(lista);
			
			mockMvc.perform(get("/productos/")).andExpect(status().isOk())
					.andExpect(model().attributeExists("productos"))
					.andExpect(model().attributeExists("producto"))
					.andExpect(model().attribute("productos", is(lista)))
					.andExpect(view().name("productos/productoList"));
		}
		
	@WithMockUser(value = "spring", authorities = "farmaceutico")
    @Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/productos/new")).andExpect(status().isOk())
		.andExpect(model().attributeExists("producto"))
		.andExpect(model().attributeExists("tipoProducto"))
		.andExpect(view().name("productos/createOrUpdateProductoForm"));
	}
	
	@WithMockUser(value = "spring", authorities = "farmaceutico")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/productos/new").param("name", "farmaco2").param("code", "PR-002")
						.with(csrf())
						.param("productType", "FARMACOSINRECETA")
						.param("pvp", "5.0")
						.param("stock", "15")
						.param("pvf", "4.0")
						.param("minStock", "5"))
			.andExpect(status().is3xxRedirection());
	}

	@WithMockUser(value = "spring", authorities = "farmaceutico")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/productos/new").param("name", "").param("code", "")
						.with(csrf())
						.param("productType", "FARMACOSINRECETA")
						.param("pvp", "4.0")
						.param("stock", "-15")
						.param("pvf", "5.0")
						.param("minStock", "5"))
		.andExpect(model().attributeHasErrors("producto"))
		.andExpect(model().attributeHasFieldErrors("producto", "name"))
		.andExpect(model().attributeHasFieldErrors("producto", "pvp"))
		.andExpect(model().attributeHasFieldErrors("producto", "stock"))
		.andExpect(view().name("productos/createOrUpdateProductoForm"));
	}
	
	@WithMockUser(value = "spring", authorities = "farmaceutico")
	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/productos/{idProducto}/edit", TEST_PRODUCTO_ID)).andExpect(status().isOk())
				.andExpect(model().attribute("producto", hasProperty("code", is("PR-001"))))
				.andExpect(model().attribute("producto", hasProperty("name", is("farmaco1"))))
				.andExpect(model().attribute("producto", hasProperty("productType", is(TipoProducto.FARMACOSINRECETA))))
				.andExpect(model().attribute("producto", hasProperty("pvp", is(5.0))))
				.andExpect(model().attribute("producto", hasProperty("pvf", is(4.0))))
				.andExpect(model().attribute("producto", hasProperty("stock", is(15))))
				.andExpect(model().attribute("producto", hasProperty("minStock", is(5))))
				.andExpect(view().name("productos/createOrUpdateProductoForm"));
	}
	
    @WithMockUser(value = "spring", authorities = "farmaceutico")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		mockMvc.perform(post("/productos/{idProducto}/edit", TEST_PRODUCTO_ID)
				.with(csrf())
				.param("id", String.valueOf(TEST_PRODUCTO_ID))
				.param("name", "farmaco1")
				.param("productType", "FARMACOSINRECETA")
				.param("pvp", "7.0")
				.param("stock", "16")
				.param("pvf", "5.0")
				.param("minStock", "4"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/productos/"+ TEST_PRODUCTO_ID));
	}
    
    @WithMockUser(value = "spring")
	@Test
	void testProcessUpdateOwnerFormHasErrors() throws Exception {
		mockMvc.perform(post("/productos/{productId}/edit", TEST_PRODUCTO_ID)
				.with(csrf())
				.param("productType", "FARMACOSINRECETA")
				.param("pvp", "4.0")
				.param("stock", "-15")
				.param("pvf", "5.0")
				.param("minStock", "5"))
				.andExpect(model().attributeHasErrors("producto"))
				.andExpect(model().attributeHasFieldErrors("producto", "name"))
				.andExpect(model().attributeHasFieldErrors("producto", "pvp"))
				.andExpect(model().attributeHasFieldErrors("producto", "stock"))
				.andExpect(view().name("productos/createOrUpdateProductoForm"));
	}
    
		
}

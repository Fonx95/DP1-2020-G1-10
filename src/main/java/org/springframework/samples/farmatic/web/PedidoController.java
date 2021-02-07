
package org.springframework.samples.farmatic.web;

import java.util.Collection;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.farmatic.model.LineaPedido;
import org.springframework.samples.farmatic.model.Pedido;
import org.springframework.samples.farmatic.model.Pedido.EstadoPedido;
import org.springframework.samples.farmatic.model.validator.LineaPedidoValidator;
import org.springframework.samples.farmatic.model.Pedidos;
import org.springframework.samples.farmatic.model.Producto;
import org.springframework.samples.farmatic.model.Proveedor;
import org.springframework.samples.farmatic.model.User;
import org.springframework.samples.farmatic.service.PedidoService;
import org.springframework.samples.farmatic.service.ProductoService;
import org.springframework.samples.farmatic.service.ProveedorService;
import org.springframework.samples.farmatic.service.UserService;
import org.springframework.samples.farmatic.service.exception.EstadoPedidoException;
import org.springframework.samples.farmatic.service.exception.LineaEmptyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class PedidoController {

	private final PedidoService		pedidoService;

	private final ProductoService	productoService;

	private final ProveedorService	proveedorService;

	private final UserService		userService;


	@Autowired
	public PedidoController(final PedidoService pedidoService, final ProductoService productoService, final UserService userService, final ProveedorService proveedorService) {
		this.pedidoService = pedidoService;
		this.productoService = productoService;
		this.userService = userService;
		this.proveedorService = proveedorService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@InitBinder(value = {"nuevaLinea","editaLinea"})
	public void initLineaVentaBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new LineaPedidoValidator());
	}

	/**
	 * Por defecto al llamar a un @GetMapping se devuelve un modelo de {@link Pedido} que corresponde con el pedido actual.
	 * 
	 * @return Un pedido con el estado Borrador
	 */

	@ModelAttribute("pedidoActual")
	public Pedido getPedidoActual() {
		Pedido pedido = this.pedidoService.pedidoActual();
		return pedido;
	}

	//==================== COMO FARMACEUTICO ====================

	/**
	 * Este metodo lista todos los {@link Pedidos} y los muestra en la vista pedidoList.jsp.
	 * 
	 * @param model
	 *            El modelo que trae de la vista
	 */

	@GetMapping(value = {
		"/pedidos"
	})
	public String showListaPedidos(final ModelMap model) {
		Pedidos pedidos = new Pedidos();
		pedidos.getPedidoLista().addAll(this.pedidoService.findPedidos());
		model.addAttribute("pedidos", pedidos);
		PedidoController.log.info("Se han mostrado " + pedidos.getPedidoLista().size() + " pedidos");
		return "pedidos/pedidoList";
	}

	/**
	 * Este metodo es un @GetMapping que, pasandole un Id, devuelve un {@link Pedido} a la vista pedidoDetails.jsp.
	 * Pero si el pedido es de tipo borrador te redirecciona a "pedidos/actual".
	 * 
	 * @param pedido
	 *            El pedido que se contruye al pasarle el id de la vista
	 * @param model
	 *            El modelo que trae de la vista
	 */

	@GetMapping(value = {
		"/pedidos/{id}"
	})
	public String showPedido(@PathVariable("id") final Pedido pedido, final ModelMap model) {
		if (pedido.getEstadoPedido() == EstadoPedido.Borrador) {
			return "redirect:/pedidos/actual";
		} else {
			model.addAttribute("pedido", pedido);
			PedidoController.log.info("Se ha mostrado el pedido con el codigo '" + pedido.getCodigo() + "'");
			return "pedidos/pedidoDetails";
		}
	}

	/**
	 * Este metodo es un @PostMapping en el que se modifica un {@link Pedido} el estado a Recibido.
	 * 
	 * @param pedido
	 *            El pedido que recibe del formulario de la vista
	 * @param result
	 *            Los resultados del Binding
	 * @param model
	 *            El modelo que trae de la vista
	 */

	@PostMapping(value = "/pedidos/{id}")
	public String pedidoRecibido(@ModelAttribute("pedido") final Pedido pedido, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			return "/pedidos/" + pedido.getId();
		} else {
			try {
				this.pedidoService.recibirPedido(pedido);
				PedidoController.log.info("El pedido con el codigo '" + pedido.getCodigo() + "' ha sido recibido");
				return "redirect:/pedidos/" + pedido.getId();
			}catch(EstadoPedidoException ex) {
				model.addAttribute("codigo", "415");
				model.addAttribute("titulo", "Error al procesar el pedido");
				model.addAttribute("descripcion", "No se puede cambiar el estado del pedido de " + pedido.getEstadoPedido() + " a Recibido");
				return "error";
			}
		}
	}

	/**
	 * Este metodo es un @GetMapping que muestra una vista con el pedido actual
	 * 
	 * @param model
	 *            El modelo que trae de la vista
	 */

	@GetMapping(value = {
		"/pedidos/actual"
	})
	public String showPedidoActual(final ModelMap model) {
		Producto producto = new Producto();
		model.addAttribute("producto", producto);
		PedidoController.log.info("Se ha mostrado el pedido actual");
		return "pedidos/pedidoActual";
	}

	/**
	 * Este es un metodo @PostMapping que primero mira si ha recibido un codigo de un {@link Producto} y, si es asi, lo busca en la BD y crea la {@link LineaPedido}, pero si la linea ya exite te redireciona a su vista de edicion.
	 * Si en cambio no recibe un codigo de un {@link Producto} entonces guarda la {@link LineaPedido} en el pedido actual.
	 * 
	 * @param producto
	 *            Un tipo Producto con solo el campo "code" si procede
	 * @param linea
	 *            La linea que trae del fomulario de la vista
	 * @param result
	 *            Los resultado del Binding
	 * @param model
	 *            El modelo que recibe y envia de nuevo a la vista
	 */

	@PostMapping(value = {
		"/pedidos/actual"
	})
	public String pedidoProcessCreation(@ModelAttribute("producto") Producto producto, @ModelAttribute("nuevaLinea") @Valid LineaPedido linea, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("nuevaLinea", linea);
			return "/pedidos/pedidoActual";
		} else if (producto.getCode() != null) {
			try {
				producto = this.productoService.findProductoByCode(producto.getCode());
				if (this.pedidoService.existelinea(producto) != null) {
					PedidoController.log.info("Se ha buscado el producto '" + producto.getCode() + "' - " + producto.getName());
					return "redirect:/pedidos/actual/" + this.pedidoService.existelinea(producto);
				}
				linea = this.pedidoService.newLinea(producto, 1);
				model.addAttribute("nuevaLinea", linea);
				model.addAttribute("producto", producto);
				PedidoController.log.info("Se ha buscado el producto '" + producto.getCode() + "' - " + producto.getName());
				return "pedidos/pedidoActual";
			} catch (EntityNotFoundException ex) {
				model.addAttribute("pedidoActual", this.getPedidoActual());
				model.addAttribute("errorProducto", "El producto no existe");
				return "/pedidos/pedidoActual";
			}
		} else {
			if(linea.getCantidad() == 0) {
				model.remove("nuevaLinea");
				return "/pedidos/pedidoActual";
			}else {
				this.pedidoService.saveLinea(linea);
				model.remove("nuevaLinea");
				model.addAttribute("producto", producto);
				PedidoController.log.info("Se ha guardado la linea con el producto '" + linea.getProducto().getCode() + "' en el pedido borrador");
				return "pedidos/pedidoActual";
			}
		}
	}

	/**
	 * Este es un metodo @GetMapping que muestra una vista preparada para editar una {@link LineaPedido}
	 * 
	 * @param linea
	 *            La linea de pedido que recibe de la vista y devuelve de nuevo
	 * @param model
	 *            El modelo que recibe de la vista y devuelve de nuevo
	 */

	@GetMapping(value = {
		"/pedidos/actual/{lineaId}"
	})
	public String showLineaEdit(@PathVariable("lineaId") final int idlinea, final ModelMap model) {
		Producto producto = new Producto();
		LineaPedido linea=this.pedidoService.lineaById(idlinea);
		model.addAttribute("producto", producto);
		model.addAttribute("editaLinea", linea);
		PedidoController.log.info("Se ha mostrado la linea con id " + linea.getId() + " para ser modificada");
		return "pedidos/editarLinea";
	}

	/**
	 * Este es un metodo @PostMapping que primero mira si se ha introducido un codigo de {@link Producto} y lo redirecciona
	 * al metodo que buscaria el producto. Si no es asi miraria se ha actualizado la linea con cantidad 0 la eliminaria de la BD.
	 * Si no es ninguna de las anteriores actualizaria la {@link LineaPedido} con la nueva cantidad introducida
	 * 
	 * @param producto
	 *            Un tipo Producto con solo el campo "code" si procede
	 * @param linea
	 *            La linea que recibe del fomulario de la vista que quiere modificar la cantidad
	 * @param result
	 *            Los resultados del Binding
	 * @param model
	 *            El modelo que recibe de la vista
	 */

	@PostMapping(value = {
		"/pedidos/actual/{lineaId}"
	})
	public String lineaEdit(@ModelAttribute("producto") final Producto producto, @ModelAttribute("editaLinea") @Valid final LineaPedido linea, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			model.addAttribute("editaLinea", linea);
			return "/pedidos/editarLinea";
		} else if (producto.getCode() != null) {
			return this.pedidoProcessCreation(producto, linea, result, model);
		} else if (linea.getCantidad() == 0) {
			this.pedidoService.deleteLinea(linea);
			PedidoController.log.info("La linea con id: '" + linea.getId() + "' se ha eliminado");
			return "redirect:/pedidos/actual";
		} else {
			this.pedidoService.saveLinea(linea);
			PedidoController.log.info("La linea con id: '" + linea.getId() + "' se ha modificado");
			return "redirect:/pedidos/actual";
		}
	}

	/**
	 * Este es un metodo @GetMapping que muestra la vista de detalles del pedido actual para para seleccionar un {@link Proveedor} y pedir el {@link Pedido}
	 * 
	 * @param model
	 *            El modelo que recibe de la vista y devuelve de nuevo
	 */

	@GetMapping(value = {
		"/pedidos/actual/pedir"
	})
	public String sendPedido(final ModelMap model) {
		Collection<Proveedor> proveedores = this.proveedorService.findProveedores();
		Proveedor proveedor = new Proveedor();
		model.addAttribute("proveedores", proveedores);
		model.addAttribute("proveedor", proveedor);
		PedidoController.log.info("Se han mostrado los detalles del pedido actual para enviarse a un proveedor");
		return "pedidos/enviarPedido";
	}

	/**
	 * Este es un metodo @PostMapping que recibe un {@link Proveedor} seleccionado en la vista y cambiaria el estado del {@link Pedido} borrador a pedido
	 * 
	 * @param proveedor
	 *            Un proveedor que recibe de la vista
	 * @param result
	 *            Los resultados del Binding
	 * @param model
	 *            El modelo que recibe de la vista
	 */

	@PostMapping(value = {
		"/pedidos/actual/pedir"
	})
	public String createPedido(@Valid final Proveedor proveedor, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			return "pedidos/enviarPedido";
		} else {
			try {
				this.pedidoService.enviarPedido(proveedor);
				PedidoController.log.info("El pedido borrador se ha pedido al proveedor " + proveedor.getEmpresa());
				return "redirect:/pedidos";
			}catch(LineaEmptyException ex) {
				result.reject("lineaEmpty", ex.getMessage());
				model.addAttribute("errors", result.getAllErrors());
				return "pedidos/pedidoActual";
			}
			
		}
	}

	//==================== COMO PROVEEDOR ====================

	/**
	 * Este es un metodo @GetMapping que muestra los {@link Pedidos} del {@link Proveedor} que ha iniciado sesion en el sistema
	 * 
	 * @param model
	 *            El modelo que recibe de la vista y devuelve de nuevo
	 */

	@GetMapping(value = {
		"/proveedor"
	})
	public String miListaPedidos(final ModelMap model) {
		Pedidos pedidos = new Pedidos();
		User user = this.userService.getCurrentUser();
		pedidos.getPedidoLista().addAll(this.proveedorService.findPedidosProveedor(user));
		model.addAttribute("pedidos", pedidos);
		PedidoController.log.info("Se ha mostrado " + pedidos.getPedidoLista().size() + " pedidos del proveedor " + user.getUsername());
		return "pedidos/pedidoList";
	}

	/**
	 * Este es un metodo @GetMapping que muestra los detalles de un {@link Pedido} recibido por el {@link Proveedor} que ha iniciado sesion en el sistema
	 * 
	 * @param pedidoId
	 *            Id de un pedido
	 * @param model
	 *            El modelo que recibe de la vista y devuelve de nuevo
	 * @return
	 */

	@GetMapping(value = {
		"/proveedor/{id}"
	})
	public String miPedido(@PathVariable("id") final int pedidoId, final ModelMap model) {
		Pedido pedido = this.pedidoService.pedido(pedidoId);
		model.addAttribute("pedido", pedido);
		PedidoController.log.info("Se ha mostrado el pedido con el codigo '" + pedido.getCodigo() + "'");
		return "pedidos/pedidoDetails";
	}

	/**
	 * Este es un metodo @PostMapping que cambia el estado de un {@link Pedido} a enviado por parte de un {@link Proveedor} que ha inicado sesion en el sistema
	 * 
	 * @param pedido
	 *            Un pedido que recibe del formulario de la vista
	 * @param result
	 *            Los resultados del Binding
	 * @param model
	 *            El modelo que recibe de la vista y devuelve de nuevo
	 */

	@PostMapping(value = "/proveedor/{id}")
	public String pedidoEnviado(@ModelAttribute("pedido") final Pedido pedido, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			return "pedidos/pedidoDetails";
		} else {
			try {
				this.pedidoService.pedidoEnviado(pedido);
				PedidoController.log.info("El pedido con el codigo: '" + pedido.getCodigo() + "' ha sido cambiado a Enviado");
				return "redirect:/proveedor/" + pedido.getId();
			}catch(EstadoPedidoException ex) {
				model.addAttribute("codigo", "414");
				model.addAttribute("titulo", "Error al procesar el pedido");
				model.addAttribute("descripcion", "No se puede cambiar el estado del pedido de " + pedido.getEstadoPedido() + " a Enviado");
				return "error";
			}
			
		}
	}

}

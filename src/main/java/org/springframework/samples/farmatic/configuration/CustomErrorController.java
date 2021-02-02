package org.springframework.samples.farmatic.configuration;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CustomErrorController implements ErrorController {

	@Override
	public String getErrorPath() {
		return "/error";
	}
	
	@RequestMapping("/error")
	public ModelAndView handleError(HttpServletRequest request) {
		ModelAndView modelView = new ModelAndView("error");
		
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		
		if(status != null) {
			int statusCode = Integer.parseInt(status.toString());
			
			if(statusCode == HttpStatus.NOT_FOUND.value()) {
				modelView.addObject("codigo", "404");
				modelView.addObject("titulo", "No hay pagina aqui");
				modelView.addObject("descripcion", "Lo sentimos, pero la página solicitada no está disponible en Farmatic.");
				return modelView;
			}else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				modelView.addObject("codigo", "500");
				modelView.addObject("titulo", "Ups!");
				modelView.addObject("descripcion", "Lo sentimos, pero nuestro servidor no funciona bien. Estamos trabajando en esto ahora mismo.");
				return modelView;
			}else if(statusCode == HttpStatus.FORBIDDEN.value()) {
				modelView.addObject("codigo", "403");
				modelView.addObject("titulo", "¡Vete!");
				modelView.addObject("descripcion", "Lo sentimos, pero no se le permite acceder a la página que solicitó.");
				return modelView;
			}
		}
		modelView.addObject("titulo", "Algo ha pasado...");
		modelView.addObject("descripcion", "Un fallo desconocido te ha llevado hasta aqui");
		return modelView;
	}
}

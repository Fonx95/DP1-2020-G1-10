package org.springframework.samples.farmatic.web;

import org.springframework.samples.farmatic.model.Producto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ProductController {
	
	@GetMapping("/products")
	public ModelAndView showProducts() {
		ModelAndView mav = new ModelAndView("Producto/productDetails");
		
		return mav;
		
	}

}

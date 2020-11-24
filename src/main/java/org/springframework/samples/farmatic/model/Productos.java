package org.springframework.samples.farmatic.model;

import java.util.ArrayList;
import java.util.List;

public class Productos {
	
	private List<Producto> productos;
	
	public List<Producto> getProductoLista(){
		if(productos == null) {
			productos = new ArrayList<>();
		}
		return productos;
	}

}

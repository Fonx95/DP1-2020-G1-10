package org.springframework.samples.farmatic.model;

import java.util.ArrayList;
import java.util.List;

public class Pedidos {
	
	private List<Pedido> pedidos;
	
	public List<Pedido> getPedidoLista(){
		if(pedidos == null) {
			pedidos = new ArrayList<>();
		}
		return pedidos;
	}

}

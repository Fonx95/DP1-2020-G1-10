package org.springframework.samples.farmatic.service.exception;

public class VentaClienteEmptyException extends Exception {
	
	public VentaClienteEmptyException() {
		super("Si el pago es menor al importe total se debe asignar a un cliente registrado");
	}
	
}

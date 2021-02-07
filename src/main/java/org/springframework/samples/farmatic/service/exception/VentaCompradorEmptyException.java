package org.springframework.samples.farmatic.service.exception;

public class VentaCompradorEmptyException extends Exception{
	
	public VentaCompradorEmptyException() {
		super("Si existe un producto estupefaciente se debe registrar al comprador");
	}
	
}

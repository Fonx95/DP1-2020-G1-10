package org.springframework.samples.farmatic.service.exception;

public class LineaEmptyException extends Exception {
	
	public LineaEmptyException(String clase) {
		super(clase + " tiene que tener al menos 1 linea de " + clase);
	}
}

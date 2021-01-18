package org.springframework.samples.farmatic.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
@Entity
@Table(name = "comprador")
public class Comprador extends NamedEntity{
	
	@Column(name = "apellidos")
	@NotBlank
	private String apellidos;
	
	@Column(name = "dni")
	@NotEmpty
	private String dni;
	
	@OneToOne()
	@JoinColumn(name = "venta_id")
	private Venta venta;

}

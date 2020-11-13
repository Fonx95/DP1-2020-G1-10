package org.springframework.samples.farmatic.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;


@Entity
@Table(name = "proveedores")
public class Proveedor extends  NamedEntity{
	@Column(name = "empresa")
	@NotBlank
	private String				empresa;
	@Column(name = "direccion")
	@NotBlank
	private String				direccion;
	@Column(name = "cif")
	@NotBlank
	private String				cif;
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "username", referencedColumnName = "username")
	private User user;
	
	
}

package org.springframework.samples.farmatic.model;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
@Entity
@Table(name = "proveedors")
public class Proveedor extends  BaseEntity{
	
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
	
	@OneToMany(mappedBy = "proveedor", fetch = FetchType.LAZY)
	private Collection<Pedido> pedido;
	
}

package org.springframework.samples.farmatic.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User{
	
	@Id
	String username;
	
	String password;
	
	boolean enabled;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
	private Set<Authorities> authorities;
	
	@OneToOne(cascade = CascadeType.PERSIST, mappedBy = "user", fetch = FetchType.LAZY)
	private Cliente cliente;
	
	@OneToOne(cascade = CascadeType.PERSIST, mappedBy = "user", fetch = FetchType.LAZY)
	private Proveedor proveedor;
	
	@OneToOne(cascade = CascadeType.PERSIST, mappedBy = "user", fetch = FetchType.LAZY)
	private Farmaceutico farmaceutico;
}

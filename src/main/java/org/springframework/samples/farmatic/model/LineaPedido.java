package org.springframework.samples.farmatic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data

public class LineaPedido extends BaseEntity{
	
	@Column(name = "cantidad")
	@NotNull
	@Min(0)
	private Integer cantidad;
	
	@ManyToOne(optional = false)
	private Producto producto;
	

}

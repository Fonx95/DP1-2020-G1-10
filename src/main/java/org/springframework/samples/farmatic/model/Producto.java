package org.springframework.samples.farmatic.model;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Range;
import org.springframework.samples.farmatic.model.Producto.tipoProducto;

import lombok.Data;

/**
 * Simple JavaBean domain object representing an person.
 *
 * @author Ken Krebs
 */
@Data
@Entity

public class Producto {
	
	@Column(name = "name")
	@NotEmpty
	private String name;
	
	@Column(name = "code")
	@NotEmpty
	private String code;
	
	@Column(name = "product_type")
	private tipoProducto productType;
	
	@Column(name = "pvp")
	@NotEmpty
	@Min(0)
	private Double pvp;
	
	@Column(name = "pvf")
	@NotEmpty
	@Min(0)
	private Double pvf;
	
	@Column(name = "stock")
	@NotEmpty
	@Min(0)
	private Integer stock;
	
	@Column(name = "min_stock")
	@Min(0)
	private Integer minStock;
	
	@ManyToMany
	private Collection<TipoMedicamento> tipoMedicamento;

	public enum tipoProducto {
		
		FármacoConReceta, FármacoSinReceta, Estupefaciente, Parafarmacia;

	}

}

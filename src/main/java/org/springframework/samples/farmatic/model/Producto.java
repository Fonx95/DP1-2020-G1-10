
package org.springframework.samples.farmatic.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

/**
 * Simple JavaBean domain object representing an person.
 *
 * @author Ken Krebs
 */
@Data
@Entity

public class Producto extends NamedEntity {

	@Column(name = "code")
	@NotEmpty
	private String			code;

	@Column(name = "product_type")
	@Enumerated(EnumType.STRING)
	private TipoProducto	productType;

	@Column(name = "pvp")
	@Min(0)
	private Double			pvp;

	@Column(name = "pvf")
	@Min(0)
	private Double			pvf;

	@Column(name = "stock")
	@Min(0)
	private Integer			stock;

	@Column(name = "min_stock")
	@Min(0)
	private Integer			minStock;
	
	@OneToMany()
	private Collection<LineaPedido> lineaPedido;

}

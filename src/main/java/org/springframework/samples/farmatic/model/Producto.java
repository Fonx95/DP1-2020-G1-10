
package org.springframework.samples.farmatic.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "productos")
public class Producto extends NamedEntity {

	@Column(name = "code")
	//@NotEmpty
	private String			code;

	@Column(name = "product_type")
	@Enumerated(EnumType.STRING)
	private TipoProducto	productType;

	@Column(name = "pvp")
	//@Min(0)
	private Double			pvp;

	@Column(name = "pvf")
	//@Min(0)
	private Double			pvf;

	@Column(name = "stock")
	//@Min(0)
	private Integer			stock;

	@Column(name = "min_stock")
	//@Min(0)
	private Integer			minStock;
	
	@JoinTable(
			name = "rel_productos_tipoMedicamentos",
			joinColumns = @JoinColumn(name = "Id_producto", nullable = false),
			inverseJoinColumns = @JoinColumn(name = "Id_tipo_medicamento", nullable = false))
	@ManyToMany(fetch = FetchType.LAZY)
	private List<TipoMedicamento> tipoMedicamento;
	
	public void sumaStock(Integer suma) {
		this.stock += suma;
	}
}

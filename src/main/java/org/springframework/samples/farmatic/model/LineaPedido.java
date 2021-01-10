
package org.springframework.samples.farmatic.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
@Table(name = "lineaPedidos")
public class LineaPedido extends BaseEntity {

	@Column(name = "cantidad")
	@NotNull
	@Min(0)
	private Integer cantidad;
	
	@ManyToOne()
	@JoinColumn(name = "pedido_id")
	private Pedido pedido;
	
	@ManyToOne()
	@JoinColumn(name = "producto_id"/*, referencedColumnName = "id"*/)
	private Producto producto;
	
	public void addProducto(Producto producto) {
		this.producto = producto;
	}

	public void addPedido(final Pedido pedido) {
		this.pedido = pedido;
	}
}

package org.springframework.samples.farmatic.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import lombok.Data;

@Data
@Entity
@Table(name = "lineaVenta")
public class LineaVenta extends BaseEntity{
	
	@Column(name = "tipoTasa")
	@Enumerated(EnumType.STRING)
	private TipoTasa tipoTasa;
	
	@Column(name = "Cantidad")
	@Min(0)
	private Integer cantidad;
	
	@Column(name = "Importe")
	private Double importe;
	
	@ManyToOne()
	@JoinColumn(name = "venta_id")
	private Venta venta;
	
	@ManyToOne()
	@JoinColumn(name = "producto_id")
	private Producto producto;
	
	public void addProducto(Producto producto) {
		this.producto = producto;
	}
	
	public void addVenta(Venta venta) {
		this.venta = venta;
	}

}

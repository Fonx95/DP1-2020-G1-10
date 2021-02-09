package org.springframework.samples.farmatic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.lang.NonNull;

import lombok.Data;

@Data
@Entity
@Table(name = "lineaVenta")
public class LineaVenta extends BaseEntity{
	
	@Column(name = "tipoTasa")
	@Enumerated(EnumType.STRING)
	private TipoTasa tipoTasa;
	
	@Column(name = "cantidad")
	//@Min(0)
	//@NonNull
	private Integer cantidad;
	
	@Column(name = "importe")
	private Double importe;
	
	@ManyToOne()
	@JoinColumn(name = "venta_id")
	private Venta venta;
	
	@ManyToOne()
	@JoinColumn(name = "producto_id")
	private Producto producto;
	
	public LineaVenta(Producto producto, Venta venta) {
		this.cantidad = 1;
		this.producto = producto;
		this.venta = venta;
	}
	public LineaVenta() {
	}
}

package org.springframework.samples.farmatic.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import lombok.Data;

@Data
@Entity
@Table(name = "lineaVentas")
public class LineaVenta extends BaseEntity{
	
	@Column(name = "tipoTasa")
	private TipoTasa tipoTasa;
	
	@Column(name = "Cantidad")
	@Min(0)
	private Integer cantidad;
	
	@Column(name = "Importe")
	private Double importe;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "venta_id")
	private Venta venta;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "producto_id", referencedColumnName = "id")
	private Producto producto;
	
	public void addProducto(Producto producto) {
		this.producto = producto;
		//producto.getLineaVenta().add(this);
	}
	
	public void addVenta(Venta venta) {
		this.venta = venta;
	}

}

package org.springframework.samples.farmatic.model;

import java.time.LocalDate;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.farmatic.model.Pedido.EstadoPedido;

import lombok.Data;

@Data
@Entity
@Table(name = "ventas")
public class Venta extends BaseEntity{
	
	@Column(name = "fecha")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate fecha;
	
	@Column(name = "importe_total")
	@Min(0)
	private Double importeTotal;
	
	@Column(name = "pagado")
	@Min(0)
	private Double pagado;
	
	@Column(name = "por_pagar")
	private Double porPagar;
	
	@Column(name = "estado")
	@NotNull
	private EstadoVenta estadoVenta;
	
	@OneToMany(mappedBy = "venta", fetch = FetchType.LAZY)
	private Collection<LineaVenta> lineaVenta;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "venta", fetch = FetchType.LAZY)
	private Collection<Comprador> comprador;
	
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "cliente_id", referencedColumnName = "id")
	private Cliente cliente;
	
	public void addLinea(LineaVenta linea) {
		getLineaVenta().add(linea);
	}
	
	public void deleteLinea(LineaVenta linea) {
		getLineaVenta().remove(linea);
	}
	
	public enum EstadoVenta {
		enProceso, Realizada;
	}
	
	public Venta() {
		this.estadoVenta = EstadoVenta.enProceso;
	}

}

package org.springframework.samples.farmatic.model;

import java.time.LocalDate;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "venta")
public class Venta extends BaseEntity{
	
	@Column(name = "fecha")
	@NotNull
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate fecha;
	
	@Column(name = "importe total")
	@Min(0)
	private Double importeTotal;
	
	@Column(name = "pagado")
	@Min(0)
	private Double pagado;
	
	@Column(name = "por pagar")
	private Double porPagar;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "venta", fetch = FetchType.LAZY)
	private Collection<LineaVenta> lineaVenta;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "venta", fetch = FetchType.LAZY)
	private Collection<Comprador> comprador;
	
	public void addLinea(LineaVenta linea) {
		getLineaVenta().add(linea);
	}
	
	public void deleteLinea(LineaVenta linea) {
		getLineaVenta().remove(linea);
	}

}

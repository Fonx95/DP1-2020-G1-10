
package org.springframework.samples.farmatic.model;

import java.time.LocalDate;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "pedidos")
public class Pedido extends BaseEntity {

	@Version
	private Integer					version;

	@Column(name = "codigo")
	@NotEmpty
	private String					codigo;

	@Column(name = "fecha_pedido")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate				fechaPedido;

	@Column(name = "fecha_entrega")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate				fechaEntrega;

	@Column(name = "Estado")
	@NotNull
	private EstadoPedido			estadoPedido;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "proveedor_id", referencedColumnName = "id")
	private Proveedor				proveedor;

	@OneToMany(mappedBy = "pedido", fetch = FetchType.LAZY)
	private Collection<LineaPedido>	lineaPedido;


	public enum EstadoPedido {
		Enviado, Recibido, Borrador, Pedido;
	}


	public void addLinea(final LineaPedido linea) {
		this.getLineaPedido().add(linea);
	}

	public void deleteLinea(final LineaPedido linea) {
		this.getLineaPedido().remove(linea);
	}

	public Pedido() {
		this.codigo = "";
		this.estadoPedido = EstadoPedido.Borrador;
	}
}

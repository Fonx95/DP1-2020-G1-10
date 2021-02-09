package org.springframework.samples.farmatic.model;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "Tipo_medicamentos")
public class TipoMedicamento extends BaseEntity {
	
	@Column(name = "tipo")
	@NotNull
	private String tipo;
	
	@Column(name = "descripcion", length = 1000)
	private String descripcion;
	
	@ManyToMany(mappedBy = "tipoMedicamento")
	private Collection<Producto> producto;
}

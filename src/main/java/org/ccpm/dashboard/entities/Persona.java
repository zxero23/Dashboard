package org.ccpm.dashboard.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.ccpm.dashboard.enums.TipoPersona;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "personas")
@Inheritance(strategy = InheritanceType.JOINED)
public class Persona implements Serializable {
	
	private static final long serialVersionUID = -8577005100354412160L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "documento", unique = true)
	private Long documento;
	@Column(name = "apellido_nombre_razon_social")
	private String apellidoNombreRazonSocial;
	@Column(name = "fecha_nacimiento")
	private Date fechaNacimiento;
	private String nacionalidad;
	@Column(length = 300)
	private String telefono;
	@OneToOne
	private Municipio municipio;
	@Column(length = 500)
	private String domicilio;
	@Column(name="tipo_persona", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TipoPersona tipoPersona;
	@Column(length = 2000)
	private String correo;
	@Column(name="forma_juridica")
	private String formaJuridica;
	
}

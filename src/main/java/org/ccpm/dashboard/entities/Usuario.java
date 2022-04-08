package org.ccpm.dashboard.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

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
@Table(name = "usuarios")
public class Usuario implements Serializable {

	private static final long serialVersionUID = 4350713783600728470L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne
	private Persona persona;
	@Column(name = "nombre_usuario")
	private String nombreUsuario;
	@Column(name = "clave_acceso")
	private String claveAcceso;
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	@Column(name = "fecha_alta")
	private Date fechaAlta;
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	@Column(name = "fecha_modificacion")
	private Date fechaModificacion;
	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	@Column(name = "ultimo_login")
	private Date ultimoLogin;
	@OneToOne
	private Rol rol;
	@Column(length = 5000)
	private String foto;
	@Column(name = "content_type")
	private String contentType;
	@Column(length = 5000)
	private String token;
	private boolean estado;

	

}

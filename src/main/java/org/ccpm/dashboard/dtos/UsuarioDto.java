package org.ccpm.dashboard.dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDto {

	private Long id;
	private PersonaDto persona;
	private String nombreUsuario;
	private String claveAcceso;
	private Date fechaAlta;
	private Date fechaModificacion;
	private Date ultimoLogin;
	private RolDto rol;
	private String foto;
	private String contentType;
	private String token;
	private boolean estado;
	
	

}

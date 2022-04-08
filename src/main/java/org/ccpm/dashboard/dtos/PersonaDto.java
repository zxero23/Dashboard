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
public class PersonaDto {
	
	private Long id;
	private Long documento;
	private String apellidoNombreRazonSocial;
	private Date fechaNacimiento;
	private String nacionalidad;
	private String telefono;
	private String correo;
	private MunicipioDto municipio;
	private String domicilio;
	
	

}

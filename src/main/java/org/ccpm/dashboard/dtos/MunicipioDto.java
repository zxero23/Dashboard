package org.ccpm.dashboard.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString(exclude = "departamento")
@NoArgsConstructor
@AllArgsConstructor
public class MunicipioDto {

	private Long id;
	private String nombre;
	private String codigoPostal;
	private DepartamentoDto departamento;
	private boolean estado;
}

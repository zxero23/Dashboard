package org.ccpm.dashboard.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class MetasDto {

	private Long id;
	private String nombre;	
	private Integer nro;	
	private String Descripcion;	
	private boolean estado;
}

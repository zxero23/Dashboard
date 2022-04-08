package org.ccpm.dashboard.dtos;

import java.util.List;

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
public class RolDto {
	private Long id;
	private String nombre;
	private String descripcion;
	private List<PermisoDto> permisos;
	private List<MenuDto> menus;
	private boolean estado;
}

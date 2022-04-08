package org.ccpm.dashboard.dtos;

import java.util.List;

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
@ToString
@EqualsAndHashCode(exclude = "menus")
@NoArgsConstructor
@AllArgsConstructor
public class MenuDto {
	private Long id;
	private String nombre;
	private String icono;
	private String link;
	private boolean estado;
	private MenuDto menu;
	private List<MenuDto> menus;
	private Integer orden;
}

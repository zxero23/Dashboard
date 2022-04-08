package org.ccpm.dashboard.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
@Table(name = "menus")
public class Menu implements Serializable {

	private static final long serialVersionUID = -1515778435654507591L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nombre;
	private String icono;
	private String link;
	private boolean estado;
	@ManyToOne
	private Menu menu;
	@OneToMany(mappedBy = "menu")
	private Set<Menu> menus;
	private Integer orden;

}

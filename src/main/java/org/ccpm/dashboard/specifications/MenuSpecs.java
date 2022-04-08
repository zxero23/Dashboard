package org.ccpm.dashboard.specifications;

import org.ccpm.dashboard.entities.Menu;
import org.springframework.data.jpa.domain.Specification;

public class MenuSpecs {
	public static Specification<Menu> getMenuByNombre(String nombre) {
		return (root, query, criteriaBuilder) -> {
			return criteriaBuilder.like(root.get("nombre"), "%" + nombre.toUpperCase() + "%");
		};
	}

	public static Specification<Menu> getByEstado(boolean estado) {
		return (root, query, criteriaBuilder) -> {
			return criteriaBuilder.equal(root.get("estado"), estado);
		};
	}
}

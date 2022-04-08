package org.ccpm.dashboard.specifications;

import org.ccpm.dashboard.entities.Permiso;
import org.springframework.data.jpa.domain.Specification;

public class PermisoSpecs {

	public static Specification<Permiso> getPermisoByNombre(String nombre) {
		return (root, query, criteriaBuilder) -> {
			return criteriaBuilder.like(root.get("nombre"), "%" + nombre.toUpperCase() + "%");
		};
	}

	public static Specification<Permiso> getByEstado(boolean estado) {
		return (root, query, criteriaBuilder) -> {
			return criteriaBuilder.equal(root.get("estado"), estado);
		};
	}
}

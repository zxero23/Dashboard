package org.ccpm.dashboard.specifications;

import org.ccpm.dashboard.entities.Rol;
import org.springframework.data.jpa.domain.Specification;

public class RolSpecs {
	public static Specification<Rol> getRolByNombre(String nombre) {
		return (root, query, criteriaBuilder) -> {
			return criteriaBuilder.like(root.get("nombre"), "%" + nombre.toUpperCase() + "%");
		};
	}

	public static Specification<Rol> getByEstado(boolean estado) {
		return (root, query, criteriaBuilder) -> {
			return criteriaBuilder.equal(root.get("estado"), estado);
		};
	}
}

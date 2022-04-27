package org.ccpm.dashboard.specifications;

import org.ccpm.dashboard.entities.Mesa;
import org.springframework.data.jpa.domain.Specification;

public class MesaSpecs {
	public static Specification<Mesa> getMesaByNombre(String nombre) {
		return (root, query, criteriaBuilder) -> {
			return criteriaBuilder.like(root.get("nombre"), "%" + nombre.toUpperCase() + "%");
		};
	}

	public static Specification<Mesa> getByEstado(boolean estado) {
		return (root, query, criteriaBuilder) -> {
			return criteriaBuilder.equal(root.get("estado"), estado);
		};
	}


}

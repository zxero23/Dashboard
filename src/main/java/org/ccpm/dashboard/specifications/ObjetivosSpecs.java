package org.ccpm.dashboard.specifications;

import org.ccpm.dashboard.entities.Objetivos;
import org.springframework.data.jpa.domain.Specification;

public class ObjetivosSpecs {
	public static Specification<Objetivos> getObjetivosByNombre(String nombre) {
		return (root, query, criteriaBuilder) -> {
			return criteriaBuilder.like(root.get("nombre"), "%" + nombre.toUpperCase() + "%");
		};
	}

	public static Specification<Objetivos> getByEstado(boolean estado) {
		return (root, query, criteriaBuilder) -> {
			return criteriaBuilder.equal(root.get("estado"), estado);
		};
	}


}

package org.ccpm.dashboard.specifications;

import org.ccpm.dashboard.entities.Metas;
import org.springframework.data.jpa.domain.Specification;

public class MetasSpecs {
	public static Specification<Metas> getMetasByNombre(String nombre) {
		return (root, query, criteriaBuilder) -> {
			return criteriaBuilder.like(root.get("nombre"), "%" + nombre.toUpperCase() + "%");
		};
	}

	public static Specification<Metas> getByEstado(boolean estado) {
		return (root, query, criteriaBuilder) -> {
			return criteriaBuilder.equal(root.get("estado"), estado);
		};
	}


}

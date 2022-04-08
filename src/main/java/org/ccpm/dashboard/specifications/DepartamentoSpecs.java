package org.ccpm.dashboard.specifications;

import org.ccpm.dashboard.entities.Departamento;
import org.springframework.data.jpa.domain.Specification;

public class DepartamentoSpecs {
	public static Specification<Departamento> getDepartamentoByNombre(String nombre) {
		return (root, query, criteriaBuilder) -> {
			return criteriaBuilder.like(root.get("nombre"), "%" + nombre.toUpperCase() + "%");
		};
	}

	public static Specification<Departamento> getByEstado(boolean estado) {
		return (root, query, criteriaBuilder) -> {
			return criteriaBuilder.equal(root.get("estado"), estado);
		};
	}
}

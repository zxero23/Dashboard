package org.ccpm.dashboard.specifications;

import javax.persistence.criteria.Join;

import org.ccpm.dashboard.entities.Departamento;
import org.ccpm.dashboard.entities.Municipio;
import org.springframework.data.jpa.domain.Specification;

public class MunicipioSpecs {
	public static Specification<Municipio> getMunicipioByNombre(String nombre) {
		return (root, query, criteriaBuilder) -> {
			return criteriaBuilder.like(root.get("nombre"), "%" + nombre.toUpperCase() + "%");
		};
	}

	public static Specification<Municipio> getByEstado(boolean estado) {
		return (root, query, criteriaBuilder) -> {
			return criteriaBuilder.equal(root.get("estado"), estado);
		};
	}

	public static Specification<Municipio> getMunicipioByDepartamento(Long idDepartamento) {
		return (root, query, criteriaBuilder) -> {
			if (idDepartamento != null) {
				Join<Municipio, Departamento> join = root.join("departamento");
				return criteriaBuilder.equal(join.get("id"), idDepartamento);
			} else {
				return criteriaBuilder.conjunction();
			}

		};
	}
}

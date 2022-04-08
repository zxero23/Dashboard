package org.ccpm.dashboard.specifications;

import org.ccpm.dashboard.entities.Persona;
import org.springframework.data.jpa.domain.Specification;

public class PersonaSpecs {
	
	public static Specification<Persona> getPersonaByApellidoNombreRazonSocial(String apellidoNombreRazonSocial) {
		return (root, query, criteriaBuilder) -> {
			return criteriaBuilder.like(root.get("apellidoNombreRazonSocial"), "%" + apellidoNombreRazonSocial.toUpperCase() + "%");
		};
	}

	public static Specification<Persona> getByEstado(boolean estado) {
		return (root, query, criteriaBuilder) -> {
			return criteriaBuilder.equal(root.get("estado"), estado);
		};
	}

}

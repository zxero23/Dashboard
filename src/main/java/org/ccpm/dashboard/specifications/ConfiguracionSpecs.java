package org.ccpm.dashboard.specifications;

import org.ccpm.dashboard.entities.Configuracion;
import org.springframework.data.jpa.domain.Specification;

public class ConfiguracionSpecs {

	public static Specification<Configuracion> getConfiguracionByLLave(String llave) {
		return (root, query, criteriaBuilder) -> {
			if (llave != null && !llave.isBlank()) {
				return criteriaBuilder.like(root.get("llave"), "%" + llave + "%");
			} else {
				return criteriaBuilder.conjunction();
			}
		};
	}

	public static Specification<Configuracion> getConfiguracionByValor(String valor) {
		return (root, query, criteriaBuilder) -> {
			if (valor != null && !valor.isBlank()) {
				return criteriaBuilder.like(root.get("valor"), "%" + valor + "%");
			} else {
				return criteriaBuilder.conjunction();
			}
		};
	}

	public static Specification<Configuracion> getByEstado(boolean estado) {
		return (root, query, criteriaBuilder) -> {
			return criteriaBuilder.equal(root.get("estado"), estado);
		};
	}
}

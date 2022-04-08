package org.ccpm.dashboard.specifications;

import javax.persistence.criteria.Join;

import org.ccpm.dashboard.entities.Persona;
import org.ccpm.dashboard.entities.Rol;
import org.ccpm.dashboard.entities.Usuario;
import org.springframework.data.jpa.domain.Specification;

public class UsuarioSpecs {

	public static Specification<Usuario> getUsuarioByApellidoNombre(String apellidoNombre) {
		return (root, query, criteriaBuilder) -> {
			if (apellidoNombre != null && !apellidoNombre.isBlank()) {
				Join<Usuario, Persona> join = root.join("persona");
				return criteriaBuilder.like(join.get("apellidoNombreRazonSocial"), "%" + apellidoNombre.toUpperCase() + "%");
			} else {
				return criteriaBuilder.conjunction();
			}

		};
	}

	public static Specification<Usuario> getUsuarioByNombreUsuario(String nombreUsuario) {
		return (root, query, criteriaBuilder) -> {
			if (nombreUsuario != null && !nombreUsuario.isBlank()) {
				return criteriaBuilder.like(root.get("nombreUsuario"), "%" + nombreUsuario + "%");
			} else {
				return criteriaBuilder.conjunction();
			}

		};
	}

	public static Specification<Usuario> getUsuarioByRol(Long idRol) {
		return (root, query, criteriaBuilder) -> {
			if (idRol != null && !idRol.equals(0L)) {
				Join<Usuario, Rol> join = root.join("rol");
				return criteriaBuilder.equal(join.get("id"), idRol);
			} else {
				return criteriaBuilder.conjunction();
			}

		};
	}

	public static Specification<Usuario> getByEstado(boolean estado) {
		return (root, query, criteriaBuilder) -> {
			Join<Usuario, Persona> join = root.join("persona");
			query.orderBy(criteriaBuilder.asc(join.get("apellidoNombreRazonSocial")));
			return criteriaBuilder.equal(root.get("estado"), estado);
		};
	}
}

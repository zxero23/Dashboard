package org.ccpm.dashboard.repositories;

import java.util.Optional;

import org.ccpm.dashboard.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

	Optional<Usuario> findByNombreUsuarioAndClaveAcceso(String nombreUsuario, String claveAcceso);
	
	Optional<Usuario> findByNombreUsuario(String nombreUsuario);
}

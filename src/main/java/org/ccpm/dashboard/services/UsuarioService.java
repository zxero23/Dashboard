package org.ccpm.dashboard.services;

import java.util.List;

import org.ccpm.dashboard.dtos.UsuarioDto;
import org.ccpm.dashboard.entities.Usuario;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface UsuarioService {

	List<UsuarioDto> findAll(Specification<Usuario> spec, Pageable pageable);

	UsuarioDto find(Long id);
	
	UsuarioDto findByUsuario(String usuario);

	UsuarioDto create(UsuarioDto usuarioDto) throws Exception;

	UsuarioDto edit(UsuarioDto usuarioDto) throws Exception;

	UsuarioDto remove(Long id) throws Exception;

	Long count(Specification<Usuario> spec);

	UsuarioDto login(String nombreUsuario, String claveAcceso) throws Exception;
}

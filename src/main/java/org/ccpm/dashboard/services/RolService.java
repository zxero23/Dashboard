package org.ccpm.dashboard.services;

import java.util.List;

import org.ccpm.dashboard.dtos.RolDto;
import org.ccpm.dashboard.entities.Rol;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface RolService {
	List<RolDto> findAll(Specification<Rol> spec, Pageable pageable);

	RolDto find(Long id);

	RolDto create(RolDto rolDto);

	RolDto edit(RolDto rolDto);

	RolDto remove(Long id);

	Long count(Specification<Rol> spec);
}

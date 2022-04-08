package org.ccpm.dashboard.services;

import java.util.List;

import org.ccpm.dashboard.dtos.PermisoDto;
import org.ccpm.dashboard.entities.Permiso;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface PermisoService {

	List<PermisoDto> findAll(Specification<Permiso> spec, Pageable pageable);

	PermisoDto find(Long id);

	PermisoDto create(PermisoDto permisoDto);

	PermisoDto edit(PermisoDto permisoDto);

	PermisoDto remove(Long id);

	Long count(Specification<Permiso> spec);
}

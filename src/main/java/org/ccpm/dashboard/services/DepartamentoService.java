package org.ccpm.dashboard.services;

import java.util.List;

import org.ccpm.dashboard.dtos.DepartamentoDto;
import org.ccpm.dashboard.entities.Departamento;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface DepartamentoService {
	List<DepartamentoDto> findAll(Specification<Departamento> spec, Pageable pageable);

	DepartamentoDto find(Long id);

	DepartamentoDto create(DepartamentoDto departamentoDto);

	DepartamentoDto edit(DepartamentoDto departamentoDto);

	DepartamentoDto remove(Long id);

	Long count(Specification<Departamento> spec);
}

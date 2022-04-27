package org.ccpm.dashboard.services;

import java.util.List;

import org.ccpm.dashboard.dtos.ObjetivosDto;
import org.ccpm.dashboard.entities.Objetivos;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface ObjetivosService {

	List<ObjetivosDto> findAll(Specification<Objetivos> spec, Pageable pageable);

	ObjetivosDto find(Long id);

	ObjetivosDto create(ObjetivosDto objetivosDto);

	ObjetivosDto edit(ObjetivosDto objetivosDto);

	ObjetivosDto remove(Long id);

	Long count(Specification<Objetivos> spec);
}

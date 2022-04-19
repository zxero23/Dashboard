package org.ccpm.dashboard.services;

import java.util.List;

import org.ccpm.dashboard.dtos.MetasDto;
import org.ccpm.dashboard.entities.Metas;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface MetasService {

	List<MetasDto> findAll(Specification<Metas> spec, Pageable pageable);

	MetasDto find(Long id);

	MetasDto create(MetasDto metasDto);

	MetasDto edit(MetasDto metasDto);

	MetasDto remove(Long id);

	Long count(Specification<Metas> spec);
}

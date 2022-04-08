package org.ccpm.dashboard.services;

import java.util.List;

import org.ccpm.dashboard.dtos.MunicipioDto;
import org.ccpm.dashboard.entities.Municipio;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface MunicipioService {

	List<MunicipioDto> findAll(Specification<Municipio> spec, Pageable pageable);

	MunicipioDto find(Long id);

	MunicipioDto create(MunicipioDto municipioDto);

	MunicipioDto edit(MunicipioDto municipioDto);

	MunicipioDto remove(Long id);

	Long count(Specification<Municipio> spec);
}

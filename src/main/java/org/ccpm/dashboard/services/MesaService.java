package org.ccpm.dashboard.services;

import java.util.List;

import org.ccpm.dashboard.dtos.MesaDto;
import org.ccpm.dashboard.entities.Mesa;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface MesaService {

	List<MesaDto> findAll(Specification<Mesa> spec, Pageable pageable);

	MesaDto find(Long id);

	MesaDto create(MesaDto mesasDto);

	MesaDto edit(MesaDto mesasDto);

	MesaDto remove(Long id);

	Long count(Specification<Mesa> spec);
}

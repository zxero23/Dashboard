package org.ccpm.dashboard.services;

import java.util.List;

import org.ccpm.dashboard.dtos.ConfiguracionDto;
import org.ccpm.dashboard.entities.Configuracion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface ConfiguracionService {
	ConfiguracionDto findByLlave(String llave);

	List<ConfiguracionDto> findAll(Specification<Configuracion> spec, Pageable pageable);

	ConfiguracionDto find(Long id);

	ConfiguracionDto create(ConfiguracionDto configuracionDto);

	ConfiguracionDto edit(ConfiguracionDto configuracionDto);

	ConfiguracionDto remove(Long id);

	Long count(Specification<Configuracion> spec);
}

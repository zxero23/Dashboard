package org.ccpm.dashboard.services;

import java.util.List;

import org.ccpm.dashboard.dtos.PersonaDto;
import org.ccpm.dashboard.entities.Persona;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface PersonaService {

	List<PersonaDto> findAll();
	
	List<PersonaDto> findAll(Specification<Persona> spec, Pageable pageable);

	PersonaDto find(Long id);

	PersonaDto create(PersonaDto personaDto);

	PersonaDto edit(PersonaDto personaDto);

	PersonaDto remove(Long id);

	Long count(Specification<Persona> spec);
}

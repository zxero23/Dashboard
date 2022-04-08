package org.ccpm.dashboard.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.ccpm.dashboard.dtos.PersonaDto;
import org.ccpm.dashboard.entities.Persona;
import org.ccpm.dashboard.exceptions.RecordNotFoundException;
import org.ccpm.dashboard.repositories.PersonaRepository;
import org.ccpm.dashboard.services.PersonaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PersonaServiceImpl implements PersonaService {

	@Autowired
	PersonaRepository personaRepository;

	@Override
	public List<PersonaDto> findAll(Specification<Persona> spec, Pageable pageable) {
		return personaRepository.findAll(spec, pageable).getContent().stream().map(this::convertToDto)
				.collect(Collectors.toList());
	}

	private PersonaDto convertToDto(Persona Persona) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(Persona, PersonaDto.class);
	}

	private Persona convertToEntity(PersonaDto PersonaDto) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(PersonaDto, Persona.class);
	}

	@Override
	public PersonaDto create(PersonaDto personaDto) {
		Persona persona = this.convertToEntity(personaDto);
		return this.convertToDto(this.personaRepository.saveAndFlush(persona));
	}

	@Override
	public PersonaDto edit(PersonaDto personaDto) {
		Persona persona = this.personaRepository.getById(personaDto.getId());
		if (persona != null) {
			return this.convertToDto(this.personaRepository.saveAndFlush(persona));
		} else {
			throw new RecordNotFoundException("No se encontró persona con id: " + personaDto.getId());
		}

	}

	@Override
	public PersonaDto remove(Long id) {
		Persona persona = this.personaRepository.getById(id);
		if (persona != null) {
			return this.convertToDto(this.personaRepository.saveAndFlush(persona));
		} else {
			throw new RecordNotFoundException("No se encontró persona con id: " + id);
		}
	}

	@Override
	public PersonaDto find(Long id) {
		Persona persona = this.personaRepository.getById(id);
		if (persona != null) {
			return this.convertToDto(persona);
		} else {
			throw new RecordNotFoundException("No se encontró persona con id: " + id);
		}
	}

	@Override
	public Long count(Specification<Persona> spec) {
		return personaRepository.count(spec);
	}

	@Override
	public List<PersonaDto> findAll() {
		return this.personaRepository.findAll().stream().map(this::convertToDto)
				.collect(Collectors.toList());
	}

}

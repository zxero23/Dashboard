package org.ccpm.dashboard.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.ccpm.dashboard.dtos.ObjetivosDto;
import org.ccpm.dashboard.entities.Objetivos;
import org.ccpm.dashboard.exceptions.RecordNotFoundException;
import org.ccpm.dashboard.repositories.ObjetivosRepository;
import org.ccpm.dashboard.services.ObjetivosService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ObejtivosServiceImpl implements ObjetivosService {

	@Autowired
	ObjetivosRepository objetivosRepository;

	@Override
	public List<ObjetivosDto> findAll(Specification<Objetivos> spec, Pageable pageable) {
		return objetivosRepository.findAll(spec, pageable).getContent().stream().map(this::convertToDto)
				.collect(Collectors.toList());
	}

	private ObjetivosDto convertToDto(Objetivos Objetivos) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(Objetivos, ObjetivosDto.class);
	}

	private Objetivos convertToEntity(ObjetivosDto ObjetivosDto) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(ObjetivosDto, Objetivos.class);
	}

	@Override
	public ObjetivosDto create(ObjetivosDto objetivosDto) {
		objetivosDto.setEstado(true);
		Objetivos objetivos = this.convertToEntity(objetivosDto);
		return this.convertToDto(this.objetivosRepository.saveAndFlush(objetivos));
	}

	@Override
	public ObjetivosDto edit(ObjetivosDto objetivosDto) {
		Objetivos objetivos = this.objetivosRepository.getById(objetivosDto.getId());
		if (objetivos != null) {
			objetivos.setNombre(objetivosDto.getNombre());
			return this.convertToDto(this.objetivosRepository.saveAndFlush(objetivos));
		} else {
			throw new RecordNotFoundException("No se encontró objetivos con id: " + objetivosDto.getId());
		}

	}

	@Override
	public ObjetivosDto remove(Long id) {
		Objetivos objetivos = this.objetivosRepository.getById(id);
		if (objetivos != null) {
			objetivos.setEstado(false);
			return this.convertToDto(this.objetivosRepository.saveAndFlush(objetivos));
		} else {
			throw new RecordNotFoundException("No se encontró objetivos con id: " + id);
		}
	}

	@Override
	public ObjetivosDto find(Long id) {
		Objetivos objetivos = this.objetivosRepository.getById(id);
		if (objetivos != null) {
			return this.convertToDto(objetivos);
		} else {
			throw new RecordNotFoundException("No se encontró objetivos con id: " + id);
		}
	}

	@Override
	public Long count(Specification<Objetivos> spec) {
		return objetivosRepository.count(spec);
	}

}

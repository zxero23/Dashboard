package org.ccpm.dashboard.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.ccpm.dashboard.dtos.MetasDto;
import org.ccpm.dashboard.entities.Metas;
import org.ccpm.dashboard.exceptions.RecordNotFoundException;
import org.ccpm.dashboard.repositories.MetasRepository;
import org.ccpm.dashboard.services.MetasService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class MetasServiceImpl implements MetasService {

	@Autowired
	MetasRepository metasRepository;

	@Override
	public List<MetasDto> findAll(Specification<Metas> spec, Pageable pageable) {
		return metasRepository.findAll(spec, pageable).getContent().stream().map(this::convertToDto)
				.collect(Collectors.toList());
	}

	private MetasDto convertToDto(Metas Metas) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(Metas, MetasDto.class);
	}

	private Metas convertToEntity(MetasDto MetasDto) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(MetasDto, Metas.class);
	}

	@Override
	public MetasDto create(MetasDto metasDto) {
		metasDto.setEstado(true);
		Metas metas = this.convertToEntity(metasDto);
		return this.convertToDto(this.metasRepository.saveAndFlush(metas));
	}

	@Override
	public MetasDto edit(MetasDto metasDto) {
		Metas metas = this.metasRepository.getById(metasDto.getId());
		if (metas != null) {
			metas.setNombre(metasDto.getNombre());
			return this.convertToDto(this.metasRepository.saveAndFlush(metas));
		} else {
			throw new RecordNotFoundException("No se encontró metas con id: " + metasDto.getId());
		}

	}

	@Override
	public MetasDto remove(Long id) {
		Metas metas = this.metasRepository.getById(id);
		if (metas != null) {
			metas.setEstado(false);
			return this.convertToDto(this.metasRepository.saveAndFlush(metas));
		} else {
			throw new RecordNotFoundException("No se encontró metas con id: " + id);
		}
	}

	@Override
	public MetasDto find(Long id) {
		Metas metas = this.metasRepository.getById(id);
		if (metas != null) {
			return this.convertToDto(metas);
		} else {
			throw new RecordNotFoundException("No se encontró metas con id: " + id);
		}
	}

	@Override
	public Long count(Specification<Metas> spec) {
		return metasRepository.count(spec);
	}

}

package org.ccpm.dashboard.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.ccpm.dashboard.dtos.MunicipioDto;
import org.ccpm.dashboard.entities.Municipio;
import org.ccpm.dashboard.exceptions.RecordNotFoundException;
import org.ccpm.dashboard.repositories.MunicipioRepository;
import org.ccpm.dashboard.services.MunicipioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class MunicipioServiceImpl implements MunicipioService {

	@Autowired
	MunicipioRepository municipioRepository;

	@Override
	public List<MunicipioDto> findAll(Specification<Municipio> spec, Pageable pageable) {
		return municipioRepository.findAll(spec, pageable).getContent().stream().map(this::convertToDto)
				.collect(Collectors.toList());
	}

	private MunicipioDto convertToDto(Municipio Municipio) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(Municipio, MunicipioDto.class);
	}

	private Municipio convertToEntity(MunicipioDto MunicipioDto) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(MunicipioDto, Municipio.class);
	}

	@Override
	public MunicipioDto create(MunicipioDto municipioDto) {
		municipioDto.setEstado(true);
		Municipio municipio = this.convertToEntity(municipioDto);
		return this.convertToDto(this.municipioRepository.saveAndFlush(municipio));
	}

	@Override
	public MunicipioDto edit(MunicipioDto municipioDto) {
		Municipio municipio = this.municipioRepository.getById(municipioDto.getId());
		if (municipio != null) {
			municipio.setNombre(municipioDto.getNombre());
			return this.convertToDto(this.municipioRepository.saveAndFlush(municipio));
		} else {
			throw new RecordNotFoundException("No se encontró municipio con id: " + municipioDto.getId());
		}

	}

	@Override
	public MunicipioDto remove(Long id) {
		Municipio municipio = this.municipioRepository.getById(id);
		if (municipio != null) {
			municipio.setEstado(false);
			return this.convertToDto(this.municipioRepository.saveAndFlush(municipio));
		} else {
			throw new RecordNotFoundException("No se encontró municipio con id: " + id);
		}
	}

	@Override
	public MunicipioDto find(Long id) {
		Municipio municipio = this.municipioRepository.getById(id);
		if (municipio != null) {
			return this.convertToDto(municipio);
		} else {
			throw new RecordNotFoundException("No se encontró municipio con id: " + id);
		}
	}

	@Override
	public Long count(Specification<Municipio> spec) {
		return municipioRepository.count(spec);
	}

}

package org.ccpm.dashboard.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.ccpm.dashboard.dtos.ConfiguracionDto;
import org.ccpm.dashboard.entities.Configuracion;
import org.ccpm.dashboard.exceptions.RecordNotFoundException;
import org.ccpm.dashboard.repositories.ConfiguracionRepository;
import org.ccpm.dashboard.services.ConfiguracionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ConfiguracionServiceImpl implements ConfiguracionService {

	@Autowired
	ConfiguracionRepository configuracionRepository;

	@Override
	public ConfiguracionDto findByLlave(String llave) {
		Configuracion configuracion = this.configuracionRepository.findByLlave(llave).orElse(null);
		return this.convertToDto(configuracion);
	}

	private ConfiguracionDto convertToDto(Configuracion configuracion) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(configuracion, ConfiguracionDto.class);
	}

	private Configuracion convertToEntity(ConfiguracionDto grupoDto) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(grupoDto, Configuracion.class);
	}

	@Override
	public List<ConfiguracionDto> findAll(Specification<Configuracion> spec, Pageable pageable) {
		return configuracionRepository.findAll(spec, pageable).getContent().stream().map(this::convertToDto)
				.collect(Collectors.toList());
	}

	@Override
	public ConfiguracionDto create(ConfiguracionDto configuracionDto) {
		configuracionDto.setEstado(true);
		Configuracion configuracion = this.convertToEntity(configuracionDto);
		return this.convertToDto(this.configuracionRepository.saveAndFlush(configuracion));
	}

	@Override
	public ConfiguracionDto edit(ConfiguracionDto configuracionDto) {
		Configuracion configuracion = this.configuracionRepository.getById(configuracionDto.getId());
		if (configuracion != null) {
			return this.convertToDto(this.configuracionRepository.saveAndFlush(this.convertToEntity(configuracionDto)));
		} else {
			throw new RecordNotFoundException("No se encontró configuracion con id: " + configuracionDto.getId());
		}

	}

	@Override
	public ConfiguracionDto remove(Long id) {
		Configuracion configuracion = this.configuracionRepository.getById(id);
		if (configuracion != null) {
			configuracion.setEstado(false);
			return this.convertToDto(this.configuracionRepository.saveAndFlush(configuracion));
		} else {
			throw new RecordNotFoundException("No se encontró configuracion con id: " + id);
		}
	}

	@Override
	public ConfiguracionDto find(Long id) {
		Configuracion configuracion = this.configuracionRepository.getById(id);
		if (configuracion != null) {
			return this.convertToDto(configuracion);
		} else {
			throw new RecordNotFoundException("No se encontró configuracion con id: " + id);
		}
	}

	@Override
	public Long count(Specification<Configuracion> spec) {
		return configuracionRepository.count(spec);
	}
}

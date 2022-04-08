package org.ccpm.dashboard.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.ccpm.dashboard.dtos.PermisoDto;
import org.ccpm.dashboard.entities.Permiso;
import org.ccpm.dashboard.exceptions.RecordNotFoundException;
import org.ccpm.dashboard.repositories.PermisoRepository;
import org.ccpm.dashboard.services.PermisoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PermisoServiceImpl implements PermisoService {

	@Autowired
	PermisoRepository permisoRepository;

	private PermisoDto convertToDto(Permiso permiso) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(permiso, PermisoDto.class);
	}

	private Permiso convertToEntity(PermisoDto grupoDto) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(grupoDto, Permiso.class);
	}

	@Override
	public List<PermisoDto> findAll(Specification<Permiso> spec, Pageable pageable) {
		return permisoRepository.findAll(spec, pageable).getContent().stream().map(this::convertToDto)
				.collect(Collectors.toList());
	}

	@Override
	public PermisoDto create(PermisoDto permisoDto) {
		permisoDto.setEstado(true);
		Permiso permiso = this.convertToEntity(permisoDto);
		return this.convertToDto(this.permisoRepository.saveAndFlush(permiso));
	}

	@Override
	public PermisoDto edit(PermisoDto permisoDto) {
		Permiso permiso = this.permisoRepository.getById(permisoDto.getId());
		if (permiso != null) {
			return this.convertToDto(this.permisoRepository.saveAndFlush(this.convertToEntity(permisoDto)));
		} else {
			throw new RecordNotFoundException("No se encontró permiso con id: " + permisoDto.getId());
		}

	}

	@Override
	public PermisoDto remove(Long id) {
		Permiso permiso = this.permisoRepository.getById(id);
		if (permiso != null) {
			permiso.setEstado(false);
			return this.convertToDto(this.permisoRepository.saveAndFlush(permiso));
		} else {
			throw new RecordNotFoundException("No se encontró permiso con id: " + id);
		}
	}

	@Override
	public PermisoDto find(Long id) {
		Permiso permiso = this.permisoRepository.getById(id);
		if (permiso != null) {
			return this.convertToDto(permiso);
		} else {
			throw new RecordNotFoundException("No se encontró permiso con id: " + id);
		}
	}

	@Override
	public Long count(Specification<Permiso> spec) {
		return permisoRepository.count(spec);
	}
}
package org.ccpm.dashboard.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.ccpm.dashboard.dtos.RolDto;
import org.ccpm.dashboard.entities.Rol;
import org.ccpm.dashboard.exceptions.RecordNotFoundException;
import org.ccpm.dashboard.repositories.RolRepository;
import org.ccpm.dashboard.services.RolService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class RolServiceImpl implements RolService {

	@Autowired
	RolRepository rolRepository;

	private RolDto convertToDto(Rol rol) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(rol, RolDto.class);
	}

	private Rol convertToEntity(RolDto grupoDto) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(grupoDto, Rol.class);
	}

	@Override
	public List<RolDto> findAll(Specification<Rol> spec, Pageable pageable) {
		return rolRepository.findAll(spec, pageable).getContent().stream().map(this::convertToDto)
				.collect(Collectors.toList());
	}

	@Override
	public RolDto create(RolDto rolDto) {
		rolDto.setEstado(true);
		Rol rol = this.convertToEntity(rolDto);
		return this.convertToDto(this.rolRepository.saveAndFlush(rol));
	}

	@Override
	public RolDto edit(RolDto rolDto) {
		Rol rol = this.rolRepository.getById(rolDto.getId());
		if (rol != null) {
			return this.convertToDto(this.rolRepository.saveAndFlush(this.convertToEntity(rolDto)));
		} else {
			throw new RecordNotFoundException("No se encontró rol con id: " + rolDto.getId());
		}

	}

	@Override
	public RolDto remove(Long id) {
		Rol rol = this.rolRepository.getById(id);
		if (rol != null) {
			rol.setEstado(false);
			return this.convertToDto(this.rolRepository.saveAndFlush(rol));
		} else {
			throw new RecordNotFoundException("No se encontró rol con id: " + id);
		}
	}

	@Override
	public RolDto find(Long id) {
		Rol rol = this.rolRepository.getById(id);
		if (rol != null) {
			return this.convertToDto(rol);
		} else {
			throw new RecordNotFoundException("No se encontró rol con id: " + id);
		}
	}

	@Override
	public Long count(Specification<Rol> spec) {
		return rolRepository.count(spec);
	}
}
package org.ccpm.dashboard.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.ccpm.dashboard.dtos.DepartamentoDto;
import org.ccpm.dashboard.entities.Departamento;
import org.ccpm.dashboard.exceptions.RecordNotFoundException;
import org.ccpm.dashboard.repositories.DepartamentoRepository;
import org.ccpm.dashboard.services.DepartamentoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class DepartamentoServiceImpl implements DepartamentoService {

	@Autowired
	DepartamentoRepository departamentoRepository;

	@Override
	public List<DepartamentoDto> findAll(Specification<Departamento> spec, Pageable pageable) {
		return departamentoRepository.findAll(spec, pageable).getContent().stream().map(this::convertToDto)
				.collect(Collectors.toList());
	}

	private DepartamentoDto convertToDto(Departamento Departamento) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(Departamento, DepartamentoDto.class);
	}

	private Departamento convertToEntity(DepartamentoDto DepartamentoDto) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(DepartamentoDto, Departamento.class);
	}

	@Override
	public DepartamentoDto create(DepartamentoDto departamentoDto) {
		departamentoDto.setEstado(true);
		Departamento departamento = this.convertToEntity(departamentoDto);
		return this.convertToDto(this.departamentoRepository.saveAndFlush(departamento));
	}

	@Override
	public DepartamentoDto edit(DepartamentoDto departamentoDto) {
		Departamento departamento = this.departamentoRepository.getById(departamentoDto.getId());
		if (departamento != null) {
			departamento.setNombre(departamentoDto.getNombre());
			return this.convertToDto(this.departamentoRepository.saveAndFlush(departamento));
		} else {
			throw new RecordNotFoundException("No se encontró departamento con id: " + departamentoDto.getId());
		}

	}

	@Override
	public DepartamentoDto remove(Long id) {
		Departamento departamento = this.departamentoRepository.getById(id);
		if (departamento != null) {
			departamento.setEstado(false);
			return this.convertToDto(this.departamentoRepository.saveAndFlush(departamento));
		} else {
			throw new RecordNotFoundException("No se encontró departamento con id: " + id);
		}
	}

	@Override
	public DepartamentoDto find(Long id) {
		Departamento departamento = this.departamentoRepository.getById(id);
		if (departamento != null) {
			return this.convertToDto(departamento);
		} else {
			throw new RecordNotFoundException("No se encontró departamento con id: " + id);
		}
	}

	@Override
	public Long count(Specification<Departamento> spec) {
		return departamentoRepository.count(spec);
	}

}

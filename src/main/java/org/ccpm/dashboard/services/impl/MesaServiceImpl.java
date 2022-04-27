package org.ccpm.dashboard.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.ccpm.dashboard.dtos.MesaDto;
import org.ccpm.dashboard.entities.Mesa;
import org.ccpm.dashboard.exceptions.RecordNotFoundException;
import org.ccpm.dashboard.repositories.MesaRepository;
import org.ccpm.dashboard.services.MesaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class MesaServiceImpl implements MesaService {

	@Autowired
	MesaRepository mesaRepository;

	@Override
	public List<MesaDto> findAll(Specification<Mesa> spec, Pageable pageable) {
		return mesaRepository.findAll(spec, pageable).getContent().stream().map(this::convertToDto)
				.collect(Collectors.toList());
	}

	private MesaDto convertToDto(Mesa Mesa) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(Mesa, MesaDto.class);
	}

	private Mesa convertToEntity(MesaDto MesaDto) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(MesaDto, Mesa.class);
	}

	@Override
	public MesaDto create(MesaDto mesaDto) {
		mesaDto.setEstado(true);
		Mesa mesa = this.convertToEntity(mesaDto);
		return this.convertToDto(this.mesaRepository.saveAndFlush(mesa));
	}

	@Override
	public MesaDto edit(MesaDto mesaDto) {
		Mesa mesa = this.mesaRepository.getById(mesaDto.getId());
		if (mesa != null) {
			mesa.setNombre(mesaDto.getNombre());
			return this.convertToDto(this.mesaRepository.saveAndFlush(mesa));
		} else {
			throw new RecordNotFoundException("No se encontró mesa con id: " + mesaDto.getId());
		}

	}

	@Override
	public MesaDto remove(Long id) {
		Mesa mesa = this.mesaRepository.getById(id);
		if (mesa != null) {
			mesa.setEstado(false);
			return this.convertToDto(this.mesaRepository.saveAndFlush(mesa));
		} else {
			throw new RecordNotFoundException("No se encontró mesa con id: " + id);
		}
	}

	@Override
	public MesaDto find(Long id) {
		Mesa mesa = this.mesaRepository.getById(id);
		if (mesa != null) {
			return this.convertToDto(mesa);
		} else {
			throw new RecordNotFoundException("No se encontró mesa con id: " + id);
		}
	}

	@Override
	public Long count(Specification<Mesa> spec) {
		return mesaRepository.count(spec);
	}

}

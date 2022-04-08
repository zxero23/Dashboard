package org.ccpm.dashboard.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.ccpm.dashboard.dtos.MenuDto;
import org.ccpm.dashboard.entities.Menu;
import org.ccpm.dashboard.exceptions.RecordNotFoundException;
import org.ccpm.dashboard.repositories.MenuRepository;
import org.ccpm.dashboard.services.MenuService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class MenuServiceImpl implements MenuService {

	@Autowired
	MenuRepository menuRepository;

	@Override
	public List<MenuDto> findAll(Specification<Menu> spec, Pageable pageable) {
		return menuRepository.findAll(spec, pageable).getContent().stream().map(this::convertToDto)
				.collect(Collectors.toList());
	}

	private MenuDto convertToDto(Menu Menu) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(Menu, MenuDto.class);
	}

	private Menu convertToEntity(MenuDto MenuDto) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(MenuDto, Menu.class);
	}

	@Override
	public MenuDto create(MenuDto menuDto) {
		menuDto.setEstado(true);
		Menu menu = this.convertToEntity(menuDto);
		return this.convertToDto(this.menuRepository.saveAndFlush(menu));
	}

	@Override
	public MenuDto edit(MenuDto menuDto) {
		Menu menu = this.menuRepository.getById(menuDto.getId());
		if (menu != null) {
			menu.setNombre(menuDto.getNombre());
			return this.convertToDto(this.menuRepository.saveAndFlush(menu));
		} else {
			throw new RecordNotFoundException("No se encontró menu con id: " + menuDto.getId());
		}

	}

	@Override
	public MenuDto remove(Long id) {
		Menu menu = this.menuRepository.getById(id);
		if (menu != null) {
			menu.setEstado(false);
			return this.convertToDto(this.menuRepository.saveAndFlush(menu));
		} else {
			throw new RecordNotFoundException("No se encontró menu con id: " + id);
		}
	}

	@Override
	public MenuDto find(Long id) {
		Menu menu = this.menuRepository.getById(id);
		if (menu != null) {
			return this.convertToDto(menu);
		} else {
			throw new RecordNotFoundException("No se encontró menu con id: " + id);
		}
	}

	@Override
	public List<MenuDto> findAll(boolean estado) {
		return this.menuRepository.findByEstado(estado).stream().map(this::convertToDto).collect(Collectors.toList());
	}

	@Override
	public Long count(Specification<Menu> spec) {
		return menuRepository.count(spec);
	}

}
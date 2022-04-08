package org.ccpm.dashboard.services;

import java.util.List;

import org.ccpm.dashboard.dtos.MenuDto;
import org.ccpm.dashboard.entities.Menu;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface MenuService {
	List<MenuDto> findAll(Specification<Menu> spec, Pageable pageable);

	MenuDto find(Long id);

	List<MenuDto> findAll(boolean estado);

	MenuDto create(MenuDto menuDto);

	MenuDto edit(MenuDto menuDto);

	MenuDto remove(Long id);

	Long count(Specification<Menu> spec);
}

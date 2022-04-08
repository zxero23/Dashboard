package org.ccpm.dashboard.repositories;

import java.util.List;

import org.ccpm.dashboard.entities.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MenuRepository extends JpaRepository<Menu, Long>, JpaSpecificationExecutor<Menu> {

	List<Menu> findByEstado(boolean estado);

}

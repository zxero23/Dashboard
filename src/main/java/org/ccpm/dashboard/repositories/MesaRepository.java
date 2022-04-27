package org.ccpm.dashboard.repositories;

import org.ccpm.dashboard.entities.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MesaRepository extends JpaRepository<Mesa, Long>, JpaSpecificationExecutor<Mesa> {

}

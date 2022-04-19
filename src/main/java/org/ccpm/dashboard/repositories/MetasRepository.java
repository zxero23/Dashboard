package org.ccpm.dashboard.repositories;

import org.ccpm.dashboard.entities.Metas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MetasRepository extends JpaRepository<Metas, Long>, JpaSpecificationExecutor<Metas> {

}

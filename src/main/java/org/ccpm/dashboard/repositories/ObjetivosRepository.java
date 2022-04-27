package org.ccpm.dashboard.repositories;

import org.ccpm.dashboard.entities.Objetivos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ObjetivosRepository extends JpaRepository<Objetivos, Long>, JpaSpecificationExecutor<Objetivos> {

}

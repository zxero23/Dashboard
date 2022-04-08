package org.ccpm.dashboard.repositories;

import java.util.Optional;

import org.ccpm.dashboard.entities.Configuracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ConfiguracionRepository
		extends JpaRepository<Configuracion, Long>, JpaSpecificationExecutor<Configuracion> {
	Optional<Configuracion> findByLlave(String llave);
}

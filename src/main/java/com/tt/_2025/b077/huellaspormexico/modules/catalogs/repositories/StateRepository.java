package com.tt._2025.b077.huellaspormexico.modules.catalogs.repositories;

import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.StateCatalog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StateRepository extends JpaRepository<StateCatalog, Long> {
    Optional<StateCatalog> findByState(String state);
}

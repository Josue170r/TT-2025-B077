package com.tt._2025.b077.huellaspormexico.modules.catalogs.repositories;

import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.CategoryPlacesCatalog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryPlacesRepository extends JpaRepository<CategoryPlacesCatalog, Long> {
    List<CategoryPlacesCatalog> findAllByIsActiveTrue();
}

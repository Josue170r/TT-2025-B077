package com.tt._2025.b077.huellaspormexico.modules.places.reporsitories;

import com.tt._2025.b077.huellaspormexico.modules.places.entities.PlaceTypes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceTypesRepository extends JpaRepository<PlaceTypes, Long> {
    Optional<PlaceTypes> findByType(String type);
}

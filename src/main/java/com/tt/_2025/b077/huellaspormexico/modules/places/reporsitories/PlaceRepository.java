package com.tt._2025.b077.huellaspormexico.modules.places.reporsitories;

import com.tt._2025.b077.huellaspormexico.modules.places.entities.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findByPlaceId(String placeId);
    Page<Place> findByIdIn(List<Long> ids, Pageable pageable);
    List<Place> findAllByPlaceIdIn(List<String> placeIds);
}

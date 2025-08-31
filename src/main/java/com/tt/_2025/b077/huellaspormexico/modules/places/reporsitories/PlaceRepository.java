package com.tt._2025.b077.huellaspormexico.modules.places.reporsitories;

import com.tt._2025.b077.huellaspormexico.modules.places.entities.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findByPlaceId(String placeId);
    Optional<Place> findByName(String name);
    Page<Place> findByIdIn(List<Long> ids, Pageable pageable);
    List<Place> findAllByPlaceIdIn(List<String> placeIds);
    List<Place> findAllByIsActiveTrue();

    @Query("SELECT DISTINCT p FROM Place p " +
            "JOIN p.placeTypes pt " +
            "WHERE p.isActive = true " +
            "AND pt.id IN (" +
            "  SELECT cpt.id FROM CategoryPlacesCatalog c " +
            "  JOIN c.placeTypes cpt " +
            "  WHERE c.id = :categoryId AND c.isActive = true" +
            ")")
    List<Place> findByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT p FROM Place p WHERE p.isActive = true AND " +
            "(LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR p.placeId = :query)")
    List<Place> findByNameOrPlaceId(@Param("query") String query);
}

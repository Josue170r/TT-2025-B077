package com.tt._2025.b077.huellaspormexico.modules.catalogs.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.PlaceTypes;
import com.tt._2025.b077.huellaspormexico.utils.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Category PlaceTypes catalog
 */
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Data
@Entity
@Table(name = "category_places")
@AllArgsConstructor
@NoArgsConstructor
public class CategoryPlacesCatalog extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category", unique = true, nullable = false, length = 150)
    private String category;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "category_place_types",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "place_type_id")
    )
    @JsonIgnore
    private List<PlaceTypes> placeTypes;
}

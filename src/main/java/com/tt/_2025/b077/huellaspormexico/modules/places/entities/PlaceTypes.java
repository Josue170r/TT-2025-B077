package com.tt._2025.b077.huellaspormexico.modules.places.entities;

import com.tt._2025.b077.huellaspormexico.utils.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * PlaceTypes catalog
 */
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Entity
@Table(name = "place_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceTypes extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type",  nullable = false, length = 200)
    private String type;
}

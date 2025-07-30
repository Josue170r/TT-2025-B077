package com.tt._2025.b077.huellaspormexico.modules.places.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tt._2025.b077.huellaspormexico.utils.BaseModel;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Entity
@Table(name = "place_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceImage extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Place place;

    @Column(name = "photo_reference", length = 500)
    private String photoReference;

    @Column(name = "photo_url", length = 1000)
    private String photoUrl;

    @Builder.Default
    @Column(name = "origin", length = 50, nullable = false)
    private String origin = "Google";
}

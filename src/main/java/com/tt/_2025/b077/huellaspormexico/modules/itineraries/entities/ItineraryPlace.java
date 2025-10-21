package com.tt._2025.b077.huellaspormexico.modules.itineraries.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.PostalCodeCatalog;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.Place;
import com.tt._2025.b077.huellaspormexico.utils.BaseModel;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;

@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Entity
@Table(name = "itinerary_place")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryPlace extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "place_id")
    private Place place;

    @Column(name = "visit_order")
    private Integer visitOrder;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "postal_code_id")
    private PostalCodeCatalog postalCode;

    @Column(name = "arrival_time", nullable = false)
    private LocalTime arrivalTime;

    @Column(name = "leaving_time")
    private LocalTime leavingTime;

    @Column(name = "is_visited")
    @Builder.Default
    private Boolean isVisited = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itinerary_day_id")
    @JsonIgnore
    @ToString.Exclude
    private ItineraryDay itineraryDay;

    @PrePersist
    @PreUpdate
    private void validate() {
        if (leavingTime != null && leavingTime.isBefore(arrivalTime)) {
            throw new IllegalStateException(
                    "Leaving_time debe ser después de arrival_time"
            );
        }
    }
}

package com.tt._2025.b077.huellaspormexico.modules.itineraries.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tt._2025.b077.huellaspormexico.utils.BaseModel;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Entity
@Table(name = "itinerary_day")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryDay extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "itinerary_date", nullable = false)
    private LocalDate itineraryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itinerary_id")
    @JsonIgnore
    @ToString.Exclude
    private Itinerary itinerary;

    @OneToMany(mappedBy = "itineraryDay", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItineraryPlace> places;
}

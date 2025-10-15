package com.tt._2025.b077.huellaspormexico.modules.itineraries.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tt._2025.b077.huellaspormexico.modules.hotels.entities.CertificatedHotel;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.Place;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.User;
import com.tt._2025.b077.huellaspormexico.utils.BaseModel;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Entity
@Table(name = "itinerary")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Itinerary extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @ToString.Exclude
    private User user;

    @Column(name = "trip_title", nullable = false, length = 200)
    private String tripTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_place_id", nullable = false)
    private Place hotelPlace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certificated_hotel_id")
    private CertificatedHotel certificatedHotel;

    @OneToMany(mappedBy = "itinerary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItineraryDay> itineraryDays;

    @Transient
    public boolean isCertificatedHotel() {
        return certificatedHotel != null;
    }
}

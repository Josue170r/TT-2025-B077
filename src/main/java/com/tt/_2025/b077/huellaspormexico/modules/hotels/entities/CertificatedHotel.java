package com.tt._2025.b077.huellaspormexico.modules.hotels.entities;

import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.CertificationCatalog;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.SettlementCatalog;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.Place;
import com.tt._2025.b077.huellaspormexico.utils.BaseModel;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Entity
@Table(name = "hotels")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificatedHotel extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hotel_name", nullable = false, length = 500)
    private String hotelName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "place_id")
    private Place place;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "settlement_id")
    private SettlementCatalog settlement;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "hotels_certifications",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "certification_id")
    )
    private List<CertificationCatalog> certifications;
}

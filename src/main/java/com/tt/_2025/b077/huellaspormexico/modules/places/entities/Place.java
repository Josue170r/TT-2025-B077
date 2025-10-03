package com.tt._2025.b077.huellaspormexico.modules.places.entities;

import com.tt._2025.b077.huellaspormexico.utils.BaseModel;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Entity
@Table(name = "places")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Place extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 500)
    private String name;

    @Column(name = "rating", precision = 3, scale = 2, nullable = false)
    private BigDecimal rating = BigDecimal.ZERO;

    @Column(name = "place_id", unique = true, length = 200, nullable = false)
    private String placeId;

    @Column(name = "formatted_address", length = 1000)
    private String formattedAddress;

    @Column(name = "weekday_text", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, String> weekdayText;

    @Column(name = "lat",precision = 17, scale = 14)
    private BigDecimal lat;

    @Column(name = "lng", precision = 11, scale = 8)
    private BigDecimal lng;

    @Column(name = "website", length = 1000)
    private String website;

    @Column(name = "google_maps_url", length = 1000)
    private String googleMapsUrl;

    @Column(name = "formatted_phone_number", length = 50)
    private String formattedPhoneNumber;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<PlaceImage> images;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<PlaceReview> reviews;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "place_place_types",
            joinColumns = @JoinColumn(name = "place_id"),
            inverseJoinColumns = @JoinColumn(name = "place_type_id")
    )
    private List<PlaceTypes> placeTypes;

    @PrePersist
    @PreUpdate
    private void ensureRating() {
        if (rating == null) rating = BigDecimal.ZERO;
    }

    public void setReviews(List<PlaceReview> reviews) {
        this.reviews = new ArrayList<>();
        if (reviews != null) {
            for (PlaceReview review : reviews) {
                review.setPlace(this);
                this.reviews.add(review);
            }
        }
    }

    public void addReview(PlaceReview review) {
        if (this.reviews == null) {
            this.reviews = new ArrayList<>();
        }
        this.reviews.add(review);
        review.setPlace(this);
    }

    public void setImages(List<PlaceImage> images) {
        this.images = new ArrayList<>();
        if (images != null) {
            for (PlaceImage image : images) {
                image.setPlace(this);
                this.images.add(image);
            }
        }
    }

    public void addImage(PlaceImage image) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }
        this.images.add(image);
        image.setPlace(this);
    }

    public void setPlaceTypes(List<PlaceTypes> placeTypes) {
        this.placeTypes = new ArrayList<>();
        if (placeTypes != null) {
            this.placeTypes.addAll(placeTypes);
        }
    }

    public void addPlaceType(PlaceTypes placeType) {
        if (this.placeTypes == null) {
            this.placeTypes = new ArrayList<>();
        }
        if (!this.placeTypes.contains(placeType)) {
            this.placeTypes.add(placeType);
        }
    }

    public double getLat() {
        return lat != null ? lat.doubleValue() : 0.0;
    }

    public double getLng() {
        return lng != null ? lng.doubleValue() : 0.0;
    }

    public double getLocalRating() {
        if (this.reviews != null && !this.reviews.isEmpty()) {
            BigDecimal totalRating = reviews.stream()
                    .map(PlaceReview::getRating)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal promedio = totalRating.divide(
                    BigDecimal.valueOf(reviews.size()),
                    2,
                    RoundingMode.HALF_UP
            );
            return promedio.doubleValue();
        }
        return rating != null ? rating.doubleValue() : 0.0;
    }

    public List<String> getPlaceTypes() {
        if (this.placeTypes == null) return Collections.emptyList();
        return this.placeTypes.stream()
                .map(PlaceTypes::getType)
                .collect(Collectors.toList());
    }
}

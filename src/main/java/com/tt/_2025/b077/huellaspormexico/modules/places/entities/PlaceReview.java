package com.tt._2025.b077.huellaspormexico.modules.places.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tt._2025.b077.huellaspormexico.utils.BaseModel;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Entity
@Table(name = "place_reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceReview extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Place place;

    @Column(name = "author_name", length = 200)
    private String authorName;

    @Column(name = "profile_photo_url", length = 1000)
    private String profilePhotoUrl;

    @Column(name = "timestamp")
    private Long timestamp;

    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    @Builder.Default
    @Column(name = "origin", length = 50, nullable = false)
    private String origin = "Google";

    @Column(name = "rating", precision = 3, scale = 2)
    private BigDecimal rating;

    public LocalDateTime getReviewDateTime() {
        if (timestamp != null) {
            return LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(timestamp),
                    ZoneId.systemDefault()
            );
        }
        return null;
    }

    public String getShortText() {
        if (text == null || text.length() <= 150) {
            return text;
        }
        return text.substring(0, 147) + "...";
    }
}

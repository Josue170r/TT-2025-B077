package com.tt._2025.b077.huellaspormexico.modules.engagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.Place;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.User;
import com.tt._2025.b077.huellaspormexico.utils.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@Data
@Table(
    name = "favorites",
    uniqueConstraints = {
            @UniqueConstraint(columnNames = {"user_id", "place_id"})
    }
)
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PlaceFavorite extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "place_id")
    private Place place;
}

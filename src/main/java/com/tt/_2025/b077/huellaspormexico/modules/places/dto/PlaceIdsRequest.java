package com.tt._2025.b077.huellaspormexico.modules.places.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceIdsRequest {

    @JsonProperty("place_ids")
    private List<Long> placeIds;
}

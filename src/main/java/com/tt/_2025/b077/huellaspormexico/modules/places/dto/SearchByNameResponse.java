package com.tt._2025.b077.huellaspormexico.modules.places.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchByNameResponse {

    private String placeId;

    private String description;

    private String mainText;

    private String secondaryText;

    private List<String> types;
}

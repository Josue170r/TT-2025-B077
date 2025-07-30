package com.tt._2025.b077.huellaspormexico.modules.places.services;

import com.tt._2025.b077.huellaspormexico.modules.places.dto.NearBySearchRequest;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.Place;

import java.util.List;

public interface PlaceApiService {
    Place fetchPlaceDetails(String placeId);
    List<String> fetchNearBySearchPlaces(NearBySearchRequest request);
}

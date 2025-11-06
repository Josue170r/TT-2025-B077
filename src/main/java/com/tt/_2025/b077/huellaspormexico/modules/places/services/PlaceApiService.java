package com.tt._2025.b077.huellaspormexico.modules.places.services;

import com.tt._2025.b077.huellaspormexico.modules.places.dto.NearBySearchRequest;
import com.tt._2025.b077.huellaspormexico.modules.places.dto.SearchByNameRequest;
import com.tt._2025.b077.huellaspormexico.modules.places.dto.SearchByNameResponse;
import com.tt._2025.b077.huellaspormexico.modules.places.dto.TextSearchRequest;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.Place;
import com.tt._2025.b077.huellaspormexico.modules.places.enums.FetchMode;

import java.util.List;

public interface PlaceApiService {
    Place fetchPlaceDetails(String placeId, FetchMode mode);
    List<String> fetchNearBySearchPlaces(NearBySearchRequest request);
    List<SearchByNameResponse> searchPlacesByName(SearchByNameRequest request);
    List<SearchByNameResponse> searchPlacesByText(TextSearchRequest request);
    byte[] fetchGooglePhoto(String photoReference, int with);
}

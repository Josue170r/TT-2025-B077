package com.tt._2025.b077.huellaspormexico.modules.places.services;

import com.tt._2025.b077.huellaspormexico.modules.places.dto.NearByPreferencesRequest;
import com.tt._2025.b077.huellaspormexico.modules.places.dto.NearBySearchRequest;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.Place;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlaceService {
    Place getPlaceDetails(String place_id);
    List<Long> getNearBySearchPlaces (NearBySearchRequest request);
    List<Long> getNearByPreferences(String username, NearByPreferencesRequest request);
    Page<Place> getAllByIdList(List<Long> ids, Pageable pageable);
}

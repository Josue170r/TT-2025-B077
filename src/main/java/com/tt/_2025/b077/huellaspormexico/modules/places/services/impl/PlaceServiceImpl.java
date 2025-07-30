package com.tt._2025.b077.huellaspormexico.modules.places.services.impl;

import com.tt._2025.b077.huellaspormexico.modules.places.dto.NearBySearchRequest;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.Place;
import com.tt._2025.b077.huellaspormexico.modules.places.reporsitories.PlaceRepository;
import com.tt._2025.b077.huellaspormexico.modules.places.services.PlaceApiService;
import com.tt._2025.b077.huellaspormexico.modules.places.services.PlaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceApiService  placeApiService;

    public PlaceServiceImpl(PlaceRepository placeRepository, PlaceApiService placeApiService) {
        this.placeRepository = placeRepository;
        this.placeApiService = placeApiService;
    }

    @Override
    public Place getPlaceDetails(String placeId) {
        return placeRepository.findByPlaceId(placeId)
                .orElseGet(() -> {
                    Place fetchedPlace = placeApiService.fetchPlaceDetails(placeId);
                    return placeRepository.save(fetchedPlace);
                });
    }

    @Override
    public List<Long> getNearBySearchPlaces(NearBySearchRequest request) {
        List<String> googlePlaceIds = placeApiService.fetchNearBySearchPlaces(request);
        List<Long> placesIds = new ArrayList<>();

        for (String googlePlaceId : googlePlaceIds) {
            Place place = placeRepository.findByPlaceId(googlePlaceId)
                    .orElseGet(() -> {
                        Place newPlace = placeApiService.fetchPlaceDetails(googlePlaceId);
                        return placeRepository.save(newPlace);
                    });
            placesIds.add(place.getId());
        }
        return placesIds;
    }

    @Override
    public Page<Place> getAllByIdList(List<Long> ids, Pageable pageable) {
        return placeRepository.findByIdIn(ids, pageable);
    }
}

package com.tt._2025.b077.huellaspormexico.modules.places.services.impl;

import com.tt._2025.b077.huellaspormexico.modules.places.dto.NearByPreferencesRequest;
import com.tt._2025.b077.huellaspormexico.modules.places.dto.NearBySearchRequest;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.Place;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.PlaceTypes;
import com.tt._2025.b077.huellaspormexico.modules.places.reporsitories.PlaceRepository;
import com.tt._2025.b077.huellaspormexico.modules.places.services.PlaceApiService;
import com.tt._2025.b077.huellaspormexico.modules.places.services.PlaceService;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.User;
import com.tt._2025.b077.huellaspormexico.modules.users.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceApiService  placeApiService;
    private final UserRepository userRepository;

    public PlaceServiceImpl(
            PlaceRepository placeRepository,
            PlaceApiService placeApiService,
            UserRepository userRepository) {
        this.placeRepository = placeRepository;
        this.placeApiService = placeApiService;
        this.userRepository = userRepository;
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
        return getOrCreatePlacesIds(googlePlaceIds);
    }

    @Override
    public List<Long> getNearByPreferences(String username, NearByPreferencesRequest request) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Usuario no encontrado: " + username)
        );
        List<String> placeTypes = user.getPlaceTypesFromPreferences();
        NearBySearchRequest dto = NearBySearchRequest.builder()
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .language(request.getLanguage())
                .radius(request.getRadius())
                .types(placeTypes)
                .build();

        List<String> googlePlaceIds = placeApiService.fetchNearBySearchPlaces(dto);
        return getOrCreatePlacesIds(googlePlaceIds);
    }

    @Override
    public Page<Place> getAllByIdList(List<Long> ids, Pageable pageable) {
        return placeRepository.findByIdIn(ids, pageable);
    }

    private List<Long> getOrCreatePlacesIds(List<String> googlePlaceIds) {
        List<Place> existingPlaces = placeRepository.findAllByPlaceIdIn(googlePlaceIds);

        Map<String, Place> byPlaceId = existingPlaces.stream()
                .collect(Collectors.toMap(Place::getPlaceId, p -> p));

        List<Long> result = new ArrayList<>(googlePlaceIds.size());

        for (String placeId : googlePlaceIds) {
            Place place = byPlaceId.get(placeId);
            if (place == null) {
                place = placeRepository.save(placeApiService.fetchPlaceDetails(placeId));
                byPlaceId.put(placeId, place);
            }
            result.add(place.getId());
        }
        return result;
    }
}

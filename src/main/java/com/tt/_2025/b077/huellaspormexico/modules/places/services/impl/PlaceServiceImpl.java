package com.tt._2025.b077.huellaspormexico.modules.places.services.impl;

import com.tt._2025.b077.huellaspormexico.modules.places.dto.NearByPreferencesRequest;
import com.tt._2025.b077.huellaspormexico.modules.places.dto.NearBySearchRequest;
import com.tt._2025.b077.huellaspormexico.modules.places.dto.SearchByNameRequest;
import com.tt._2025.b077.huellaspormexico.modules.places.dto.SearchByNameResponse;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.Place;
import com.tt._2025.b077.huellaspormexico.modules.places.exceptions.PlaceNotFoundException;
import com.tt._2025.b077.huellaspormexico.modules.places.reporsitories.PlaceRepository;
import com.tt._2025.b077.huellaspormexico.modules.places.services.PlaceApiService;
import com.tt._2025.b077.huellaspormexico.modules.places.services.PlaceService;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.User;
import com.tt._2025.b077.huellaspormexico.modules.users.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.*;
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
        Optional<Place> existingOpt = placeRepository.findByPlaceId(placeId);

        try {
            Place newPlace = placeApiService.fetchPlaceDetails(placeId);
            existingOpt.ifPresent(existing -> newPlace.setId(existing.getId()));
            return placeRepository.save(newPlace);
        } catch (Exception ex) {
            return existingOpt.orElseThrow(() ->
                    new PlaceNotFoundException("No se pudo obtener el lugar"));
        }
    }

    @Override
    public List<Long> getNearBySearchPlaces(NearBySearchRequest request) {
        return getLongs(request);
    }

    @Override
    public List<Long> getNearByPreferences(String username, NearByPreferencesRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        List<String> placeTypes = user.getPlaceTypesFromPreferences();

        NearBySearchRequest dto = NearBySearchRequest.builder()
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .language(request.getLanguage())
                .radius(request.getRadius())
                .types(placeTypes)
                .build();

        return getLongs(dto);
    }

    @Override
    public Page<Place> getAllByIdList(List<Long> ids, Pageable pageable) {
        return placeRepository.findByIdIn(ids, pageable);
    }

    @Override
    public List<SearchByNameResponse> searchPlacesByName(SearchByNameRequest request) {
        return placeApiService.searchPlacesByName(request);
    }

    private List<Long> getLongs(NearBySearchRequest dto) {
        List<Place> places = placeApiService.fetchNearBySearchPlaces(dto);

        List<String> incomingPlaceIds = places.stream()
                .map(Place::getPlaceId)
                .filter(Objects::nonNull)
                .toList();

        Map<String, Place> existingByPlaceId = placeRepository.findAllByPlaceIdIn(incomingPlaceIds)
                .stream()
                .collect(Collectors.toMap(Place::getPlaceId, p -> p));

        List<Long> resultIds = new ArrayList<>(places.size());
        for (Place place : places) {
            Place existing = existingByPlaceId.get(place.getPlaceId());
            if (existing != null) {
                resultIds.add(existing.getId());
            } else {
                Place saved = placeRepository.save(place);
                existingByPlaceId.put(saved.getPlaceId(), saved);
                resultIds.add(saved.getId());
            }
        }
        return resultIds;
    }
}

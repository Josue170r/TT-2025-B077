package com.tt._2025.b077.huellaspormexico.modules.places.services.impl;

import com.tt._2025.b077.huellaspormexico.modules.places.dto.*;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.Place;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.PlaceReview;
import com.tt._2025.b077.huellaspormexico.modules.places.enums.FetchMode;
import com.tt._2025.b077.huellaspormexico.modules.places.exceptions.PlaceNotFoundException;
import com.tt._2025.b077.huellaspormexico.modules.places.reporsitories.PlaceRepository;
import com.tt._2025.b077.huellaspormexico.modules.places.services.PlaceApiService;
import com.tt._2025.b077.huellaspormexico.modules.places.services.PlaceService;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.User;
import com.tt._2025.b077.huellaspormexico.modules.users.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;

@Slf4j
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
    @Transactional
    public Place getPlaceDetails(String placeId) {
        Optional<Place> existingOpt = placeRepository.findByPlaceId(placeId);
        try {
            Place newPlace = placeApiService.fetchPlaceDetails(placeId, FetchMode.FULL);

            if (existingOpt.isPresent()) {
                Place existing = existingOpt.get();
                newPlace.setId(existing.getId());

                List<PlaceReview> systemReviews = existing.getReviews().stream()
                        .filter(review -> "Huellas por México".equals(review.getOrigin()))
                        .toList();

                systemReviews.forEach(newPlace::addReview);
            }
            return placeRepository.save(newPlace);
        } catch (Exception ex) {
            return existingOpt.orElseThrow(() ->
                    new PlaceNotFoundException("No se pudo obtener el lugar"));
        }
    }

    @Override
    @Transactional
    public Place getPlaceById(Long id) {
        return placeRepository.findById(id)
                .orElseThrow(() -> new PlaceNotFoundException("No existe el lugar con id " + id));
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
    @Transactional(readOnly = true)
    public Page<Place> getAllByIdList(List<Long> ids, Pageable pageable) {
        return placeRepository.findByIdIn(ids, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SearchByNameResponse> searchPlacesByName(SearchByNameRequest request) {
        return placeApiService.searchPlacesByName(request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SearchByNameResponse> searchPlacesByText(TextSearchRequest request) {
        return placeApiService.searchPlacesByText(request);
    }

    private List<Long> getLongs(NearBySearchRequest dto) {
        List<String> placeIds = placeApiService.fetchNearBySearchPlaces(dto);

        Map<String, Place> existingByPlaceId = placeRepository.findAllByPlaceIdIn(placeIds)
                .stream()
                .collect(Collectors.toMap(Place::getPlaceId, p -> p));

        List<Long> resultIds = new ArrayList<>(placeIds.size());
        for (String placeId : placeIds) {
            Place existing = existingByPlaceId.get(placeId);
            if (existing != null) {
                resultIds.add(existing.getId());
            } else {
                Place saved = placeRepository.save(placeApiService.fetchPlaceDetails(placeId, FetchMode.LIGHT));
                existingByPlaceId.put(saved.getPlaceId(), saved);
                resultIds.add(saved.getId());
            }
        }
        return resultIds;
    }
}

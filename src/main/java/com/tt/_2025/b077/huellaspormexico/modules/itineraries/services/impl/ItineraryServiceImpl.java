package com.tt._2025.b077.huellaspormexico.modules.itineraries.services.impl;

import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.PostalCodeCatalog;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.repositories.PostalCodeRepository;
import com.tt._2025.b077.huellaspormexico.modules.hotels.entities.CertificatedHotel;
import com.tt._2025.b077.huellaspormexico.modules.hotels.repositories.CertificatedHotelRepository;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.dto.*;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.entities.Itinerary;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.entities.ItineraryDay;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.entities.ItineraryPlace;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.repositories.ItineraryDayRepository;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.repositories.ItineraryPlaceRepository;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.repositories.ItineraryRepository;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.services.ItineraryService;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.Place;
import com.tt._2025.b077.huellaspormexico.modules.places.reporsitories.PlaceRepository;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.User;
import com.tt._2025.b077.huellaspormexico.modules.users.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItineraryServiceImpl implements ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final ItineraryDayRepository itineraryDayRepository;
    private final ItineraryPlaceRepository itineraryPlaceRepository;
    private final PlaceRepository placeRepository;
    private final PostalCodeRepository postalCodeRepository;
    private final CertificatedHotelRepository certificatedHotelRepository;
    private final UserRepository userRepository;

    public ItineraryServiceImpl(
            ItineraryRepository itineraryRepository,
            ItineraryDayRepository itineraryDayRepository,
            ItineraryPlaceRepository itineraryPlaceRepository,
            PlaceRepository placeRepository,
            PostalCodeRepository postalCodeRepository,
            CertificatedHotelRepository certificatedHotelRepository,
            UserRepository userRepository
    ) {
        this.itineraryRepository = itineraryRepository;
        this.itineraryDayRepository = itineraryDayRepository;
        this.itineraryPlaceRepository = itineraryPlaceRepository;
        this.placeRepository = placeRepository;
        this.postalCodeRepository = postalCodeRepository;
        this.certificatedHotelRepository = certificatedHotelRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Itinerary createCompleteItinerary(CreateItineraryDTO dto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Place hotelPlace = placeRepository.findById(dto.getHotelPlaceId())
                .orElseThrow(() -> new RuntimeException("Hotel no encontrado"));

        Itinerary itinerary = Itinerary.builder()
                .tripTitle(dto.getTripTitle())
                .hotelPlace(hotelPlace)
                .user(user)
                .build();

        if (Boolean.TRUE.equals(dto.getIsCertificatedHotel())) {
            CertificatedHotel certHotel = certificatedHotelRepository
                    .findByPlaceId(dto.getHotelPlaceId())
                    .orElseThrow(() -> new RuntimeException(
                            "El hotel seleccionado no está certificado"));
            itinerary.setCertificatedHotel(certHotel);
        }

        List<ItineraryDay> days = new ArrayList<>();
        for (ItineraryDayDTO dayDTO : dto.getItineraryDays()) {
            ItineraryDay day = createItineraryDay(dayDTO, itinerary);
            days.add(day);
        }
        itinerary.setItineraryDays(days);
        return itineraryRepository.save(itinerary);
    }

    private ItineraryDay createItineraryDay(ItineraryDayDTO dayDTO, Itinerary itinerary) {
        ItineraryDay day = ItineraryDay.builder()
                .itineraryDate(dayDTO.getItineraryDate())
                .itinerary(itinerary)
                .build();

        List<ItineraryPlace> places = new ArrayList<>();
        for (ItineraryPlaceDTO placeDTO : dayDTO.getPlaces()) {
            ItineraryPlace place = createItineraryPlace(placeDTO, day);
            places.add(place);
        }
        day.setPlaces(places);

        return day;
    }

    private ItineraryPlace createItineraryPlace(ItineraryPlaceDTO placeDTO, ItineraryDay day) {
        Place place = placeRepository.findById(placeDTO.getPlaceId())
                .orElseThrow(() -> new RuntimeException(
                        "Lugar con ID " + placeDTO.getPlaceId() + " no encontrado"));

        PostalCodeCatalog postalCode = null;
        if (placeDTO.getPostalCodeId() != null) {
            postalCode = postalCodeRepository.findById(placeDTO.getPostalCodeId())
                    .orElse(null);
        }

        return ItineraryPlace.builder()
                .place(place)
                .postalCode(postalCode)
                .visitOrder(placeDTO.getVisitOrder())
                .arrivalTime(placeDTO.getArrivalTime())
                .leavingTime(placeDTO.getLeavingTime())
                .itineraryDay(day)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Itinerary getItineraryById(Long id) {
        return itineraryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Itinerario no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Itinerary> getUserItineraries(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return itineraryRepository.findByUserId(user.getId());
    }

    @Override
    @Transactional
    public void updateVisitOrder(Long itineraryId, Long dayId, UpdateVisitOrderDTO dto) {
        itineraryDayRepository.findById(dayId)
                .orElseThrow(() -> new RuntimeException("Día no encontrado"));

        List<ItineraryPlace> places = itineraryPlaceRepository
                .findByItineraryDayIdOrderByVisitOrderAsc(dayId);

        if (places.size() != dto.getPlaceIds().size()) {
            throw new RuntimeException(
                    "La cantidad de lugares no coincide con el orden proporcionado");
        }

        for (int i = 0; i < dto.getPlaceIds().size(); i++) {
            Long placeId = dto.getPlaceIds().get(i);
            ItineraryPlace place = places.stream()
                    .filter(p -> p.getId().equals(placeId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(
                            "Lugar con ID " + placeId + " no encontrado en este día"));

            place.setVisitOrder(i + 1);
            itineraryPlaceRepository.save(place);
        }
    }

    @Override
    @Transactional
    public void updatePlace(Long itineraryId, Long dayId, Long placeId, UpdatePlaceDTO dto) {
        ItineraryPlace itineraryPlace = itineraryPlaceRepository
                .findByIdAndItineraryDayId(placeId, dayId)
                .orElseThrow(() -> new RuntimeException(
                        "Lugar no encontrado en este día"));

        ItineraryDay day = itineraryDayRepository.findByIdAndItineraryId(dayId, itineraryId)
                .orElseThrow(() -> new RuntimeException(
                        "Día no encontrado en este itinerario"));

        Place newPlace = placeRepository.findById(dto.getNewPlaceId())
                .orElseThrow(() -> new RuntimeException("Nuevo lugar no encontrado"));

        itineraryPlace.setPlace(newPlace);
        itineraryPlace.setArrivalTime(dto.getArrivalTime());
        itineraryPlace.setLeavingTime(dto.getLeavingTime());

        if (dto.getPostalCodeId() != null) {
            PostalCodeCatalog postalCode = postalCodeRepository
                    .findById(dto.getPostalCodeId())
                    .orElse(null);
            itineraryPlace.setPostalCode(postalCode);
        }

        itineraryPlaceRepository.save(itineraryPlace);
    }

    @Override
    @Transactional
    public void deleteItinerary(Long id) {
        Itinerary itinerary = getItineraryById(id);
        itineraryRepository.delete(itinerary);
    }
}
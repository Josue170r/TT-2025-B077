package com.tt._2025.b077.huellaspormexico.modules.itineraries.services.impl;

import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.PostalCodeCatalog;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.repositories.PostalCodeRepository;
import com.tt._2025.b077.huellaspormexico.modules.hotels.entities.CertificatedHotel;
import com.tt._2025.b077.huellaspormexico.modules.hotels.exceptions.HotelNotFoundException;
import com.tt._2025.b077.huellaspormexico.modules.hotels.repositories.CertificatedHotelRepository;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.dto.*;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.entities.Itinerary;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.entities.ItineraryDay;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.entities.ItineraryPlace;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.exceptions.*;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.repositories.ItineraryDayRepository;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.repositories.ItineraryPlaceRepository;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.repositories.ItineraryRepository;
import com.tt._2025.b077.huellaspormexico.modules.itineraries.services.ItineraryService;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.Place;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.PlaceImage;
import com.tt._2025.b077.huellaspormexico.modules.places.exceptions.PlaceNotFoundException;
import com.tt._2025.b077.huellaspormexico.modules.places.reporsitories.PlaceRepository;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.User;
import com.tt._2025.b077.huellaspormexico.modules.users.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItineraryServiceImpl implements ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final ItineraryDayRepository itineraryDayRepository;
    private final ItineraryPlaceRepository itineraryPlaceRepository;
    private final PlaceRepository placeRepository;
    private final PostalCodeRepository postalCodeRepository;
    private final CertificatedHotelRepository certificatedHotelRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    public ItineraryServiceImpl(
            ItineraryRepository itineraryRepository,
            ItineraryDayRepository itineraryDayRepository,
            ItineraryPlaceRepository itineraryPlaceRepository,
            PlaceRepository placeRepository,
            PostalCodeRepository postalCodeRepository,
            CertificatedHotelRepository certificatedHotelRepository,
            UserRepository userRepository, EntityManager entityManager) {
        this.itineraryRepository = itineraryRepository;
        this.itineraryDayRepository = itineraryDayRepository;
        this.itineraryPlaceRepository = itineraryPlaceRepository;
        this.placeRepository = placeRepository;
        this.postalCodeRepository = postalCodeRepository;
        this.certificatedHotelRepository = certificatedHotelRepository;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Itinerary createCompleteItinerary(CreateItineraryDTO dto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Place hotelPlace = placeRepository.findById(dto.getHotelPlaceId())
                .orElseThrow(() -> new PlaceNotFoundException("Hotel no encontrado"));

        Itinerary itinerary = Itinerary.builder()
                .tripTitle(dto.getTripTitle())
                .hotelPlace(hotelPlace)
                .user(user)
                .build();

        if (Boolean.TRUE.equals(dto.getIsCertificatedHotel())) {
            CertificatedHotel certHotel = certificatedHotelRepository
                    .findByPlaceId(dto.getHotelPlaceId())
                    .orElseThrow(() -> new HotelNotFoundException(
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
                .orElseThrow(() -> new PlaceNotFoundException(
                        "Lugar con ID " + placeDTO.getPlaceId() + " no encontrado"));

        PostalCodeCatalog postalCode = null;
        if (placeDTO.getPostalCode() != null) {
            postalCode = postalCodeRepository.findByPostalCode(placeDTO.getPostalCode())
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
                .orElseThrow(() -> new ItineraryNotFound("Itinerario no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ItinerarySummaryDTO> getUserItineraries(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Page<Itinerary> itinerariesPage = itineraryRepository.findByUserId(user.getId(), pageable);
        return itinerariesPage.map(this::mapToSummaryDTO);
    }

    private ItinerarySummaryDTO mapToSummaryDTO(Itinerary itinerary) {
        LocalDate startDate = itinerary.getItineraryDays().stream()
                .map(ItineraryDay::getItineraryDate)
                .min(LocalDate::compareTo)
                .orElse(null);

        LocalDate endDate = itinerary.getItineraryDays().stream()
                .map(ItineraryDay::getItineraryDate)
                .max(LocalDate::compareTo)
                .orElse(null);

        int totalPlaces = itinerary.getItineraryDays().stream()
                .mapToInt(day -> day.getPlaces().size())
                .sum();

        Set<String> imageSet = getStrings(itinerary);

        return ItinerarySummaryDTO.builder()
                .id(itinerary.getId())
                .tripTitle(itinerary.getTripTitle())
                .averageSustainableIndex(itinerary.getAverageSustainableIndex())
                .startDate(startDate)
                .endDate(endDate)
                .totalPlaces(totalPlaces)
                .images(new ArrayList<>(imageSet))
                .build();
    }

    private static Set<String> getStrings(Itinerary itinerary) {
        return itinerary.getItineraryDays().stream()
                .flatMap(day -> day.getPlaces().stream())
                .map(ItineraryPlace::getPlace)
                .filter(place -> place.getImages() != null && !place.getImages().isEmpty())
                .map(place -> place.getImages().get(0))
                .filter(ItineraryServiceImpl::isValidImage)
                .map(img -> "Google".equals(img.getOrigin()) ? img.getPhotoReference() : img.getPhotoUrl())
                .collect(Collectors.toSet());
    }

    private static boolean isValidImage(PlaceImage img) {
        String url = img.getPhotoUrl();
        String ref = img.getPhotoReference();
        return (url != null && !url.isEmpty()) || (ref != null && !ref.isEmpty());
    }

    @Override
    @Transactional
    public ItineraryDay updateVisitOrder(Long itineraryId, Long dayId, UpdateVisitOrderDTO dto) {
        ItineraryDay day = itineraryDayRepository.findByIdAndItineraryId(dayId, itineraryId)
                .orElseThrow(() -> new ItineraryDayNotFound(
                        "Día no encontrado en este itinerario"));

        List<ItineraryPlace> places = itineraryPlaceRepository.findByItineraryDayIdOrderByVisitOrder(dayId);

        if (places.size() != dto.getPlaceIds().size()) {
            throw new RuntimeException(
                    "La cantidad de lugares no coincide con el orden proporcionado");
        }

        Map<Long, LocalTime> arrivalTimeMap = new HashMap<>();
        Map<Long, LocalTime> leavingTimeMap = new HashMap<>();

        for (ItineraryPlace place : places) {
            arrivalTimeMap.put(place.getId(), place.getArrivalTime());
            leavingTimeMap.put(place.getId(), place.getLeavingTime());
        }

        for (int i = 0; i < dto.getPlaceIds().size(); i++) {
            Long currentPlaceId = dto.getPlaceIds().get(i);
            Long originalPlaceIdAtPosition = places.get(i).getId();

            ItineraryPlace place = places.stream()
                    .filter(p -> p.getId().equals(currentPlaceId))
                    .findFirst()
                    .orElseThrow(() -> new PlaceNotFoundException(
                            "Lugar con ID " + currentPlaceId + " no encontrado en este día"));

            place.setVisitOrder(i + 1);
            place.setArrivalTime(arrivalTimeMap.get(originalPlaceIdAtPosition));
            place.setLeavingTime(leavingTimeMap.get(originalPlaceIdAtPosition));
            itineraryPlaceRepository.save(place);
        }
        entityManager.flush();
        entityManager.refresh(day);
        return day;
    }

    @Override
    public void setPlaceVisited(Long itineraryId, Long dayId, Long placeId) {
        itineraryDayRepository.findByIdAndItineraryId(dayId, itineraryId)
                .orElseThrow(() -> new ItineraryDayNotFound(
                        "Día no encontrado en este itinerario"));

        ItineraryPlace itineraryPlace = itineraryPlaceRepository
                .findByIdAndItineraryDayId(placeId, dayId)
                .orElseThrow(() -> new ItineraryPlaceNotFound(
                        "Lugar no encontrado en este día"));

        if (itineraryPlace.getIsVisited()) {
            throw new PlaceVisited("El lugar ya fue visitado");
        }
        itineraryPlace.setIsVisited(true);
        itineraryPlaceRepository.save(itineraryPlace);
    }

    @Override
    @Transactional
    public ItineraryPlace updatePlace(Long itineraryId, Long dayId, Long placeId, UpdatePlaceDTO dto) {
        itineraryDayRepository.findByIdAndItineraryId(dayId, itineraryId)
                .orElseThrow(() -> new ItineraryDayNotFound(
                        "Día no encontrado en este itinerario"));
        
        ItineraryPlace itineraryPlace = itineraryPlaceRepository
                .findByIdAndItineraryDayId(placeId, dayId)
                .orElseThrow(() -> new ItineraryPlaceNotFound(
                        "Lugar no encontrado en este día"));

        Place newPlace = placeRepository.findById(dto.getNewPlaceId())
                .orElseThrow(() -> new PlaceNotFoundException("Nuevo lugar no encontrado"));

        itineraryPlace.setPlace(newPlace);
        itineraryPlace.setArrivalTime(dto.getArrivalTime());
        itineraryPlace.setLeavingTime(dto.getLeavingTime());
        if (itineraryPlace.getIsVisited()) {
            itineraryPlace.setIsVisited(false);
        }

        if (dto.getPostalCode() != null) {
            PostalCodeCatalog postalCode = postalCodeRepository
                    .findByPostalCode(dto.getPostalCode())
                    .orElse(null);
            itineraryPlace.setPostalCode(postalCode);
        }
        return itineraryPlaceRepository.save(itineraryPlace);
    }

    @Override
    @Transactional
    public ItineraryPlace updatePlaceTime(Long itineraryId, Long dayId, Long placeId, UpdatePlaceTimeDTO dto) {
        itineraryDayRepository.findByIdAndItineraryId(dayId, itineraryId)
                .orElseThrow(() -> new ItineraryNotFound(
                        "Día no encontrado en este itinerario"));

        ItineraryPlace itineraryPlace = itineraryPlaceRepository
                .findByIdAndItineraryDayId(placeId, dayId)
                .orElseThrow(() -> new ItineraryPlaceNotFound(
                        "Lugar no encontrado en este día"));

        LocalTime arrivalTime = dto.getArrivalTime();
        LocalTime leavingTime = dto.getLeavingTime();

        validateTimeRange(arrivalTime, leavingTime);
        validateNoTimeOverlap(dayId, placeId, arrivalTime, leavingTime);

        itineraryPlace.setArrivalTime(arrivalTime);
        itineraryPlace.setLeavingTime(leavingTime);

        return itineraryPlaceRepository.save(itineraryPlace);
    }

    private void validateTimeRange(LocalTime arrivalTime, LocalTime leavingTime) {
        if (arrivalTime != null && leavingTime != null && arrivalTime.isAfter(leavingTime)) {
            throw new IllegalArgumentException(
                    "La hora de llegada debe ser anterior a la hora de salida");
        }
    }

    private void validateNoTimeOverlap(Long dayId, Long placeId, LocalTime arrivalTime, LocalTime leavingTime) {
        if (arrivalTime == null || leavingTime == null) {
            return;
        }
        List<ItineraryPlace> otherPlaces = itineraryPlaceRepository
                .findByItineraryDayIdOrderByVisitOrder(dayId)
                .stream()
                .filter(p -> !p.getId().equals(placeId))
                .filter(p -> p.getArrivalTime() != null && p.getLeavingTime() != null)
                .toList();

        for (ItineraryPlace otherPlace : otherPlaces) {
            LocalTime otherArrival = otherPlace.getArrivalTime();
            LocalTime otherLeaving = otherPlace.getLeavingTime();

            if (timesOverlap(arrivalTime, leavingTime, otherArrival, otherLeaving)) {
                throw new TimeOverlapException(
                        "El horario se traslapa con el lugar '" + otherPlace.getPlace().getName() +
                                "' (" + formatTime(otherArrival) + " - " + formatTime(otherLeaving) + ")");
            }
        }
    }

    private boolean timesOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return (start1.isBefore(end2) && end1.isAfter(start2)) ||
                start1.equals(start2) || end1.equals(end2);
    }

    private String formatTime(LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    @Override
    public List<ItineraryDaysResponseDto> getItineraryDays(Long itineraryId) {
        List<ItineraryDay> days = itineraryDayRepository.findByItineraryId(itineraryId);

        if (days.isEmpty()) {
            throw new ItineraryDayNotFound("Este itinerario no tiene ningún día");
        }

        return days.stream()
                .map(day -> new ItineraryDaysResponseDto(day.getId(), day.getItineraryDate()))
                .toList();
    }

    @Override
    @Transactional
    public void addPlaceToDay(Long itineraryId, Long dayId, AddNewPlaceDTO dto) {
        ItineraryDay day = itineraryDayRepository.findByIdAndItineraryId(dayId, itineraryId)
                .orElseThrow(() -> new ItineraryNotFound("Día no encontrado en este itinerario"));

        Place newPlace = placeRepository.findById(dto.getNewPlaceId())
                .orElseThrow(() -> new PlaceNotFoundException("Nuevo lugar no encontrado"));

        if (itineraryPlaceRepository.existsByPlacePlaceIdAndItineraryDayId(newPlace.getPlaceId(), day.getId())) {
            throw new RuntimeException("El lugar ya se encuentra en el itinerario");
        }

        List<ItineraryPlace> placesByDay = itineraryPlaceRepository.findByItineraryDayId(dayId);
        if (placesByDay.size() >= 6) {
            throw new RuntimeException("El límite de lugares por día es 6");
        }

        int nextVisitOrder = 1;
        if (!placesByDay.isEmpty()) {
            ItineraryPlace lastPlace = itineraryPlaceRepository.findFirstByItineraryDayIdOrderByVisitOrderDesc(dayId);
            if (lastPlace != null) {
                nextVisitOrder = lastPlace.getVisitOrder() + 1;
            }
        }

        PostalCodeCatalog postalCode = dto.getPostalCode() != null
                ? postalCodeRepository.findByPostalCode(dto.getPostalCode()).orElse(null)
                : null;

        ItineraryPlace newItineraryPlace = ItineraryPlace.builder()
                .place(newPlace)
                .itineraryDay(day)
                .postalCode(postalCode)
                .arrivalTime(dto.getArrivalTime())
                .leavingTime(dto.getLeavingTime())
                .visitOrder(nextVisitOrder)
                .build();

        itineraryPlaceRepository.save(newItineraryPlace);
    }

    @Override
    public void deletePlaceByDayId(Long itineraryId, Long dayId, Long placeId) {
        itineraryDayRepository.findByIdAndItineraryId(dayId, itineraryId)
                .orElseThrow(() -> new ItineraryNotFound(
                        "Día no encontrado en este itinerario"));

        ItineraryPlace itineraryPlace = itineraryPlaceRepository
                .findByIdAndItineraryDayId(placeId, dayId)
                .orElseThrow(() -> new ItineraryPlaceNotFound(
                        "Lugar no encontrado en este día"));

        itineraryPlaceRepository.delete(itineraryPlace);
    }

    @Override
    @Transactional
    public void deleteItinerary(Long id) {
        Itinerary itinerary = getItineraryById(id);
        itineraryRepository.delete(itinerary);
    }
}
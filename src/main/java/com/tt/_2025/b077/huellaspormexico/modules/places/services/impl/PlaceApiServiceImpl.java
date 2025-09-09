package com.tt._2025.b077.huellaspormexico.modules.places.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tt._2025.b077.huellaspormexico.modules.places.dto.NearBySearchRequest;
import com.tt._2025.b077.huellaspormexico.modules.places.dto.SearchByNameRequest;
import com.tt._2025.b077.huellaspormexico.modules.places.dto.SearchByNameResponse;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.Place;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.PlaceImage;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.PlaceReview;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.PlaceTypes;
import com.tt._2025.b077.huellaspormexico.modules.places.enums.FetchMode;
import com.tt._2025.b077.huellaspormexico.modules.places.exceptions.PlaceNotFoundException;
import com.tt._2025.b077.huellaspormexico.modules.places.reporsitories.PlaceTypesRepository;
import com.tt._2025.b077.huellaspormexico.modules.places.services.PlaceApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class PlaceApiServiceImpl implements PlaceApiService {


    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final PlaceTypesRepository placeTypesRepository;

    @Value("${google.api.key}")
    private String googleApiKey;

    @Value("${tripadvisor.api.key}")
    private String tripAdvisorApiKey;

    @Value("${google.base.url}")
    private String googleBaseUrl;

    @Value("${tripadvisor.base.url}")
    private String tripAdvisorBaseUrl;

    public PlaceApiServiceImpl(RestTemplate restTemplate, ObjectMapper objectMapper, PlaceTypesRepository placeTypesRepository) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.placeTypesRepository = placeTypesRepository;
    }

    @Override
    public Place fetchPlaceDetails(String placeId) {
        Place place = fetchFromGoogle(placeId, FetchMode.FULL);
        enrichWithTripAdvisor(place);
        return place;
    }

    @Override
    public List<Place> fetchNearBySearchPlaces(NearBySearchRequest request) {
        List<String> placeIds = fetchNearBySearchPlacesGoogle(request);
        List<Place> places = new ArrayList<>();
        for (String placeId : placeIds) {
            try {
                places.add(fetchFromGoogle(placeId, FetchMode.LIGHT));
            } catch (Exception ex) {
                log.warn("No se pudo traer el lugar {}: {}", placeId, ex.getMessage());
            }
        }
        return places;
    }

    @Override
    public List<SearchByNameResponse> searchPlacesByName(SearchByNameRequest request) {
        List<SearchByNameResponse> suggestions = new ArrayList<>();

        try {
            String url = UriComponentsBuilder
                    .fromUriString(googleBaseUrl + "/autocomplete/json")
                    .queryParam("input", request.getInput())
                    .queryParam("language", request.getLanguage())
                    .queryParam("components", "country:" + request.getCountry())
                    .queryParam("key", googleApiKey)
                    .toUriString();

            log.debug("Calling Google Autocomplete API for place search: {}", url);

            String response = restTemplate.getForObject(url, String.class);
            JsonNode rootNode = objectMapper.readTree(response);

            String status = rootNode.get("status").asText();

            if (!"OK".equals(status)) {
                if ("ZERO_RESULTS".equals(status)) {
                    return suggestions;
                }
                throw new RuntimeException("Error en Google Places API: " + status);
            }

            JsonNode predictions = rootNode.get("predictions");
            if (predictions != null && predictions.isArray()) {
                for (JsonNode prediction : predictions) {
                    SearchByNameResponse suggestion = mapJsonToSearchByNameResponse(prediction);
                    if (suggestion != null) {
                        suggestions.add(suggestion);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error buscando lugares por nombre: {}", e.getMessage(), e);
            throw new PlaceNotFoundException("Error al buscar lugares por nombre");
        }

        return suggestions;
    }

    private Place fetchFromGoogle(String placeId, FetchMode mode) {
        try {
            String url = UriComponentsBuilder
                    .fromUriString(googleBaseUrl + "/details/json")
                    .queryParam("place_id", placeId)
                    .queryParam("fields", mode.getFields())
                    .queryParam("key", googleApiKey)
                    .queryParam("language", "es")
                    .toUriString();

            String response = restTemplate.getForObject(url, String.class);
            JsonNode rootNode = objectMapper.readTree(response);

            if (!"OK".equals(rootNode.get("status").asText())) {
                throw new PlaceNotFoundException("Lugar no encontrado: " + placeId);
            }

            JsonNode result = rootNode.get("result");
            Place place = mapJsonToPlace(result, placeId);

            if (result.has("types")) {
                mapGoogleTypesToPlaceTypes(result.get("types"), place);
            }

            if (mode == FetchMode.FULL) {
                if (result.has("reviews")) {
                    mapJsonToReviews(result.get("reviews"), "Google").forEach(place::addReview);
                }
            }

            if (result.has("photos")) {
                mapJsonToImages(result.get("photos"), "Google").forEach(place::addImage);
            }
            return place;

        } catch (Exception e) {
            throw new RuntimeException("Error fetching from Google Places API", e);
        }
    }

    private List<String> fetchNearBySearchPlacesGoogle(NearBySearchRequest request) {
        Set<String> allPlaceIds = new HashSet<>();
        String location = request.getLatitude() + "," + request.getLongitude();

        for (String type : request.getTypes()) {
            try {
                String url = UriComponentsBuilder
                        .fromUriString(googleBaseUrl + "/nearbysearch/json")
                        .queryParam("location", location)
                        .queryParam("radius", request.getRadius())
                        .queryParam("language", request.getLanguage())
                        .queryParam("type", type)
                        .queryParam("key", googleApiKey)
                        .toUriString();

                String response = restTemplate.getForObject(url, String.class);
                JsonNode rootNode = objectMapper.readTree(response);

                if (!"OK".equals(rootNode.get("status").asText())) {
                    log.warn("Error en consulta para tipo {}: {}", type, rootNode.get("status").asText());
                    continue;
                }

                JsonNode results = rootNode.get("results");
                List<String> placeIdsResult = getPlaceIdFromResults(results);
                allPlaceIds.addAll(placeIdsResult);

            } catch (Exception e) {
                log.error("Error fetching places for type {}: {}", type, e.getMessage());
            }
        }
        return new ArrayList<>(allPlaceIds);
    }

    private List<String> getPlaceIdFromResults(JsonNode resultsNode) {
        List<String> placeIds = new ArrayList<>();

        if (resultsNode == null || !resultsNode.isArray()) {
            return placeIds;
        }

        for (JsonNode node : resultsNode) {
            String placeId = getTextValue(node, "place_id");
            if (placeId != null && !placeId.isEmpty()) {
                placeIds.add(placeId);
            }
        }
        return placeIds;
    }

    private void enrichWithTripAdvisor(Place place) {
        try {
            Optional<String> locationId = searchTripAdvisorLocation(place.getName());
            if (locationId.isEmpty()) return;

            String id = locationId.get();
            mapJsonToReviews(getTripAdvisorReviews(id), "TripAdvisor").forEach(place::addReview);
            mapJsonToImages(getTripAdvisorImages(id), "TripAdvisor").forEach(place::addImage);

        } catch (Exception e) {
            log.warn("Could not fetch TripAdvisor data for: {}", place.getName());
        }
    }

    private Optional<String> searchTripAdvisorLocation(String placeName) {
        try {
            String url = UriComponentsBuilder
                    .fromUriString(tripAdvisorBaseUrl + "/search")
                    .queryParam("searchQuery", placeName)
                    .queryParam("key", tripAdvisorApiKey)
                    .queryParam("language", "es")
                    .toUriString();

            String response = restTemplate.getForObject(url, String.class);
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode dataNode = rootNode.get("data");

            if (dataNode == null || !dataNode.isArray() || dataNode.isEmpty()) {
                return Optional.empty();
            }

            for (JsonNode result : dataNode) {
                String resultName = getTextValue(result, "name");
                if (resultName != null && calculateSimilarity(resultName.toLowerCase(), placeName.toLowerCase()) >= 0.8) {
                    return Optional.ofNullable(getTextValue(result, "location_id"));
                }
            }

            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private JsonNode getTripAdvisorReviews(String locationId) {
        try {
            String url = UriComponentsBuilder
                    .fromUriString(tripAdvisorBaseUrl + "/" + locationId + "/reviews")
                    .queryParam("key", tripAdvisorApiKey)
                    .queryParam("language", "es")
                    .toUriString();

            String response = restTemplate.getForObject(url, String.class);
            JsonNode rootNode = objectMapper.readTree(response);
            return rootNode.get("data");
        } catch (Exception e) {
            return objectMapper.createArrayNode();
        }
    }

    private JsonNode getTripAdvisorImages(String locationId) {
        try {
            String url = UriComponentsBuilder
                    .fromUriString(tripAdvisorBaseUrl + "/" + locationId + "/photos")
                    .queryParam("key", tripAdvisorApiKey)
                    .queryParam("language", "es")
                    .toUriString();

            String response = restTemplate.getForObject(url, String.class);
            JsonNode rootNode = objectMapper.readTree(response);
            return rootNode.get("data");
        } catch (Exception e) {
            return objectMapper.createArrayNode();
        }
    }

    private Place mapJsonToPlace(JsonNode result, String placeId) {
        Place place = Place.builder()
                .name(getTextValue(result, "name"))
                .rating(getBigDecimalValue(result, "rating"))
                .placeId(placeId)
                .formattedAddress(getTextValue(result, "formatted_address"))
                .website(getTextValue(result, "website"))
                .googleMapsUrl(getTextValue(result, "url"))
                .formattedPhoneNumber(getTextValue(result, "formatted_phone_number"))
                .description(getNestedTextValue(result))
                .build();

        JsonNode geometry = result.get("geometry");
        if (geometry != null && geometry.has("location")) {
            JsonNode location = geometry.get("location");
            place.setLat(getBigDecimalValue(location, "lat"));
            place.setLng(getBigDecimalValue(location, "lng"));
        }


        JsonNode openingHours = result.get("opening_hours");
        if (openingHours != null && openingHours.has("weekday_text")) {
            JsonNode weekdayArray = openingHours.get("weekday_text");
            Map<String, String> weekdayMap = new HashMap<>();

            for (int i = 0; i < weekdayArray.size(); i++) {
                String daySchedule = weekdayArray.get(i).asText();
                String[] parts = daySchedule.split(": ", 2);
                if (parts.length == 2) {
                    weekdayMap.put(parts[0], parts[1]);
                }
            }
            place.setWeekdayText(weekdayMap);
        }
        return place;
    }

    private List<PlaceReview> mapJsonToReviews(JsonNode reviewsNode, String origin) {
        List<PlaceReview> reviews = new ArrayList<>();
        if (reviewsNode == null || !reviewsNode.isArray()) return reviews;

        for (JsonNode reviewNode : reviewsNode) {
            PlaceReview review = PlaceReview.builder()
                    .origin(origin)
                    .build();

            if ("Google".equals(origin)) {
                review.setAuthorName(getTextValue(reviewNode, "author_name"));
                review.setProfilePhotoUrl(getTextValue(reviewNode, "profile_photo_url"));
                review.setTimestamp(getLongValue(reviewNode));
                review.setText(getTextValue(reviewNode, "text"));
                review.setRating(getBigDecimalValue(reviewNode, "rating"));
            } else {
                JsonNode userNode = reviewNode.get("user");
                if (userNode != null) {
                    review.setAuthorName(getTextValue(userNode, "username"));
                    JsonNode avatarNode = userNode.get("avatar");
                    if (avatarNode != null) {
                        review.setProfilePhotoUrl(getTextValue(avatarNode, "large"));
                    }
                }

                review.setText(getTextValue(reviewNode, "text"));
                review.setRating(getBigDecimalValue(reviewNode, "rating"));

                String publishedDate = getTextValue(reviewNode, "published_date");
                if (publishedDate != null) {
                    try {
                        LocalDateTime dateTime = publishedDate.contains("T")
                                ? LocalDateTime.parse(publishedDate, DateTimeFormatter.ISO_DATE_TIME)
                                : LocalDateTime.parse(publishedDate + "T00:00:00", DateTimeFormatter.ISO_DATE_TIME);
                        review.setTimestamp(dateTime.atZone(java.time.ZoneId.systemDefault()).toEpochSecond());
                    } catch (Exception e) {
                        review.setTimestamp(System.currentTimeMillis() / 1000);
                    }
                }
            }
            reviews.add(review);
        }
        return reviews;
    }

    private List<PlaceImage> mapJsonToImages(JsonNode imagesNode, String origin) {
        List<PlaceImage> images = new ArrayList<>();
        if (imagesNode == null || !imagesNode.isArray()) return images;

        for (JsonNode imageNode : imagesNode) {
            PlaceImage image = PlaceImage.builder()
                    .origin(origin)
                    .build();

            if ("Google".equals(origin)) {
                String photoReference = getTextValue(imageNode, "photo_reference");
                if (photoReference != null) {
                    image.setPhotoReference(photoReference);
                    String photoUrl = UriComponentsBuilder
                            .fromUriString(googleBaseUrl + "/photo")
                            .queryParam("maxwidth", "4000")
                            .queryParam("photo_reference", photoReference)
                            .queryParam("key", googleApiKey)
                            .toUriString();
                    image.setPhotoUrl(photoUrl);
                }
            } else {
                String photoId = getTextValue(imageNode, "id");
                image.setPhotoReference(photoId != null ? photoId : "");

                JsonNode imagesData = imageNode.get("images");
                if (imagesData != null) {
                    JsonNode originalNode = imagesData.get("original");
                    if (originalNode != null) {
                        image.setPhotoUrl(getTextValue(originalNode, "url"));
                    } else {
                        JsonNode largeNode = imagesData.get("large");
                        if (largeNode != null) {
                            image.setPhotoUrl(getTextValue(largeNode, "url"));
                        }
                    }
                }
            }
            if (image.getPhotoUrl() != null && !image.getPhotoUrl().isEmpty()) {
                images.add(image);
            }
        }
        return images;
    }

    private void mapGoogleTypesToPlaceTypes(JsonNode typesNode, Place place) {
        if (typesNode == null || !typesNode.isArray()) return;

        List<PlaceTypes> placeTypes = new ArrayList<>();

        for (JsonNode typeNode : typesNode) {
            String googleType = typeNode.asText();

            Optional<PlaceTypes> placeTypeOpt = placeTypesRepository.findByType(googleType);

            if (placeTypeOpt.isPresent()) {
                placeTypes.add(placeTypeOpt.get());
                log.debug("Found PlaceType in DB: {}", googleType);
            }
        }
        place.setPlaceTypes(placeTypes);
    }

    private SearchByNameResponse mapJsonToSearchByNameResponse(JsonNode prediction) {
        try {
            String placeId = getTextValue(prediction, "place_id");
            String description = getTextValue(prediction, "description");

            if (placeId == null || description == null) {
                return null;
            }

            SearchByNameResponse suggestion = SearchByNameResponse.builder()
                    .placeId(placeId)
                    .description(description)
                    .build();

            JsonNode structuredFormatting = prediction.get("structured_formatting");
            if (structuredFormatting != null) {
                suggestion.setMainText(getTextValue(structuredFormatting, "main_text"));
                suggestion.setSecondaryText(getTextValue(structuredFormatting, "secondary_text"));
            }

            JsonNode types = prediction.get("types");
            if (types != null && types.isArray()) {
                List<String> typesList = new ArrayList<>();
                for (JsonNode type : types) {
                    typesList.add(type.asText());
                }
                suggestion.setTypes(typesList);
            }

            return suggestion;

        } catch (Exception e) {
            return null;
        }
    }

    private double calculateSimilarity(String s1, String s2) {
        if (s1 == null || s2 == null) return 0.0;
        if (s1.equals(s2)) return 1.0;

        int maxLength = Math.max(s1.length(), s2.length());
        if (maxLength == 0) return 1.0;

        int distance = levenshteinDistance(s1, s2);
        return 1.0 - (double) distance / maxLength;
    }

    private int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= s2.length(); j++) dp[0][j] = j;

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
            }
        }
        return dp[s1.length()][s2.length()];
    }

    private String getTextValue(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        return field != null && !field.isNull() ? field.asText() : null;
    }

    private String getNestedTextValue(JsonNode node) {
        JsonNode parent = node.get("editorial_summary");
        if (parent != null) {
            JsonNode child = parent.get("overview");
            return child != null && !child.isNull() ? child.asText() : null;
        }
        return null;
    }

    private BigDecimal getBigDecimalValue(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        return field != null && !field.isNull() ? BigDecimal.valueOf(field.asDouble()) : null;
    }

    private Long getLongValue(JsonNode node) {
        JsonNode field = node.get("time");
        return field != null && !field.isNull() ? field.asLong() : null;
    }
}
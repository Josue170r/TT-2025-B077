package com.tt._2025.b077.huellaspormexico.modules.hotels.repositories;

import com.tt._2025.b077.huellaspormexico.modules.hotels.entities.CertificatedHotel;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class CertificatedHotelSpecification {

    private static final double EARTH_RADIUS_KM = 6371.0;
    private static final double SEARCH_RADIUS_KM = 4.0;

    public static Specification<CertificatedHotel> byHotelName(String hotelName) {
        return (root, query, criteriaBuilder) -> {
            if (hotelName == null || hotelName.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("hotelName")),
                    "%" + hotelName.toLowerCase() + "%");
        };
    }

    public static Specification<CertificatedHotel> bySettlement(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("settlement").get("id"), id);
        };
    }

    public static Specification<CertificatedHotel> byState(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("settlement").get("state").get("id"), id);
        };
    }

    public static Specification<CertificatedHotel> byCertifications(List<Long> certificationIds) {
        return (root, query, criteriaBuilder) -> {
            if (certificationIds == null || certificationIds.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.join("certifications").get("id").in(certificationIds);
        };
    }

    public static Specification<CertificatedHotel> byCoordinates(Double latitude, Double longitude) {
        return (root, query, criteriaBuilder) -> {
            if (latitude == null || longitude == null) {
                return criteriaBuilder.conjunction();
            }

            Expression<Double> placeLat = root.get("place").get("lat").as(Double.class);
            Expression<Double> placeLng = root.get("place").get("lng").as(Double.class);

            Expression<Double> lat1Rad = criteriaBuilder.prod(
                    criteriaBuilder.literal(latitude),
                    criteriaBuilder.literal(Math.PI / 180.0)
            );
            Expression<Double> lat2Rad = criteriaBuilder.prod(placeLat, Math.PI / 180.0);
            Expression<Double> deltaLatRad = criteriaBuilder.prod(
                    criteriaBuilder.diff(placeLat, latitude),
                    Math.PI / 180.0
            );
            Expression<Double> deltaLonRad = criteriaBuilder.prod(
                    criteriaBuilder.diff(placeLng, longitude),
                    Math.PI / 180.0
            );

            Expression<Double> sinDeltaLat = criteriaBuilder.function(
                    "sin", Double.class,
                    criteriaBuilder.quot(deltaLatRad, 2.0)
            );
            Expression<Double> sinDeltaLatSq = criteriaBuilder.prod(sinDeltaLat, sinDeltaLat);

            Expression<Double> sinDeltaLon = criteriaBuilder.function(
                    "sin", Double.class,
                    criteriaBuilder.quot(deltaLonRad, 2.0)
            );
            Expression<Double> sinDeltaLonSq = criteriaBuilder.prod(sinDeltaLon, sinDeltaLon);

            Expression<Double> cosLat1 = criteriaBuilder.function("cos", Double.class, lat1Rad);
            Expression<Double> cosLat2 = criteriaBuilder.function("cos", Double.class, lat2Rad);
            Expression<Double> cosProduct = criteriaBuilder.prod(cosLat1, cosLat2);

            Expression<Double> a = criteriaBuilder.sum(
                    sinDeltaLatSq,
                    criteriaBuilder.prod(cosProduct, sinDeltaLonSq)
            );
            Expression<Double> sqrtA = criteriaBuilder.function("sqrt", Double.class, a);
            Expression<Double> asin = criteriaBuilder.function("asin", Double.class, sqrtA);
            Expression<Double> c = criteriaBuilder.prod(2.0, asin);
            Expression<Double> distance = criteriaBuilder.prod(EARTH_RADIUS_KM, c);
            return criteriaBuilder.lessThanOrEqualTo(distance, SEARCH_RADIUS_KM);
        };
    }

    public static Specification<CertificatedHotel> buildSpecification(
            String hotelName,
            Long settlementId,
            Long stateId,
            List<Long> certificationIds,
            Double latitude,
            Double longitude) {
        return byHotelName(hotelName)
                .and(bySettlement(settlementId))
                .and(byState(stateId))
                .and(byCertifications(certificationIds))
                .and(byCoordinates(latitude, longitude));
    }
}
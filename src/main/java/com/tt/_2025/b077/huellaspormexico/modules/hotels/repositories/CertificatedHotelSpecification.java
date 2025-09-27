package com.tt._2025.b077.huellaspormexico.modules.hotels.repositories;

import com.tt._2025.b077.huellaspormexico.modules.hotels.entities.CertificatedHotel;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class CertificatedHotelSpecification {

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

    public static Specification<CertificatedHotel> buildSpecification(
            String hotelName, Long settlementId, Long stateId, List<Long> certificationIds) {
        return byHotelName(hotelName)
                .and(bySettlement(settlementId))
                .and(byState(stateId))
                .and(byCertifications(certificationIds));
    }
}
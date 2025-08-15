package com.tt._2025.b077.huellaspormexico.modules.catalogs.repositories;

import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.PostalCodeCatalog;
import org.springframework.data.jpa.domain.Specification;

public class PostalCodeSpecification {

    public static Specification<PostalCodeCatalog> byPostalCode(String postalCode) {
        return (root, query, criteriaBuilder) -> {
            if (postalCode == null || postalCode.trim().isEmpty()) {
                return criteriaBuilder.conjunction(); // Retorna todos si es null
            }
            return criteriaBuilder.equal(root.get("postalCode"), postalCode);
        };
    }

    public static Specification<PostalCodeCatalog> bySettlement(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("settlement").get("id"), id);
        };
    }

    public static Specification<PostalCodeCatalog> byState(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("settlement").get("state").get("id"), id);
        };
    }

    public static Specification<PostalCodeCatalog> buildSpecification(String postalCode, Long settlementId, Long stateId) {
        return byPostalCode(postalCode)
                .and(bySettlement(settlementId))
                .and(byState(stateId));
    }
}

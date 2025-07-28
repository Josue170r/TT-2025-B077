package com.tt._2025.b077.huellaspormexico.modules.carbon.repositories;

import com.tt._2025.b077.huellaspormexico.modules.carbon.entities.CarbonActivity;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.User;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CarbonActivitySpecification {

    public static Specification<CarbonActivity> byUser(User user) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user"), user);
    }

    public static Specification<CarbonActivity> byActivityType(String activityType) {
        return (root, query, criteriaBuilder) ->
                activityType == null ? null : criteriaBuilder.equal(root.get("activityType"), activityType);
    }

    public static Specification<CarbonActivity> byDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return null;
            }
            if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("registeredAt"), startDate, endDate);
            }
            if (startDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("registeredAt"), startDate);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("registeredAt"), endDate);
        };
    }

    public static Specification<CarbonActivity> buildSpecification(User user, String activityType,
                                                                   LocalDateTime startDate, LocalDateTime endDate) {
        return byUser(user)
                .and(byActivityType(activityType))
                .and(byDateRange(startDate, endDate));
    }
}
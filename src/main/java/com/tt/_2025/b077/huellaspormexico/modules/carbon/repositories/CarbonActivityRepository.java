package com.tt._2025.b077.huellaspormexico.modules.carbon.repositories;

import com.tt._2025.b077.huellaspormexico.modules.carbon.entities.CarbonActivity;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface CarbonActivityRepository extends JpaRepository<CarbonActivity, Long>, JpaSpecificationExecutor<CarbonActivity> {

    @Query("SELECT COALESCE(SUM(ca.co2Generated), 0) FROM CarbonActivity ca " +
            "WHERE (:userId IS NULL OR ca.user.id = :userId) " +
            "AND (:activityType IS NULL OR ca.activityType = :activityType) " +
            "AND (:startDate IS NULL OR ca.registeredAt >= :startDate) " +
            "AND (:endDate IS NULL OR ca.registeredAt <= :endDate)")
    BigDecimal calculateTotalCo2(@Param("userId") Long userId,
                                 @Param("activityType") String activityType,
                                 @Param("startDate") java.time.LocalDateTime startDate,
                                 @Param("endDate") java.time.LocalDateTime endDate);
}

package com.tt._2025.b077.huellaspormexico.modules.carbon.service;

import com.tt._2025.b077.huellaspormexico.modules.carbon.entities.CarbonActivity;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface CarbonActivityService {
    void saveCarbonActivity(CarbonActivity carbonActivity);
    List<CarbonActivity> getUserActivities(Long userID, String activityType, LocalDate startDate, LocalDate endDate);
    BigDecimal calculateUserTotalCo2(Long userID, String activityType, LocalDate startDate, LocalDate endDate);
    User findUserById(Long userID);
}
package com.tt._2025.b077.huellaspormexico.modules.carbon.service;

import com.tt._2025.b077.huellaspormexico.modules.carbon.entities.CarbonActivity;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface CarbonActivityService {
    void saveCarbonActivity(CarbonActivity carbonActivity);
    List<CarbonActivity> getUserActivities(String username, String activityType, LocalDate startDate, LocalDate endDate);
    BigDecimal calculateUserTotalCo2(String username, String activityType, LocalDate startDate, LocalDate endDate);
}
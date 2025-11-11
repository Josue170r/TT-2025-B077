package com.tt._2025.b077.huellaspormexico.modules.carbon.service.impl;

import com.tt._2025.b077.huellaspormexico.modules.carbon.repositories.CarbonActivityRepository;
import com.tt._2025.b077.huellaspormexico.modules.carbon.entities.CarbonActivity;
import com.tt._2025.b077.huellaspormexico.modules.carbon.repositories.CarbonActivitySpecification;
import com.tt._2025.b077.huellaspormexico.modules.carbon.service.CarbonActivityService;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.User;
import com.tt._2025.b077.huellaspormexico.modules.users.repositories.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CarbonActivityServiceImpl implements CarbonActivityService {

    private final UserRepository userRepository;
    private final CarbonActivityRepository carbonRepository;

    public CarbonActivityServiceImpl(UserRepository userRepository, CarbonActivityRepository carbonRepository) {
        this.userRepository = userRepository;
        this.carbonRepository = carbonRepository;
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveCarbonActivity(CarbonActivity carbonActivity) {
        carbonRepository.save(carbonActivity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarbonActivity> getUserActivities(String username, String activityType, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(23, 59, 59) : null;

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Specification<CarbonActivity> spec = CarbonActivitySpecification.buildSpecification(user, activityType, startDateTime, endDateTime);
        Sort sort = Sort.by(Sort.Direction.DESC, "registeredAt");
        return carbonRepository.findAll(spec, sort);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateUserTotalCo2(String username, String activityType, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(23, 59, 59) : null;

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Specification<CarbonActivity> spec = CarbonActivitySpecification.buildSpecification(user, activityType, startDateTime, endDateTime);
        List<CarbonActivity> activities = carbonRepository.findAll(spec);

        return activities.stream()
                .map(CarbonActivity::getCo2Generated)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
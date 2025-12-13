package com.tt._2025.b077.huellaspormexico.config.aop;

import com.tt._2025.b077.huellaspormexico.config.aop.services.Co2CalculatorService;
import com.tt._2025.b077.huellaspormexico.modules.carbon.entities.CarbonActivity;
import com.tt._2025.b077.huellaspormexico.modules.carbon.service.CarbonActivityService;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.User;
import com.tt._2025.b077.huellaspormexico.modules.users.repositories.UserRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Aspect
@Component
public class CarbonTrackingAspect {

    private final Logger log = LoggerFactory.getLogger(CarbonTrackingAspect.class);

    private final UserRepository userRepository;
    private final CarbonActivityService carbonService;
    private final Co2CalculatorService co2CalculatorService;

    public CarbonTrackingAspect(
            UserRepository userRepository,
            CarbonActivityService carbonService,
            Co2CalculatorService co2CalculatorService) {
        this.userRepository = userRepository;
        this.carbonService = carbonService;
        this.co2CalculatorService = co2CalculatorService;
    }

    @Around("CarbonTrackingPointcuts.trackableExecution()")
    public Object trackCarbonFootprint(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Runtime runtime = Runtime.getRuntime();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();

        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();

        try {
            User user = getCurrentUser();
            if (user != null) {

                long executionTime = endTime - startTime;
                double memoryUsedMB = Math.max(0, (memoryAfter - memoryBefore) / (1024.0 * 1024.0));

                Co2CalculatorService.ExecutionMetrics metrics = new Co2CalculatorService.ExecutionMetrics(
                        executionTime,
                        memoryUsedMB,
                        0
                );

                BigDecimal co2Amount = co2CalculatorService.calculateMethodCo2(metrics);
                String packageName = joinPoint.getTarget().getClass().getPackage().getName();
                String className = joinPoint.getTarget().getClass().getSimpleName();
                String methodName = joinPoint.getSignature().getName();

                String activityType = co2CalculatorService.determineActivityType(packageName, className, methodName);
                String description = co2CalculatorService.generateDescription(className, methodName);

                CarbonActivity carbonActivity = new CarbonActivity();
                carbonActivity.setUser(user);
                carbonActivity.setCo2Generated(co2Amount);
                carbonActivity.setUnit("KG");
                carbonActivity.setActivityType(activityType);
                carbonActivity.setDescription(description);

                carbonService.saveCarbonActivity(carbonActivity);
                log.debug("CO2 tracked: {} - {} kg CO2 - {}ms", activityType, co2Amount, executionTime);
            }
        } catch (Exception e) {
            log.error("Error tracking carbon footprint: {}", e.getMessage());
        }

        return result;
    }

    private User getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && !authentication.getName().equals("anonymousUser")) {
                String username = authentication.getName();
                return userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
            }
        } catch (Exception e) {
            log.warn("Could not extract user ID: {}", e.getMessage());
        }
        return null;
    }
}
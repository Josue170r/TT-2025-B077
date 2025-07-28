package com.tt._2025.b077.huellaspormexico.config.aop.services;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class Co2CalculatorService {

    private static final double CO2_PER_CPU_MILLISECOND = 0.0000001;
    private static final double CO2_PER_MB_MEMORY = 0.0000005;
    private static final double CO2_PER_DB_QUERY = 0.001;
    private static final double CO2_PER_HTTP_REQUEST = 0.0005;

    public BigDecimal calculateMethodCo2(ExecutionMetrics metrics) {
        double totalCo2 = 0.0;

        totalCo2 += metrics.executionTimeMs() * CO2_PER_CPU_MILLISECOND;
        totalCo2 += metrics.memoryUsedMB() * CO2_PER_MB_MEMORY;
        totalCo2 += metrics.databaseQueries() * CO2_PER_DB_QUERY;
        totalCo2 += CO2_PER_HTTP_REQUEST;

        return BigDecimal.valueOf(totalCo2).setScale(6, RoundingMode.HALF_UP);
    }

    public String determineActivityType(String packageName, String className, String methodName) {
        if (packageName.contains(".controllers")) {
            return "HTTP_REQUEST";
        } else if (packageName.contains(".services")) {
            if (className.toLowerCase().contains("google") || methodName.toLowerCase().contains("google")) {
                return "GOOGLE_API";
            }
            return "BUSINESS_LOGIC";
        } else if (packageName.contains(".repositories")) {
            return "DATABASE_OPERATION";
        } else if (className.toLowerCase().contains("maps") || methodName.toLowerCase().contains("maps")) {
            return "MAPS_SERVICE";
        } else if (className.toLowerCase().contains("places") || methodName.toLowerCase().contains("places")) {
            return "PLACES_SERVICE";
        }
        return "SYSTEM_OPERATION";
    }

    public String generateDescription(String className, String methodName) {
        return className + "." + methodName + "()";
    }

    public record ExecutionMetrics(long executionTimeMs, double memoryUsedMB, int databaseQueries) {
    }
}
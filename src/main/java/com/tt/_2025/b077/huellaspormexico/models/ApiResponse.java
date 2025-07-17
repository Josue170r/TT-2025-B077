package com.tt._2025.b077.huellaspormexico.models;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
public class ApiResponse<T> {
    private final String status;
    private final String message;
    private final T data;
    private final int code;
    private final Instant timestamp;

    private ApiResponse(String status, String message, T data, int code, Instant timestamp) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.code = code;
        this.timestamp = timestamp;
    }

    public static <T> ApiResponse<T> of(HttpStatus httpStatus, String message, T data) {
        return new ApiResponse<>(
                httpStatus.name(),
                message,
                data,
                httpStatus.value(),
                Instant.now()
        );
    }

    public static ApiResponse<Void> of(HttpStatus httpStatus, String message) {
        return new ApiResponse<>(
                httpStatus.name(),
                message,
                null,
                httpStatus.value(),
                Instant.now()
        );
    }
}

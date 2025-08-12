package com.tt._2025.b077.huellaspormexico.models;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
public class ApiResponse<T> {
    private final int status;
    private final String message;
    private final T data;
    private final String code;
    private final Instant timestamp;

    private ApiResponse(int status, String message, T data, String code, Instant timestamp) {
        this.status = status;
        this.message = message != null ? message : "Success";
        this.data = data;
        this.code = code;
        this.timestamp = timestamp;
    }

    public static <T> ApiResponse<T> of(HttpStatus httpStatus, String message, T data) {
        return new ApiResponse<>(
                httpStatus.value(),
                message,
                data,
                httpStatus.name(),
                Instant.now()
        );
    }

    public static ApiResponse<Void> of(HttpStatus httpStatus, String message) {
        return new ApiResponse<>(
                httpStatus.value(),
                message,
                null,
                httpStatus.name(),
                Instant.now()
        );
    }
}

package com.tt._2025.b077.huellaspormexico.models;

import lombok.Data;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

@Data
public class ApiResponse<T> {
    private final String status;
    private final String message;
    private final T data;
    private final int code;
    private final LocalDateTime timestamp;

    private ApiResponse(String status, String message, T data, int code, LocalDateTime timestamp) {
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
                LocalDateTime.now()
        );
    }

    public static ApiResponse<Void> of(HttpStatus httpStatus, String message) {
        return new ApiResponse<>(
                httpStatus.name(),
                message,
                null,
                httpStatus.value(),
                LocalDateTime.now()
        );
    }
}

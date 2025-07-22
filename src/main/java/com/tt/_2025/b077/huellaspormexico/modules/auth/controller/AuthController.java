package com.tt._2025.b077.huellaspormexico.modules.auth.controller;

import com.tt._2025.b077.huellaspormexico.models.ApiResponse;
import com.tt._2025.b077.huellaspormexico.modules.auth.dto.LoginRequest;
import com.tt._2025.b077.huellaspormexico.modules.auth.dto.LoginResponse;
import com.tt._2025.b077.huellaspormexico.modules.auth.dto.RefreshTokenRequest;
import com.tt._2025.b077.huellaspormexico.modules.auth.dto.RefreshTokenResponse;
import com.tt._2025.b077.huellaspormexico.modules.auth.services.AuthService;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    private final AuthService authService;

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody User user) {
        try {
            User registeredUser = authService.registerUser(user);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.of(HttpStatus.CREATED, "Usuario registrado correctamente", registeredUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.of(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @RequestMapping(path = "/refresh", method = RequestMethod.POST)
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        try {
            RefreshTokenResponse response = authService.refreshToken(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.of(HttpStatus.UNAUTHORIZED, "Error en la petición"));
        }
    }
}
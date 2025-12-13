package com.tt._2025.b077.huellaspormexico.modules.auth.controller;

import com.tt._2025.b077.huellaspormexico.models.ApiResponse;
import com.tt._2025.b077.huellaspormexico.modules.auth.dto.*;
import com.tt._2025.b077.huellaspormexico.modules.auth.services.AuthService;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody User user) {
        User registeredUser = authService.registerUser(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.of(HttpStatus.CREATED,
                        "Usuario registrado, revise su correo para verificar su cuenta",
                        registeredUser));
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @RequestMapping(path = "/refresh", method = RequestMethod.POST)
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        RefreshTokenResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(path = "/resend-verification", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<?>> resendVerificationEmail(@Valid @RequestBody ResendVerificationRequest request) {
        authService.resendVerificationEmail(request.getEmail());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, "Correo enviado exitosamente"));
    }

    @RequestMapping(path = "/verify-account", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<?>> verifyUserAccount(@Valid @RequestBody VerifyAccountRequest request) {
        authService.verifyUserAccount(request.getToken());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, "Usuario verificado correctamente"));
    }

    @RequestMapping(path = "/forgot-password", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<?>>  forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, "Correo enviado exitosamente"));
    }

    @RequestMapping(path = "/recover-password", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<?>> recoverPassword(@Valid @RequestBody RecoverPasswordRequest request) {
        authService.recoverPassword(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, "Contraseña modificada correctamente"));
    }
}
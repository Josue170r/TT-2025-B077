package com.tt._2025.b077.huellaspormexico.modules.users.controllers;

import com.tt._2025.b077.huellaspormexico.models.ApiResponse;
import com.tt._2025.b077.huellaspormexico.modules.users.dto.UserPreferencesRequest;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.UserPreferences;
import com.tt._2025.b077.huellaspormexico.modules.users.services.UserPreferencesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/preferences")
public class UserPreferencesController {

    private final UserPreferencesService preferencesService;

    public UserPreferencesController(UserPreferencesService preferencesService) {
        this.preferencesService = preferencesService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<?>>  getPreferences(Authentication authentication) {
        List<UserPreferences> preferences = preferencesService.findByUsername(authentication.getName());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, null, preferences));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<?>> savePreferences(
            @Valid @RequestBody UserPreferencesRequest request,
            Authentication authentication) {
        preferencesService.saveUserPreferences(authentication.getName(), request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, "Preferencias guardadas correctamente"));
    }
}

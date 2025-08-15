package com.tt._2025.b077.huellaspormexico.modules.users.controllers;

import com.tt._2025.b077.huellaspormexico.models.ApiResponse;
import com.tt._2025.b077.huellaspormexico.modules.auth.dto.ChangePasswordRequest;
import com.tt._2025.b077.huellaspormexico.modules.auth.services.AuthService;
import com.tt._2025.b077.huellaspormexico.modules.users.dto.UpdateUserRequest;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.User;
import com.tt._2025.b077.huellaspormexico.modules.users.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @RequestMapping(path = "/profile", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<User>> getProfile(Authentication authentication) {
        User user = userService.getUserProfile(authentication.getName());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, null, user));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<ApiResponse<?>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {

        User user =  userService.updateUser(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, "Usuario actualizado correctamente", user));
    }

    @RequestMapping(path = "/change-password", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<?>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication) {

        authService.changePassword(authentication.getName(), request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, "Contraseña actualizada correctamente"));
    }

    @RequestMapping(path = "/profile/picture", method = RequestMethod.PUT)
    public ResponseEntity<ApiResponse<?>> updateProfilePicture(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {

        userService.updateProfilePicture(authentication.getName(), file);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, "Foto de perfil actualizada correctamente"));
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ApiResponse<?>> deleteUserById(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(HttpStatus.OK, "Usuario eliminado correctamente"));
    }
}

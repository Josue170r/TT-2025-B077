package com.tt._2025.b077.huellaspormexico.modules.auth.services;

import com.tt._2025.b077.huellaspormexico.modules.auth.dto.LoginRequest;
import com.tt._2025.b077.huellaspormexico.modules.auth.dto.LoginResponse;
import com.tt._2025.b077.huellaspormexico.modules.auth.dto.RefreshTokenRequest;
import com.tt._2025.b077.huellaspormexico.modules.auth.dto.RefreshTokenResponse;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.User;
import com.tt._2025.b077.huellaspormexico.modules.users.repositories.UserRepository;
import com.tt._2025.b077.huellaspormexico.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(JwtUtil jwtUtil, UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registers a new user in the system
     */
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está registrado");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Authenticates a user and generates JWT tokens
     */
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            User user = userRepository.findByEmailOrUsername(loginRequest.getEmailOrUsername())
                    .orElseThrow(() -> new BadCredentialsException("Revise sus credenciales"));

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            String accessToken = jwtUtil.generateAccessToken(user.getUsername());
            String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

            return new LoginResponse(accessToken, refreshToken);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Revise sus credenciales");
        }
    }

    /**
     * Refreshes the access token using refresh token
     */
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        try {
            if (!jwtUtil.isTokenValid(refreshToken)) {
                throw new RuntimeException("Refresh token inválido o expirado");
            }

            String username = jwtUtil.extractUsername(refreshToken);
            String tokenType = jwtUtil.extractTokenType(refreshToken);

            if (!"refresh".equals(tokenType)) {
                throw new RuntimeException("Token no es de tipo refresh");
            }

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            if (!jwtUtil.validateRefreshToken(refreshToken, user.getUsername())) {
                throw new RuntimeException("Refresh token inválido");
            }

            String newAccessToken = jwtUtil.generateAccessToken(user.getUsername());

            RefreshTokenResponse response = new RefreshTokenResponse();
            response.setAccessToken(newAccessToken);
            return response;

        } catch (Exception e) {
            throw new RuntimeException("Error al renovar token: " + e.getMessage());
        }
    }

}

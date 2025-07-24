package com.tt._2025.b077.huellaspormexico.modules.auth.services;

import com.tt._2025.b077.huellaspormexico.modules.auth.dto.*;
import com.tt._2025.b077.huellaspormexico.modules.auth.exceptions.*;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.User;
import com.tt._2025.b077.huellaspormexico.modules.users.repositories.UserRepository;
import com.tt._2025.b077.huellaspormexico.modules.users.tasks.UserEmailService;
import com.tt._2025.b077.huellaspormexico.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserEmailService  userEmailService;

    public AuthService(
            JwtUtil jwtUtil,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            UserEmailService userEmailService) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userEmailService = userEmailService;
    }

    /**
     * Registers a new user in the system
     */
    @Transactional
    public User registerUser(User user) {
        try {
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new UserAlreadyExists("El email ya está registrado");
            }

            if (userRepository.existsByUsername(user.getUsername())) {
                throw new UserAlreadyExists("El nombre de usuario ya está registrado");
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.createUserProfile();
            String token = jwtUtil.generateAccessToken(user.getUsername());
            User savedUser = userRepository.save(user);

            userEmailService.send_verification_email(savedUser, token);
            return savedUser;
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar usuario " + e.getMessage());
        }
    }

    /**
     * Authenticates a user and generates JWT tokens
     */
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            User user = userRepository.findByEmailOrUsername(loginRequest.getEmailOrUsername())
                    .orElseThrow(() -> new BadCredentialsException("Revise sus credenciales"));

            if (!user.isVerified()) {
                throw new UserNotVerified("Debe verificar su cuenta");
            }

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
    @Transactional(readOnly = true)
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        try {
            if (!jwtUtil.isTokenValid(refreshToken)) {
                throw new InvalidToken("Refresh token inválido o expirado");
            }

            String username = jwtUtil.extractUsername(refreshToken);
            String tokenType = jwtUtil.extractTokenType(refreshToken);

            if (!"refresh".equals(tokenType)) {
                throw new InvalidToken("Token no es de tipo refresh");
            }

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            if (!jwtUtil.validateRefreshToken(refreshToken, user.getUsername())) {
                throw new InvalidToken("Refresh token inválido");
            }

            String newAccessToken = jwtUtil.generateAccessToken(user.getUsername());
            RefreshTokenResponse response = new RefreshTokenResponse();
            response.setAccessToken(newAccessToken);
            return response;

        } catch (Exception e) {
            throw new RuntimeException("Error al renovar token: " + e.getMessage());
        }
    }

    /**
     * Resend email to verify user account
     */
    public void resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        try {
            String token = jwtUtil.generateAccessToken(user.getUsername());
            userEmailService.send_verification_email(user, token);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar email " + e.getMessage());
        }
    }

    /**
     * Verify user account
     */
    @Transactional
    public void verifyUserAccount(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new InvalidToken("No se proporcionó el token");
        }

        if (!jwtUtil.isTokenValid(token)) {
            throw new InvalidToken("El token ha expirado");
        }

        String username = jwtUtil.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
        if (user.isVerified()) {
            throw new UserAlreadyVerifiedException("El usuario ya está verificado");
        }

        user.setVerified(true);
        userRepository.save(user);
    }

    /**
     * Send email to recover user password
     */
    @Transactional(readOnly = true)
    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        try {
            String token = jwtUtil.generateAccessToken(user.getUsername());
            userEmailService.send_forgot_password_email(user, token);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo" + e.getMessage());
        }
    }

    /**
     * Recover user password
     */
    @Transactional
    public void recoverPassword(String token, RecoverPasswordRequest request) {
        if (token == null || token.trim().isEmpty()) {
            throw new InvalidToken("No se proporcionó el token");
        }

        if (!jwtUtil.isTokenValid(token)) {
            throw new InvalidToken("El token ha expirado");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordMismatchException("Las contraseñas no coinciden");
        }

        String username = jwtUtil.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    /**
     * Change user password after login
     */
    @Transactional
    public void changePassword(String username, ChangePasswordRequest request) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                throw new PasswordMismatchException("Las contraseñas no coinciden");
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            request.getCurrentPassword()
                    )
            );

            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Revise sus credenciales");
        }
    }

}

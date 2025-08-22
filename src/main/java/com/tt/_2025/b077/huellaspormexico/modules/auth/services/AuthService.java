package com.tt._2025.b077.huellaspormexico.modules.auth.services;

import com.tt._2025.b077.huellaspormexico.modules.auth.dto.*;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.User;

public interface AuthService {
    User registerUser(User user);
    LoginResponse login(LoginRequest loginRequest);
    RefreshTokenResponse refreshToken(RefreshTokenRequest request);
    void resendVerificationEmail(String email);
    void verifyUserAccount(String token);
    void forgotPassword(ForgotPasswordRequest request);
    void recoverPassword(RecoverPasswordRequest request);
    void changePassword(String username, ChangePasswordRequest request);
}

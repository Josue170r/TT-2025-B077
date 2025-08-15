package com.tt._2025.b077.huellaspormexico.modules.users.services;

import com.tt._2025.b077.huellaspormexico.modules.users.dto.UpdateUserRequest;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


public interface UserService extends UserDetailsService {
    User updateUser(Long userId, UpdateUserRequest request);
    User getUserProfile(String username);
    void updateProfilePicture(String username, MultipartFile file);
    void deleteById(Long id);
}
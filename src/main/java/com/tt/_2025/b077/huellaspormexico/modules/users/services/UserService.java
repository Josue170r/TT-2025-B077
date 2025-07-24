package com.tt._2025.b077.huellaspormexico.modules.users.services;

import com.tt._2025.b077.huellaspormexico.modules.users.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


@Service
public interface UserService extends UserDetailsService {
    Optional<User> findByUsername(String username);
    User getUserProfile(String username);
    Optional<User> findByEmail(String email);
    void updateProfilePicture(String username, MultipartFile file);
}
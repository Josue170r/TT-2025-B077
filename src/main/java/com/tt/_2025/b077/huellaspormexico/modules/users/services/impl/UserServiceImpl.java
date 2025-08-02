package com.tt._2025.b077.huellaspormexico.modules.users.services.impl;

import com.tt._2025.b077.huellaspormexico.modules.users.dto.UpdateUserRequest;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.User;
import com.tt._2025.b077.huellaspormexico.modules.users.repositories.UserRepository;
import com.tt._2025.b077.huellaspormexico.modules.users.services.FileStorageService;
import com.tt._2025.b077.huellaspormexico.modules.users.services.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public UserServiceImpl(UserRepository userRepository, FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }

    /**
     * Implementation of UserDetailsService for Spring Security
     */
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(new ArrayList<>())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    /**
     * Update user using patch
     */
    @Override
    public User updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("Usuario no encontrado: " + userId)
        );
        if (request.getName() != null && !request.getName().isEmpty()) {
            user.setName(request.getName());
        }

        if (request.getLastName() != null  && !request.getLastName().isEmpty()) {
            user.setLastName(request.getLastName());
        }

        if (request.getSecondLastName() != null && !request.getSecondLastName().isEmpty()) {
            user.setSecondLastName(request.getSecondLastName());
        }

        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        return userRepository.save(user);
    }

    /**
     * Finds user profile
     */
    @Transactional(readOnly = true)
    @Override
    public User getUserProfile(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }

    /**
     * Updates user profile picture
     */
    @Override
    @Transactional
    public void updateProfilePicture(String username, MultipartFile file) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

            if (user.getUserProfile() == null) {
                user.createUserProfile();
            }

            if (user.getUserProfile() != null && user.getUserProfile().getPicture() != null) {
                fileStorageService.deleteProfilePicture(user.getUserProfile().getPicture());
            }

            String pictureUrl = fileStorageService.saveProfilePicture(file, username);
            user.getUserProfile().setPicture(pictureUrl);
            userRepository.save(user);

        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la foto de perfil: " + e.getMessage(), e);
        }
    }
}

package com.tt._2025.b077.huellaspormexico.modules.users.services.impl;

import com.tt._2025.b077.huellaspormexico.modules.users.dto.UpdateUserRequest;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.User;
import com.tt._2025.b077.huellaspormexico.modules.users.enums.UserRole;
import com.tt._2025.b077.huellaspormexico.modules.users.repositories.UserRepository;
import com.tt._2025.b077.huellaspormexico.modules.users.services.FileStorageService;
import com.tt._2025.b077.huellaspormexico.modules.users.services.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
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
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        UserRole role = (user.getUserProfile() != null && user.getUserProfile().getRole() != null)
                ? user.getUserProfile().getRole()
                : UserRole.USER;

        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));

        boolean enabled = Boolean.TRUE.equals(user.getIsActive());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!enabled)
                .build();
    }

    /**
     * Update user using patch
     */
    @Override
    public User updateUser(Long userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

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
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    /**
     * Updates user profile picture
     */
    @Override
    @Transactional
    public void updateProfilePicture(String username, MultipartFile file) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

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

    @Override
    public void deleteById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        userRepository.delete(user);
    }
}

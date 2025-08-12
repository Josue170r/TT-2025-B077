package com.tt._2025.b077.huellaspormexico.modules.users.services.impl;

import com.tt._2025.b077.huellaspormexico.modules.catalogs.entities.CategoryPlacesCatalog;
import com.tt._2025.b077.huellaspormexico.modules.catalogs.repositories.CategoryPlacesRepository;
import com.tt._2025.b077.huellaspormexico.modules.users.dto.UserPreferencesRequest;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.User;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.UserPreferences;
import com.tt._2025.b077.huellaspormexico.modules.users.repositories.UserPreferencesRepository;
import com.tt._2025.b077.huellaspormexico.modules.users.repositories.UserRepository;
import com.tt._2025.b077.huellaspormexico.modules.users.services.UserPreferencesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserPreferencesServiceImpl implements UserPreferencesService {

    private final UserPreferencesRepository preferencesRepository;
    private final UserRepository userRepository;
    private final CategoryPlacesRepository categoryRepository;

    public UserPreferencesServiceImpl(
            UserPreferencesRepository preferencesRepository,
            UserRepository userRepository,
            CategoryPlacesRepository categoryRepository) {
        this.preferencesRepository = preferencesRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<UserPreferences> findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return preferencesRepository.findByUserId(user.getId());
    }

    @Override
    public void saveUserPreferences(String username, UserPreferencesRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Set<Long> currentCategoryIds = user.getPreferences().stream()
                .map(pref -> pref.getCategory().getId())
                .collect(Collectors.toSet());

        Set<Long> newCategoryIds = request.getCategories().stream()
                .map(CategoryPlacesCatalog::getId)
                .collect(Collectors.toSet());

        Set<Long> toRemove = new HashSet<>(currentCategoryIds);
        toRemove.removeAll(newCategoryIds);

        Set<Long> toAdd = new HashSet<>(newCategoryIds);
        toAdd.removeAll(currentCategoryIds);

        if (!toRemove.isEmpty()) {
            user.getPreferences().removeIf(pref -> toRemove.contains(pref.getCategory().getId()));
        }

        for (Long categoryId : toAdd) {
            CategoryPlacesCatalog category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryId));
            user.addPreference(category);
        }
        userRepository.save(user);
    }
}

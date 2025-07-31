package com.tt._2025.b077.huellaspormexico.modules.users.services;

import com.tt._2025.b077.huellaspormexico.modules.users.dto.UserPreferencesRequest;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.UserPreferences;

import java.util.List;

public interface UserPreferencesService {
    List<UserPreferences> findByUsername(String username);
    void saveUserPreferences(String username, UserPreferencesRequest request);
}

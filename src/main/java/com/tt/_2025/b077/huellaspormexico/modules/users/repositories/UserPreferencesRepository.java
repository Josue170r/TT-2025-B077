package com.tt._2025.b077.huellaspormexico.modules.users.repositories;

import com.tt._2025.b077.huellaspormexico.modules.users.entities.UserPreferences;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPreferencesRepository extends JpaRepository<UserPreferences,Integer> {
    List<UserPreferences> findByUserId(Long userId);
}

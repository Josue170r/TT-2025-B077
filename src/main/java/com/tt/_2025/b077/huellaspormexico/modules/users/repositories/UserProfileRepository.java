package com.tt._2025.b077.huellaspormexico.modules.users.repositories;

import com.tt._2025.b077.huellaspormexico.modules.users.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    /**
     * Finds a UserProfile by the associated User's username
     */
    @Query("SELECT up FROM UserProfile up WHERE up.user.username = :username")
    Optional<UserProfile> findByUserUsername(@Param("username") String username);

    /**
     * Finds a UserProfile by the associated User's ID
     */
    @Query("SELECT up FROM UserProfile up WHERE up.user.id = :userId")
    Optional<UserProfile> findByUserId(@Param("userId") Long userId);
}
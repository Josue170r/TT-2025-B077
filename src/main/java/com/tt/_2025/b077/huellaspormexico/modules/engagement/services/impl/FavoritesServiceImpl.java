package com.tt._2025.b077.huellaspormexico.modules.engagement.services.impl;

import com.tt._2025.b077.huellaspormexico.modules.engagement.entities.PlaceFavorite;
import com.tt._2025.b077.huellaspormexico.modules.engagement.repositories.FavoritesRepository;
import com.tt._2025.b077.huellaspormexico.modules.engagement.services.FavoritesService;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.Place;
import com.tt._2025.b077.huellaspormexico.modules.places.exceptions.PlaceNotFoundException;
import com.tt._2025.b077.huellaspormexico.modules.places.reporsitories.PlaceRepository;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.User;
import com.tt._2025.b077.huellaspormexico.modules.users.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoritesServiceImpl implements FavoritesService {

    private final FavoritesRepository favoritesRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    public FavoritesServiceImpl(
            FavoritesRepository favoritesRepository,
            UserRepository userRepository,
            PlaceRepository placeRepository) {
        this.favoritesRepository = favoritesRepository;
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    @Transactional
    public void toggleFavorite(String username, Long placeId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceNotFoundException("Lugar no encontrado: " + placeId));

        Optional<PlaceFavorite> favoriteOpt = favoritesRepository.findByUserIdAndPlaceId(user.getId(), place.getId());

        if (favoriteOpt.isPresent()) {
            favoritesRepository.delete(favoriteOpt.get());
        } else {
            PlaceFavorite favorite = PlaceFavorite.builder()
                    .user(user)
                    .place(place)
                    .build();
            favoritesRepository.save(favorite);
        }
    }

    @Override
    public List<Long> getAllFavoritesIds(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return favoritesRepository.findAllByUserId(user.getId())
                .stream()
                .map((placeFavorite -> placeFavorite.getPlace().getId()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlaceFavorite> getAllByUserId(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return favoritesRepository.findAllByUserId(user.getId(), pageable);
    }
}

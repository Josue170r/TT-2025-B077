package com.tt._2025.b077.huellaspormexico.modules.engagement.services.impl;

import com.tt._2025.b077.huellaspormexico.modules.engagement.entities.PlaceHistory;
import com.tt._2025.b077.huellaspormexico.modules.engagement.repositories.PlaceHistoryRepository;
import com.tt._2025.b077.huellaspormexico.modules.engagement.services.HistoryService;
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

import java.util.Optional;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final PlaceHistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    public HistoryServiceImpl(
            PlaceHistoryRepository historyRepository,
            UserRepository userRepository,
            PlaceRepository placeRepository) {
        this.historyRepository = historyRepository;
        this.userRepository = userRepository;
        this.placeRepository = placeRepository;
    }

    @Override
    @Transactional
    public void toggleHistory(String username, Long placeId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new PlaceNotFoundException("Lugar no encontrado: " + placeId));

        Optional<PlaceHistory> historyOpt = historyRepository.findByUserIdAndPlaceId(user.getId(), place.getId());
        if (historyOpt.isPresent()) {
            historyRepository.delete(historyOpt.get());
        } else {
            PlaceHistory placeHistory = PlaceHistory.builder()
                    .place(place)
                    .user(user)
                    .build();
            historyRepository.save(placeHistory);
        }

    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlaceHistory> getAllByUserId(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return historyRepository.findAllByUserId(user.getId(), pageable);
    }
}

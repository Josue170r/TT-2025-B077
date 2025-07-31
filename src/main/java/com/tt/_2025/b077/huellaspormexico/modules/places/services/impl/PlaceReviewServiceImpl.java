package com.tt._2025.b077.huellaspormexico.modules.places.services.impl;

import com.tt._2025.b077.huellaspormexico.modules.places.dto.PlaceReviewRequest;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.Place;
import com.tt._2025.b077.huellaspormexico.modules.places.entities.PlaceReview;
import com.tt._2025.b077.huellaspormexico.modules.places.exceptions.PlaceNotFoundException;
import com.tt._2025.b077.huellaspormexico.modules.places.reporsitories.PlaceRepository;
import com.tt._2025.b077.huellaspormexico.modules.places.reporsitories.PlaceReviewRepository;
import com.tt._2025.b077.huellaspormexico.modules.places.services.PlaceReviewService;
import com.tt._2025.b077.huellaspormexico.modules.users.entities.User;
import com.tt._2025.b077.huellaspormexico.modules.users.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceReviewServiceImpl implements PlaceReviewService {

    private final PlaceReviewRepository reviewRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    public PlaceReviewServiceImpl(
            PlaceReviewRepository reviewRepository,
            PlaceRepository placeRepository,
            UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;
    }


    @Override
    public List<PlaceReview> findAllByPlaceId(Long id) {
        Place place = placeRepository.findById( id).orElseThrow(
                () -> new PlaceNotFoundException("Place not found with id: " + id)
        );
        return reviewRepository.findAllByPlaceId(place.getId());
    }

    @Override
    public PlaceReview save(Long id, PlaceReviewRequest request, Authentication auth) {
        Place place = placeRepository.findById( id).orElseThrow(
                () -> new PlaceNotFoundException("Place not found with id: " + id)
        );
        User user = userRepository.findByUsername(auth.getName()).orElseThrow(
                () -> new UsernameNotFoundException("User no encontrado " + auth.getName())
        );
        PlaceReview review = PlaceReview.builder()
                .origin("Huellas por México")
                .text(request.getText())
                .rating(request.getRating())
                .timestamp(request.getTimestamp())
                .authorName(user.getFullName())
                .profilePhotoUrl(user.getUserProfile().getPicture())
                .place(place)
                .build();

        return reviewRepository.save(review);
    }
}

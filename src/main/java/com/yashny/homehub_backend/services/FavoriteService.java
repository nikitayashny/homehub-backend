package com.yashny.homehub_backend.services;

import com.yashny.homehub_backend.entities.Favorite;
import com.yashny.homehub_backend.entities.Realt;
import com.yashny.homehub_backend.entities.User;
import com.yashny.homehub_backend.repositories.FavoriteRepository;
import com.yashny.homehub_backend.repositories.RealtRepository;
import com.yashny.homehub_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteService {

    private final RealtRepository realtRepository;
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public void addFavorite(Long userId, Long realtId) {
        Favorite favorite = new Favorite();
        Realt realt = realtRepository.findById(realtId)
                .orElseThrow(() -> new RuntimeException("Realt not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        favorite.setUser(user);
        favorite.setRealt(realt);
        favoriteRepository.save(favorite);
    }

    @Transactional
    public void deleteFavorite(Long userId, Long realtId) {
        Realt realt = realtRepository.findById(realtId)
                .orElseThrow(() -> new RuntimeException("Realt not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        favoriteRepository.deleteByUserAndRealt(user, realt);
    }

    public List<Realt> getFavorites(Long id) {
        // костыль
        String sql = "SELECT realt_id FROM favorites WHERE user_id = :userId";
        List<Long> realtIds = entityManager.createNativeQuery(sql)
                .setParameter("userId", id)
                .getResultList();

        List<Realt> realts = realtRepository.findAllById(realtIds);

        for (Realt realt : realts) {
            realt.setImages(realt.getImages().stream()
                    .map(image -> {
                        image.setRealt(null);
                        return image;
                    })
                    .collect(Collectors.toList()));
        }

        return realts;
    }
}
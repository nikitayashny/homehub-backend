package com.yashny.homehub_backend.repositories;

import com.yashny.homehub_backend.entities.Favorite;
import com.yashny.homehub_backend.entities.Realt;
import com.yashny.homehub_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findAllByUser(User user);
    List<Favorite> findAllByRealt(Realt realt);
    void deleteAllByRealtId(Long realtId);
    void deleteByUserAndRealt(User user, Realt realt);
}

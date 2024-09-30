package com.yashny.homehub_backend.repositories;

import com.yashny.homehub_backend.entities.Realt;
import com.yashny.homehub_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RealtRepository extends JpaRepository<Realt, Long> {
    List<Realt> findByName(String name);

    List<Realt> findAllByUser(User user);
}

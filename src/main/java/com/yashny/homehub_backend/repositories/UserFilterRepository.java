package com.yashny.homehub_backend.repositories;

import com.yashny.homehub_backend.entities.UserFilter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFilterRepository extends JpaRepository<UserFilter, Long> {
    UserFilter findByUserId(Long id);
}
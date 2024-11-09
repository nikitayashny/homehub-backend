package com.yashny.homehub_backend.repositories;

import com.yashny.homehub_backend.entities.UserCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCodeRepository extends JpaRepository<UserCode, Long> {
    Optional<UserCode> findByLogin(String login);
}
package com.yashny.homehub_backend.repositories;

import com.yashny.homehub_backend.entities.Type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeRepository extends JpaRepository<Type, Long> {
}

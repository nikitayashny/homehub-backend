package com.yashny.homehub_backend.repositories;

import com.yashny.homehub_backend.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}

package com.yashny.homehub_backend.repositories;

import com.yashny.homehub_backend.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}

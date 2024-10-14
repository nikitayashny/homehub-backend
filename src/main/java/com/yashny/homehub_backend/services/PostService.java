package com.yashny.homehub_backend.services;

import com.yashny.homehub_backend.dto.PostDto;
import com.yashny.homehub_backend.entities.Post;
import com.yashny.homehub_backend.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    public List<Post> listPosts() {
        return postRepository.findAll();
    }

    public void createPost(Post post) {
        postRepository.save(post);
    }
}

package com.yashny.homehub_backend.controllers;

import com.yashny.homehub_backend.config.UserAuthenticationProvider;
import com.yashny.homehub_backend.dto.PostDto;
import com.yashny.homehub_backend.entities.Post;
import com.yashny.homehub_backend.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;
    private final UserAuthenticationProvider userAuthenticationProvider;

    @GetMapping("/api/news")
    public ResponseEntity<List<Post>> getPosts() {
        return ResponseEntity.ok(postService.listPosts());
    }

    @PostMapping("/api/news")
    public ResponseEntity<List<Post>> createPost(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                                 @ModelAttribute Post post) {
        String token = authorization.substring(7);
        if (userAuthenticationProvider.isAdmin(token)) {
            postService.createPost(post);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        postService.createPost(post);
        return ResponseEntity.ok(postService.listPosts());
    }
}

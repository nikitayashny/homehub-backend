package com.yashny.homehub_backend.controllers;

import com.yashny.homehub_backend.config.UserAuthenticationProvider;
import com.yashny.homehub_backend.dto.PostDto;
import com.yashny.homehub_backend.dto.RealtResponseDto;
import com.yashny.homehub_backend.entities.Post;
import com.yashny.homehub_backend.services.PostService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/news/{id}")
    public ResponseEntity<Response> deleteNews(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                               @PathVariable Long id) throws IOException {
        String token = authorization.substring(7);
        if (userAuthenticationProvider.isAdmin(token)) {
            postService.deletePost(id);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }
}

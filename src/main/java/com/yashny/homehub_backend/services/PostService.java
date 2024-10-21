package com.yashny.homehub_backend.services;

import com.yashny.homehub_backend.dto.PostDto;
import com.yashny.homehub_backend.entities.Post;
import com.yashny.homehub_backend.entities.Realt;
import com.yashny.homehub_backend.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    public List<Post> listPosts() {
        List<Post> posts = postRepository.findAll();
        for (Post post : posts) {
            post.setComments(post.getComments().stream()
                    .map(comment -> {
                        comment.setPost(null);
                        return comment;
                    })
                    .collect(Collectors.toList()));
        }
        return posts;
    }

    public void createPost(Post post) {
        postRepository.save(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}

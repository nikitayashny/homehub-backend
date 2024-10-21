package com.yashny.homehub_backend.services;

import com.yashny.homehub_backend.dto.CommentDto;
import com.yashny.homehub_backend.entities.Comment;
import com.yashny.homehub_backend.entities.Post;
import com.yashny.homehub_backend.entities.Realt;
import com.yashny.homehub_backend.entities.User;
import com.yashny.homehub_backend.repositories.CommentRepository;
import com.yashny.homehub_backend.repositories.PostRepository;
import com.yashny.homehub_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<Comment> getComments(Long id) {
        return commentRepository.findAllByPostId(id);
    }

    public void createComment(CommentDto commentDto) {
        Comment comment = new Comment();
        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(commentDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        comment.setUser(user);
        comment.setPost(post);
        comment.setText(commentDto.getText());
        commentRepository.save(comment);
    }

    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        Post post = comment.getPost();
        post.getComments().remove(comment);
        commentRepository.delete(comment);
    }
}

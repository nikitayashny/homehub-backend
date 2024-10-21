package com.yashny.homehub_backend.controllers;

import com.yashny.homehub_backend.dto.CommentDto;
import com.yashny.homehub_backend.entities.Comment;
import com.yashny.homehub_backend.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("api/comments/{id}")
    public ResponseEntity<List<Comment>> comments(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getComments(id));
    }

    @PostMapping("api/comments")
    public ResponseEntity<Response> addComment(@RequestBody CommentDto commentDto) {
        commentService.createComment(commentDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("api/comments/{id}")
    public ResponseEntity<Response> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok().build();
    }
}

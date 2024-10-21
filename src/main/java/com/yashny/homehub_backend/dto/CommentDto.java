package com.yashny.homehub_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class CommentDto {
    private String text;
    private Long postId;
    private Long userId;
}

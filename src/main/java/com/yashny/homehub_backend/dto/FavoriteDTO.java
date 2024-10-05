package com.yashny.homehub_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class FavoriteDTO {
    private Long userId;
    private Long realtId;
}

package com.yashny.homehub_backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserFilterDto {
    private Long userId;
    private boolean active;
    private int typeId;
    private int dealTypeId;
    private int roomsCount;
    private int maxPrice;
    private String city;


}

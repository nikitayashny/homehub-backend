package com.yashny.homehub_backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfirmDto {
    @NonNull
    private String code;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    private String login;

    @NonNull
    private char[] password;

    @NonNull
    private String phoneNumber;
}

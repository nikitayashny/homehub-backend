package com.yashny.homehub_backend.controllers;

import com.yashny.homehub_backend.dto.UserDto;
import com.yashny.homehub_backend.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@RestController
public class HomeController {

    private final UserService userService;

    @GetMapping("/")
    public ResponseEntity<UserDto> home(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        if (authorization == null) {
            return ResponseEntity.ok().build();
        }
        String token = authorization.substring(7);
        UserDto userDto = new UserDto();
        userDto.setToken(token);
        return ResponseEntity.ok(userDto);
    }
}
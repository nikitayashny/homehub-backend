package com.yashny.homehub_backend.controllers;

import com.yashny.homehub_backend.config.UserAuthenticationProvider;
import com.yashny.homehub_backend.dto.CredentialsDto;
import com.yashny.homehub_backend.dto.SignUpDto;
import com.yashny.homehub_backend.dto.UserDto;
import com.yashny.homehub_backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final UserService userService;
    private final UserAuthenticationProvider userAuthenticationProvider;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody @Validated CredentialsDto credentialsDto) {
        UserDto userDto = userService.login(credentialsDto);
        userDto.setToken(userAuthenticationProvider.createToken(userDto.getLogin(), userDto.getId(), userDto.getRole()));
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Validated SignUpDto user) {
        UserDto createdUser = userService.register(user);
        createdUser.setToken(userAuthenticationProvider.createToken(createdUser.getLogin(), createdUser.getId(), createdUser.getRole()));
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
    }

    @GetMapping("/api/user/auth")
    public ResponseEntity<UserDto> auth(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        if (authorization == null) {
            return ResponseEntity.ok().build();
        }
        String token = authorization.substring(7);
        UserDto userDto = new UserDto();
        userDto.setToken(token);
        return ResponseEntity.ok(userDto);
    }
}

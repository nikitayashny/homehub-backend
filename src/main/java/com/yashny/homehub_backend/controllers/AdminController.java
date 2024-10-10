package com.yashny.homehub_backend.controllers;

import com.yashny.homehub_backend.entities.User;
import com.yashny.homehub_backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class AdminController {

    private final UserService userService;

    @GetMapping("/api/users")
    public ResponseEntity<List<User>> realts() {
        return ResponseEntity.ok(userService.listUsers());
    }
}
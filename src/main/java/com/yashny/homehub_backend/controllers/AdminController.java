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
    public ResponseEntity<List<User>> users() {
        return ResponseEntity.ok(userService.listUsers());
    }

    @PostMapping("/api/users/ban/{id}")
    public ResponseEntity<List<User>> banUser(@PathVariable Long id) {
        userService.banUser(id);
        return ResponseEntity.ok(userService.listUsers());
    }

    @PostMapping("/api/users/change/{id}")
    public ResponseEntity<List<User>> changeUserRole(@PathVariable Long id) {
        userService.changeRole(id);
        return ResponseEntity.ok(userService.listUsers());
    }
}
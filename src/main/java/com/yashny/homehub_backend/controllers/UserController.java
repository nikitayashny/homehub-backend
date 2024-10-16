package com.yashny.homehub_backend.controllers;

import com.yashny.homehub_backend.entities.Realt;
import com.yashny.homehub_backend.entities.User;
import com.yashny.homehub_backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @GetMapping("/api/user/{id}")
    public ResponseEntity<User> getOneUser(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping("/api/user/realts/{id}")
    public ResponseEntity<List<Realt>> usersRealts(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUsersRealts(id));
    }
}

package com.yashny.homehub_backend.controllers;

import com.yashny.homehub_backend.entities.Realt;
import com.yashny.homehub_backend.entities.User;
import com.yashny.homehub_backend.services.RealtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class RealtController {

    private final RealtService realtService;

    @GetMapping("/api/realt")
    public ResponseEntity<List<Realt>> realts() {
        return ResponseEntity.ok(realtService.listRealts());
    }

    @PostMapping("/api/realt/create")
    public ResponseEntity<String> createRealt(@RequestParam("file1") MultipartFile file1,
                                              @RequestParam("file2") MultipartFile file2,
                                              @RequestParam("file3") MultipartFile file3,
                                              @ModelAttribute Realt realt,
                                              @RequestParam Long userId) throws IOException {
        realtService.saveRealt(userId, realt, file1, file2, file3);
        return ResponseEntity.ok("Success");
    }
}

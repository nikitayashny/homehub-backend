package com.yashny.homehub_backend.controllers;

import com.yashny.homehub_backend.dto.RealtResponseDto;
import com.yashny.homehub_backend.entities.Realt;
import com.yashny.homehub_backend.services.RealtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class RealtController {

    private final RealtService realtService;

    @GetMapping("/api/realt")
    public ResponseEntity<RealtResponseDto> realts(@RequestParam Long limit,
                                                   @RequestParam Long page,
                                                   @RequestParam Long selectedType,
                                                   @RequestParam Long selectedDealType,
                                                   @RequestParam Long roomsCount,
                                                   @RequestParam Long maxPrice,
                                                   @RequestParam Long sortType,
                                                   @RequestParam Long userId) {
        return ResponseEntity.ok(realtService.listRealts(limit, page, selectedType, selectedDealType, roomsCount,
                maxPrice, sortType, userId));
    }

    @GetMapping("/api/realt/{id}")
    public ResponseEntity<Realt> realt(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(realtService.getRealt(id));
    }

    @PostMapping("/api/realt/create")
    public ResponseEntity<RealtResponseDto> createRealt(@RequestParam("file1") MultipartFile file1,
                                              @RequestParam("file2") MultipartFile file2,
                                              @RequestParam("file3") MultipartFile file3,
                                              @ModelAttribute Realt realt,
                                              @RequestParam Long userId) throws IOException {
        realtService.saveRealt(userId, realt, file1, file2, file3);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/realt/delete/{id}")
    public ResponseEntity<RealtResponseDto> deleteRealt(@PathVariable Long id) throws IOException {
        realtService.deleteRealt(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("api/realt/like/{id}")
    public ResponseEntity<?> likeRealt(@PathVariable Long id) {
        realtService.likeRealt(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("api/realt/view/{id}")
    public ResponseEntity<?> viewRealt(@PathVariable Long id) {
        realtService.viewRealt(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("api/realt/repost/{id}")
    public ResponseEntity<?> repostRealt(@PathVariable Long id) {
        realtService.repostRealt(id);
        return ResponseEntity.ok().build();
    }
}

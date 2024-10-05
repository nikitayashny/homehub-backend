package com.yashny.homehub_backend.controllers;

import com.yashny.homehub_backend.dto.FavoriteDTO;
import com.yashny.homehub_backend.entities.Realt;
import com.yashny.homehub_backend.services.FavoriteService;
import com.yashny.homehub_backend.services.RealtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final RealtService realtService;

    @GetMapping("/api/favorite/{id}")
    public ResponseEntity<List<Realt>> favorites(@PathVariable Long id) {
        return ResponseEntity.ok(favoriteService.getFavorites(id));
    }

    @PostMapping("/api/favorite/add")
    public ResponseEntity<List<Realt>> addFavorite(@RequestBody FavoriteDTO favoriteDTO) {
        favoriteService.addFavorite(favoriteDTO.getUserId(), favoriteDTO.getRealtId());
        return ResponseEntity.ok(favoriteService.getFavorites(favoriteDTO.getUserId()));
    }

    @PostMapping("/api/favorite/delete")
    public ResponseEntity<List<Realt>> deleteFavorite(@RequestBody FavoriteDTO favoriteDTO) {
        favoriteService.deleteFavorite(favoriteDTO.getUserId(), favoriteDTO.getRealtId());
        return ResponseEntity.ok(favoriteService.getFavorites(favoriteDTO.getUserId()));
    }
}

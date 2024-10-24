package com.yashny.homehub_backend.controllers;

import com.yashny.homehub_backend.dto.UserFilterDto;
import com.yashny.homehub_backend.entities.UserFilter;
import com.yashny.homehub_backend.services.UserFilterService;
import lombok.RequiredArgsConstructor;
import org.eclipse.angus.mail.iap.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class UserFilterController {
    private final UserFilterService userFilterService;

    @GetMapping("/api/userfilter/{id}")
    public ResponseEntity<UserFilter> getUsersFilters(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok(userFilterService.getUserFilters(id));
    }

    @PostMapping("/api/userfilter")
    public ResponseEntity<Response> setUsersFilters(@ModelAttribute UserFilterDto userFilterDto) {
        userFilterService.setUserFilter(userFilterDto);
        return ResponseEntity.ok().build();
    }
}

package com.yashny.homehub_backend.controllers;

import com.yashny.homehub_backend.config.UserAuthenticationProvider;
import com.yashny.homehub_backend.dto.ConfirmDto;
import com.yashny.homehub_backend.dto.CredentialsDto;
import com.yashny.homehub_backend.dto.SignUpDto;
import com.yashny.homehub_backend.dto.UserDto;
import com.yashny.homehub_backend.entities.UserCode;
import com.yashny.homehub_backend.repositories.UserCodeRepository;
import com.yashny.homehub_backend.repositories.UserRepository;
import com.yashny.homehub_backend.services.EmailSenderService;
import com.yashny.homehub_backend.services.UserCodeService;
import com.yashny.homehub_backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Random;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final UserService userService;
    private final UserAuthenticationProvider userAuthenticationProvider;
    private final EmailSenderService emailSenderService;
    private final UserCodeService userCodeService;
    private final UserRepository userRepository;
    private final UserCodeRepository userCodeRepository;
    private final Random random = new Random();

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody @Validated CredentialsDto credentialsDto) {
        UserDto userDto = userService.login(credentialsDto);
        String name = userDto.getFirstName() + ' ' + userDto.getLastName();
        if (userDto.isActive()) {
            userDto.setToken(userAuthenticationProvider.createToken(userDto.getLogin(), userDto.getId(), userDto.getRole(), name));
            return ResponseEntity.ok(userDto);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .build();
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Validated SignUpDto user) {
        if (userRepository.findByLogin(user.getLogin()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        String confirmationCode = String.valueOf(100000 + random.nextInt(900000));
        emailSenderService.sendConfirmationCode(user.getLogin(), confirmationCode);
        userCodeService.create(user.getLogin(), confirmationCode);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm")
    public ResponseEntity confirm(@RequestBody @Validated ConfirmDto user) {
        UserCode userCode = userCodeService.findByLogin(user.getLogin());
        if (!userCode.getCode().equals(user.getCode())) {
            return ResponseEntity.badRequest().body("Неверный код подтверждения");
        }
        SignUpDto newUser = new SignUpDto();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setLogin(user.getLogin());
        newUser.setPassword(user.getPassword());
        newUser.setPhoneNumber(user.getPhoneNumber());
        UserDto createdUser = userService.register(newUser);
        String name = createdUser.getFirstName() + ' ' + createdUser.getLastName();
        createdUser.setToken(userAuthenticationProvider.createToken(createdUser.getLogin(), createdUser.getId(), createdUser.getRole(), name));

        userCodeRepository.delete(userCode);

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

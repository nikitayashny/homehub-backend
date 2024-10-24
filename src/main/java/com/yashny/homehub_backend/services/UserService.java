package com.yashny.homehub_backend.services;

import com.yashny.homehub_backend.dto.CredentialsDto;
import com.yashny.homehub_backend.dto.SignUpDto;
import com.yashny.homehub_backend.dto.UserDto;
import com.yashny.homehub_backend.entities.Realt;
import com.yashny.homehub_backend.entities.User;
import com.yashny.homehub_backend.entities.UserFilter;
import com.yashny.homehub_backend.exceptions.AppException;
import com.yashny.homehub_backend.mappers.UserMapper;
import com.yashny.homehub_backend.repositories.RealtRepository;
import com.yashny.homehub_backend.repositories.UserFilterRepository;
import com.yashny.homehub_backend.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RealtRepository realtRepository;
    private final UserFilterRepository userFilterRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public UserDto login(CredentialsDto credentialsDto) {
        User user = userRepository.findByLogin(credentialsDto.getLogin())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.getPassword()), user.getPassword())) {
            return userMapper.toUserDto(user);
        }
        throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
    }

    public UserDto register(SignUpDto userDto) {
        Optional<User> optionalUser = userRepository.findByLogin(userDto.getLogin());

        if (optionalUser.isPresent()) {
            throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.signUpToUser(userDto);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.getPassword())));
        user.setActive(true);
        user.setRole("USER");
        User savedUser = userRepository.save(user);

        UserFilter userFilter = new UserFilter();
        userFilter.setUser(user);
        userFilterRepository.save(userFilter);

        return userMapper.toUserDto(savedUser);
    }

    public UserDto findByLogin(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Realt not found with id " + id));
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public void changeRole(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
        if (Objects.equals(user.getRole(), "USER")) {
            user.setRole("ADMIN");
        } else {
            user.setRole("USER");
        }
        userRepository.save(user);
    }

    public void banUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
        if (user.isActive()) {
            user.setActive(false);
        } else {
            user.setActive(true);
        }
        userRepository.save(user);
    }

    public List<Realt> getUsersRealts(Long id) {
        List<Realt> realts = realtRepository.findAll();

        realts = realts.stream()
                .filter(realt -> id.equals(realt.getUser().getId()))
                .collect(Collectors.toList());

        for (Realt realt : realts) {
            realt.setImages(realt.getImages().stream()
                    .map(image -> {
                        image.setRealt(null);
                        return image;
                    })
                    .collect(Collectors.toList()));
        }
        return realts;
    }
}

package com.yashny.homehub_backend.data;

import com.yashny.homehub_backend.entities.DealType;
import com.yashny.homehub_backend.entities.Type;
import com.yashny.homehub_backend.entities.User;
import com.yashny.homehub_backend.repositories.DealTypeRepository;
import com.yashny.homehub_backend.repositories.TypeRepository;
import com.yashny.homehub_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private DealTypeRepository dealTypeRepository;
    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (typeRepository.count() == 0) {
            typeRepository.save(new Type(null, "Квартира"));
            typeRepository.save(new Type(null, "Дом"));
        }
        if (dealTypeRepository.count() == 0) {
            dealTypeRepository.save(new DealType(null, "Продажа"));
            dealTypeRepository.save(new DealType(null, "Аренда"));
        }
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setActive(true);
            admin.setLogin("admin@gmail.com");
            admin.setFirstName("Администратор");
            admin.setLastName("Главный");
            admin.setPassword(passwordEncoder.encode("1"));
            admin.setPhoneNumber("+375444444444");
            admin.setRole("ADMIN");
            userRepository.save(admin);
        }
    }
}

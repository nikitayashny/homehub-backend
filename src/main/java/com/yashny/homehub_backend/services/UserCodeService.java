package com.yashny.homehub_backend.services;

import com.yashny.homehub_backend.entities.UserCode;
import com.yashny.homehub_backend.repositories.UserCodeRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserCodeService {

    private final UserCodeRepository userCodeRepository;

    public void create(@NonNull String login, String confirmationCode) {
        if (userCodeRepository.findByLogin(login).isPresent()) {
            UserCode userCode = userCodeRepository.findByLogin(login).orElseThrow();
            userCode.setCode(confirmationCode);
            userCodeRepository.save(userCode);
        }
        else {
            UserCode userCode = new UserCode();
            userCode.setLogin(login);
            userCode.setCode(confirmationCode);
            userCodeRepository.save(userCode);
        }

    }

    public UserCode findByLogin(@NonNull String login) {
        return userCodeRepository.findByLogin(login).orElseThrow();
    }
}

package com.yashny.homehub_backend.services;

import com.yashny.homehub_backend.entities.Realt;
import com.yashny.homehub_backend.entities.User;
import com.yashny.homehub_backend.entities.Favorite;
import com.yashny.homehub_backend.repositories.FavoriteRepository;
import com.yashny.homehub_backend.repositories.RealtRepository;
import com.yashny.homehub_backend.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.yashny.homehub_backend.entities.Image;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RealtService {

    private final RealtRepository realtRepository;
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;

    public List<Realt> listRealts() {
        List<Realt> realts = realtRepository.findAll();

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

    public void saveRealt(Long userId, Realt realt, MultipartFile file1, MultipartFile file2, MultipartFile file3) throws IOException {
        User user = userRepository.findById(userId).orElse(null);
        realt.setUser(user);
        Image image1;
        Image image2;
        Image image3;
        if (file1.getSize() != 0) {
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            realt.addImageToRealt(image1);
        }
        if (file2.getSize() != 0) {
            image2 = toImageEntity(file2);
            realt.addImageToRealt(image2);
        }
        if (file3.getSize() != 0) {
            image3 = toImageEntity(file3);
            realt.addImageToRealt(image3);
        }
        log.info("Saving new Product. Name: {}; email: {};", realt.getName(), realt.getUser().getLogin());
        Realt realtFromDb = realtRepository.save(realt);
        realtFromDb.setPreviewImageId(realtFromDb.getImages().get(0).getId());
        realtRepository.save(realt);
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    @Transactional
    public void deleteRealt(Long id) {
        Realt realt = realtRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Realt not found with id " + id));

        favoriteRepository.deleteAllByRealtId(id);

        realtRepository.delete(realt);
    }

    public Realt getRealt(Long id) {
        Realt realt = realtRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Realt not found with id " + id));

        realt.setImages(realt.getImages().stream()
                .map(image -> {
                    image.setRealt(null);
                    return image;
                })
                .collect(Collectors.toList()));

        return realt;
    }
}

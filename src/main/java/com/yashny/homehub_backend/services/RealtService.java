package com.yashny.homehub_backend.services;

import com.yashny.homehub_backend.dto.RealtResponseDto;
import com.yashny.homehub_backend.entities.*;
import com.yashny.homehub_backend.repositories.FavoriteRepository;
import com.yashny.homehub_backend.repositories.RealtRepository;
import com.yashny.homehub_backend.repositories.UserFilterRepository;
import com.yashny.homehub_backend.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final UserFilterRepository userFilterRepository;
    private final EmailSenderService emailSenderService;

    public RealtResponseDto listRealts(Long limit, Long page, Long selectedType, Long selectedDealType, Long roomsCount,
                                       Long maxPrice, Long userId) {
        if (limit <= 0 || page <= 0) {
            throw new IllegalArgumentException("Limit must be greater than 0 and page must be 0 or greater.");
        }

        int start = Math.toIntExact(page * limit - limit);
        List<Realt> realts = realtRepository.findAll();

        if (selectedType != 0) {
            realts = realts.stream()
                    .filter(realt -> selectedType.equals(realt.getType().getId()))
                    .collect(Collectors.toList());
        }
        if (selectedDealType != 0) {
            realts = realts.stream()
                    .filter(realt -> selectedDealType.equals(realt.getDealType().getId()))
                    .collect(Collectors.toList());
        }
        if (roomsCount != 0 && roomsCount < 5) {
            realts = realts.stream()
                    .filter(realt -> roomsCount == realt.getRoomsCount())
                    .collect(Collectors.toList());
        }
        if (roomsCount >= 5) {
            realts = realts.stream()
                    .filter(realt -> roomsCount <= realt.getRoomsCount())
                    .collect(Collectors.toList());
        }
        if (maxPrice != -1) {
            realts = realts.stream()
                    .filter(realt -> maxPrice >= realt.getPrice())
                    .collect(Collectors.toList());
        }

        if (userId != 0) {
            realts = realts.stream()
                    .filter(realt -> userId.equals(realt.getUser().getId()))
                    .collect(Collectors.toList());
        }

        long totalCount = realts.size();

        List<Realt> paginatedRealts = realts.stream()
                .skip(start)
                .limit(limit)
                .collect(Collectors.toList());

        for (Realt realt : paginatedRealts) {
            realt.setImages(realt.getImages().stream()
                    .map(image -> {
                        image.setRealt(null);
                        return image;
                    })
                    .collect(Collectors.toList()));
        }

        return new RealtResponseDto(paginatedRealts, totalCount);
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

        List<UserFilter> userFilterList = userFilterRepository.findAll();
        List<UserFilter> activeUserFilters = userFilterList.stream()
                .filter(UserFilter::isActive)
                .toList();

        for (UserFilter filter : activeUserFilters) {
            boolean matches = true;

            if (filter.getCity() != null && !filter.getCity().isEmpty() && !filter.getCity().equals(realt.getCity())) {
                matches = false;
            }

            if (filter.getMaxPrice() != 0 && filter.getMaxPrice() < realt.getPrice()) {
                matches = false;
            }

            if (filter.getRoomsCount() != 0 && filter.getRoomsCount() != realt.getRoomsCount()) {
                matches = false;
            }

            if (filter.getType() != null && filter.getType() != realt.getType()) {
                matches = false;
            }

            if (filter.getDealType() != null && filter.getDealType() != realt.getDealType()) {
                matches = false;
            }

            if (matches) {
                String subject = "Подходящее для вас объявление!";
                String body = String.format(
                        "Здравствуйте, %s!\n\n" +
                                "Мы рады сообщить, что найдено новое объявление, подходящее под ваши фильтры:\n" +
                                "Название: %s\n" +
                                "Цена: %d\n" +
                                "Количество комнат: %d\n" +
                                "Город: %s\n" +
                                "Посмотреть можно по ссылке: %s%d\n\n" +
                                "С уважением,\n" +
                                "HomeHub.",
                        filter.getUser().getFirstName(),
                        realt.getName(),
                        realt.getPrice(),
                        realt.getRoomsCount(),
                        realt.getCity(),
                        "http://localhost:3000/realt/", realt.getId()
                );
                emailSenderService.sendEmail(filter.getUser().getLogin(), subject, body);
            }
        }
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

    public void likeRealt(Long id) {
        Realt realt = realtRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Realt not found with id " + id));
        
        realt.setLikes(realt.getLikes() + 1);
        realtRepository.save(realt);
    }

    public void viewRealt(Long id) {
        Realt realt = realtRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Realt not found with id " + id));

        realt.setViews(realt.getViews() + 1);
        realtRepository.save(realt);
    }

    public void repostRealt(Long id) {
        Realt realt = realtRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Realt not found with id " + id));

        realt.setReposts(realt.getReposts() + 1);
        realtRepository.save(realt);
    }
}
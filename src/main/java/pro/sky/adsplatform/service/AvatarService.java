package pro.sky.adsplatform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.adsplatform.entity.AvatarEntity;
import pro.sky.adsplatform.entity.UserEntity;
import pro.sky.adsplatform.exception.NotFoundException;
import pro.sky.adsplatform.repository.AvatarRepository;
import pro.sky.adsplatform.utility.ImageUtility;

import java.io.IOException;

/**
 * Сервис для работы с аватарами пользователей.
 */
@Service
public class AvatarService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AvatarService.class);

    private final AvatarRepository avatarRepository;

    public AvatarService(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }

    /**
     * Возвращает аватар по указанному ID.
     *
     * @param id ID аватара.
     * @return аватар.
     * @throws NotFoundException аватар с указанным ID отсутствует в базе.
     */
    public AvatarEntity findAvatar(Long id) {
        return avatarRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User avatar not found"));
    }

    /**
     * Обновляет аватар пользователя.
     *
     * @param user пользователь.
     * @param file файл изображения.
     * @throws NotFoundException не удалось извлечь содержимое изображения.
     */
    public void updateAvatar(UserEntity user, MultipartFile file) {
        byte[] imageContent;
        try {
            imageContent = ImageUtility.getThumbnailImageContent(file);
        } catch (IOException e) {
            throw new NotFoundException("Failed to extract avatar contents");
        }

        AvatarEntity avatarBD = avatarRepository.findById(user.getId()).orElse(new AvatarEntity());
        avatarBD.setUser(user);
        avatarBD.setImage(imageContent);

        avatarRepository.save(avatarBD);
    }
}
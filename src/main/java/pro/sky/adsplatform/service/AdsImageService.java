package pro.sky.adsplatform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.adsplatform.entity.AdsEntity;
import pro.sky.adsplatform.entity.AdsImageEntity;
import pro.sky.adsplatform.exception.NotFoundException;
import pro.sky.adsplatform.repository.AdsImageRepository;
import pro.sky.adsplatform.utility.ImageUtility;

import java.io.IOException;

/**
 * Сервис для работы с изображениями.
 */
@Service
public class AdsImageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdsImageService.class);

    private final AdsImageRepository adsImageRepository;

    public AdsImageService(AdsImageRepository adsImageRepository) {
        this.adsImageRepository = adsImageRepository;
    }

    /**
     * Возвращает изображение по указанному ID.
     *
     * @param id ID изображения.
     * @return изображение.
     * @throws NotFoundException изображение с указанным ID отсутствует в базе.
     */
    public AdsImageEntity findImage(Long id) {
        return adsImageRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Image not found"));
    }

    /**
     * Создает новое изображение для объявления.
     *
     * @param ads  объявление.
     * @param file файл изображения.
     * @return ID созданного изображения.
     * @throws NotFoundException не удалось извлечь содержимое изображения.
     */
    public String createImage(AdsEntity ads, MultipartFile file) {
        byte[] imageContent;
        try {
            imageContent = ImageUtility.getImageContent(file);
        } catch (IOException e) {
            throw new NotFoundException("Failed to extract image contents");
        }

        AdsImageEntity adsImage = new AdsImageEntity();
        adsImage.setAds(ads);
        adsImage.setImage(imageContent);

        return adsImageRepository.save(adsImage).getId().toString();
    }
}

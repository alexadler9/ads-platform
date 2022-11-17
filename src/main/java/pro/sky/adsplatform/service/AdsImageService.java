package pro.sky.adsplatform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.adsplatform.entity.AdsEntity;
import pro.sky.adsplatform.entity.AdsImageEntity;
import pro.sky.adsplatform.repository.AdsImageRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.datical.liquibase.ext.init.InitProjectUtil.getExtension;

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
     * Создает новое изображение для объявления.
     *
     * @param ads объявление.
     * @param file файл изображения.
     * @return ID созданного изображения.
     */
    public String createImage(AdsEntity ads, MultipartFile file) throws IOException {
        byte[] imageContent = getImageContent(file);

        AdsImageEntity adsImage = new AdsImageEntity();
        adsImage.setAds(ads);
        adsImage.setImage(imageContent);

        return adsImageRepository.save(adsImage).getId().toString();
    }

    /**
     * Возвращает содержимое изображения.
     *
     * @param file файл изображения.
     * @return содержимое изображения.
     */
    private byte[] getImageContent(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        String fileNameOriginal = file.getOriginalFilename();
        String ext = getExtension(fileNameOriginal);

        byte[] imageByte = file.getBytes();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream bais = new ByteArrayInputStream(imageByte);

        BufferedImage imgIn = ImageIO.read(bais);
        if (imgIn == null) {
            return null;
        }

        double height = imgIn.getHeight() / (imgIn.getWidth() / 100d);
        BufferedImage imgOut = new BufferedImage(100, (int) height, imgIn.getType());
        Graphics2D graphics = imgOut.createGraphics();
        graphics.drawImage((Image) imgIn, 0, 0, 100, (int) height, null);
        graphics.dispose();

        ImageIO.write(imgOut, ext, baos);

        return baos.toByteArray();
    }

    /**
     * Возвращает изображение по указанному ID.
     *
     * @param id ID изображения.
     * @return изображение. Может вернуть null, если такое изображение отсутствует.
     */
    public AdsImageEntity findImage(Long id) {
        return adsImageRepository.findById(id).orElse(null);
    }
}

package pro.sky.adsplatform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
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
import java.util.Collections;
import java.util.List;

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

//IMAGE
    public String saveAddFile(AdsEntity adsEntity, MultipartFile file) throws IOException {

        AdsImageEntity adsImageEntity = new AdsImageEntity();

        adsImageEntity.setAds(adsEntity);

        byte[] imageByte = getImageByteLite(file);

        adsImageEntity.setImage(imageByte);
        adsImageRepository.save(adsImageEntity);
return adsImageEntity.getId().toString();
    }


    public byte[] getImageByteLite(MultipartFile file) throws IOException {

        String ContentType = file.getContentType();
        String fileNameOriginal = file.getOriginalFilename();
        String ext = getExtension(fileNameOriginal);

        byte[] imageByte = file.getBytes();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream bais = new ByteArrayInputStream(imageByte);



        BufferedImage imgIn = ImageIO.read(bais);
        if (imgIn == null) return null;

        double height = imgIn.getHeight() / (imgIn.getWidth() / 100d);
        BufferedImage imgOut = new BufferedImage(100, (int) height, imgIn.getType());
        Graphics2D graphics = imgOut.createGraphics();
        graphics.drawImage((Image) imgIn, 0, 0, 100, (int) height, null);
        graphics.dispose();

        ImageIO.write(imgOut, ext, baos);
        byte[] imageByteOut = baos.toByteArray();

        return imageByteOut;
    };


    public AdsImageEntity  getImageEntity(Long no) {
       return adsImageRepository.findById(no).orElse(null);
            }

}

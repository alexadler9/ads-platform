package pro.sky.adsplatform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.adsplatform.entity.AdsEntity;
import pro.sky.adsplatform.entity.AdsImageEntity;
import pro.sky.adsplatform.repository.AdsImageRepository;

import java.util.Collections;
import java.util.List;

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
    public void saveAddFile(AdsEntity adsEntity, MultipartFile file) {

        AdsImageEntity adsImageEntity = new AdsImageEntity();

        adsImageEntity.setAds(adsEntity);

        byte[] imageByte = getImageByte(file);
        adsImageEntity.setImage(imageByte);
        adsImageRepository.save(adsImageEntity);

    }

    public byte[] getImageByte(MultipartFile file){
        return null;
    };


    public AdsImageEntity  getImageEntity(Long no) {
       return adsImageRepository.findById(no).orElse(null);
            }
}

package pro.sky.adsplatform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.adsplatform.repository.AdsImageRepository;

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
}

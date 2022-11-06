package pro.sky.adsplatform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.adsplatform.entity.AdsCommentEntity;
import pro.sky.adsplatform.entity.AdsEntity;
import pro.sky.adsplatform.repository.AdsRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Сервис для работы с объявлениями.
 */
@Service
public class AdsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdsService.class);

    private final AdsRepository adsRepository;

    public AdsService(AdsRepository adsRepository) {
        this.adsRepository = adsRepository;
    }

    /**
     * Возвращает объявление по указанному ID.
     *
     * @param id ID объявления.
     * @return объявление. Может вернуть null, если такое объявление отсутствует.
     */
    public AdsEntity getAds(long id) {
        return adsRepository.findById(id).orElse(null);
    }

    /**
     * Возвращает все объявления.
     *
     * @return список всех объявлений.
     */
    public List<AdsEntity> getAllAds() {
        return adsRepository.findAll();
    }

    /**
     * Сохраняет обьявление.
     */
    public void saveAddAdsUsingPOST(AdsEntity adsEntity) {
        adsRepository.save(adsEntity);

    }

    /**
     * удаляет обьявление.
     */
    public void removeAdsUsingDELETE(AdsEntity adsEntity) {
        adsRepository.delete(adsEntity);

    }
}

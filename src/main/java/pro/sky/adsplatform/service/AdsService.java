package pro.sky.adsplatform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.adsplatform.dto.CreateAdsDto;
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
    public AdsEntity findAds(long id) {
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
//IMAGE
    public AdsEntity  saveAddAds(AdsEntity adsEntity) {
        adsRepository.save(adsEntity);
        return adsEntity;

    }

    /**
     * удаляет обьявление.
     */
    public void removeAdsUsingDELETE(AdsEntity adsEntity) {
        adsRepository.delete(adsEntity);

    }

    /**
     * Возвращает все объявления по шаблону
     *
     * @return список всех объявлений.
     */
    public List<AdsEntity> findAllByTitleLike(String title) {
        return adsRepository.findAllByTitleLike("%"+title+"%");
    }


}

package pro.sky.adsplatform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
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
     * Добавить новое объявление.
     *
     * @param ads новое объявление.
     */
    public void addAds(AdsEntity ads) {
        throw new UnsupportedOperationException("ads add is not supported");
    }

    /**
     * Обновить данные объявления.
     *
     * @param id ID объявления.
     * @param ads новые данные объявления.
     */
    public void updateAds(long id, AdsEntity ads) {
        throw new UnsupportedOperationException("ads update is not supported");
    }

    /**
     * Удалить объявление.
     *
     * @param id ID объявления.
     */
    @Transactional
    public void deleteAds(Long id) {
        adsRepository.deleteById(id);
    }
}

package pro.sky.adsplatform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.adsplatform.entity.AdsEntity;
import pro.sky.adsplatform.entity.UserEntity;
import pro.sky.adsplatform.exception.NoContentException;
import pro.sky.adsplatform.exception.NotFoundException;
import pro.sky.adsplatform.repository.AdsRepository;

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
        return adsRepository.findById(id).orElseThrow(
                ()-> new NotFoundException("Not found"));
    }
    public AdsEntity findAdsContent(long id) {
        return adsRepository.findById(id).orElseThrow(
                ()-> new NoContentException("No content"));
    }

    /**
     * Возвращает все объявления.
     *
     * @return список всех объявлений.
     */
    public List<AdsEntity> findAllAds() {
        return adsRepository.findAll();
    }

    /**
     * Возвращает все объявления автора.
     *
     * @param user автор.
     * @return список всех объявлений автора.
     */
    public List<AdsEntity> findAllAdsByAuthor(UserEntity user) {
        return adsRepository.findAllByAuthor(user);
    }

    /**
     * Возвращает все объявления, соответствующие шаблону.
     *
     * @param title шаблон.
     * @return список всех объявлений, соответствующих шаблону.
     */
    public List<AdsEntity> findAllAdsByTitleLike(String title) {
        return adsRepository.findAllByTitleLikeIgnoreCase("%" + title + "%");
    }

    /**
     * Создает новое объявление.
     *
     * @param ads новое объявление.
     * @return созданное объявление.
     */
    public AdsEntity createAds(AdsEntity ads) {
        ads.setId(null);
        return adsRepository.save(ads);
    }

    /**
     * Обновляет содержание объявления (поля price, title и description).
     *
     * @param ads обновленные данные объявления.
     * @param id  ID объявления.
     * @return обновленное объявление.
     * @throws IllegalArgumentException несооветствие значений ID entity и аргумента.
     * @throws NotFoundException        объявление с указанными параметрами отсутствует в базе.
     */
    public AdsEntity updateAds(AdsEntity ads, long id) {
        if (ads.getId() != id) {
            LOGGER.error("Несоответствие значений ID объявления");
            throw new IllegalArgumentException("Несоответствие значений ID объявления");
        }

        AdsEntity adsBD = findAdsContent(id);

        adsBD.setPrice(ads.getPrice());

        adsBD.setTitle(ads.getTitle());

        adsBD.setTitle(ads.getDescription());

        return adsRepository.save(adsBD);
    }

    /**
     * Удаляет объявление.
     *
     * @param ads объявление, которое должно быть удалено.
     */
    public void deleteAds(AdsEntity ads) {
        adsRepository.delete(ads);
    }
}

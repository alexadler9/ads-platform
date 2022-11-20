package pro.sky.adsplatform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.adsplatform.entity.AdsEntity;
import pro.sky.adsplatform.entity.UserEntity;
import pro.sky.adsplatform.exception.NotFoundException;
import pro.sky.adsplatform.repository.AdsRepository;

import javax.naming.NotContextException;
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
     * @return объявление.
     * @throws NotFoundException объявление с указанным ID отсутствует в базе.
     */
    public AdsEntity findAds(long id) {
        return adsRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Ads not found"));
    }

    /**
     * Возвращает объявление по указанному ID.
     *
     * @param id ID объявления.
     * @return объявление.
     * @throws NotContextException объявление с указанным ID отсутствует в базе.
     */
    public AdsEntity findAdsContent(long id) throws NotContextException {
        return adsRepository.findById(id).orElseThrow(
                ()-> new NotContextException("No content"));
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
     * Обновляет содержание объявления (поля price и title).
     *
     * @param ads обновленные данные объявления.
     * @param id ID объявления.
     * @return обновленное объявление.
      */
    public AdsEntity updateAds(AdsEntity ads, long id) throws NotContextException {
        AdsEntity adsBD = findAdsContent(id);
        adsBD.setId(id);
        adsBD.setPrice(ads.getPrice());
        adsBD.setTitle(ads.getTitle());

        return adsRepository.save(adsBD);
    }

    /**
     * Удаляет объявление.
     *
     * @param id ID объявления, которое должно быть удалено.
     */
    public void deleteAds(long id) {
        adsRepository.deleteById(id);
    }
}

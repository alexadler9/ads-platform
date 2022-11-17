package pro.sky.adsplatform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.adsplatform.entity.AdsCommentEntity;
import pro.sky.adsplatform.exception.NotFoundException;
import pro.sky.adsplatform.mapper.AdsCommentMapper;
import pro.sky.adsplatform.repository.AdsCommentRepository;

import java.util.List;

/**
 * Сервис для работы с отзывами.
 */
@Service
public class AdsCommentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdsCommentService.class);

    private final AdsCommentRepository adsCommentRepository;

    public AdsCommentService(AdsCommentRepository adsCommentRepository, AdsCommentMapper adsCommentMapper) {
        this.adsCommentRepository = adsCommentRepository;
    }

    /**
     * Возвращает отзыв для объявления по указанному ID.
     *
     * @param id ID отзыва.
     * @param idAds ID объявления.
     * @return отзыв. Может вернуть null, если такой отзыв отсутствует.
     */
    public AdsCommentEntity findAdsComment(long id, long idAds) {
        return adsCommentRepository.findFirstByIdAndAds_Id(id, idAds).orElse(null);
    }

    /**
     * Возвращает все отзывы для объявления.
     *
     * @param idAds ID объявления.
     * @return список всех отзывов для объявления.
     */
    public List<AdsCommentEntity> findAllAdsComments(long idAds) {
        return adsCommentRepository.findAllByAds_Id(idAds);
    }

    /**
     * Создает новый отзыв.
     *
     * @param adsComment новый отзыв.
     * @return созданный отзыв.
     */
    public AdsCommentEntity createAdsComment(AdsCommentEntity adsComment) {
        adsComment.setId(null);
        return adsCommentRepository.save(adsComment);
    }

    /**
     * Обновляет содержание отзыва (поле text).
     *
     * @param adsComment обновленные данные отзыва.
     * @param id ID отзыва.
     * @param idAds ID объявления.
     * @throws IllegalArgumentException несооветствие значений ID entity и аргумента.
     * @throws NotFoundException отзыв с указанными параметрами отсутствует в базе.
     */
    public void updateAdsComment(AdsCommentEntity adsComment, long id, long idAds) {
        if (adsComment.getId() != id) {
            LOGGER.error("Несоответствие значений ID отзыва");
            throw new IllegalArgumentException("Несоответствие значений ID отзыва");
        }
        AdsCommentEntity adsCommentBD = findAdsComment(id, idAds);
        if (adsCommentBD == null) {
            LOGGER.error("Отзыв с таким ID отсутствует");
            throw new NotFoundException("Отзыв с таким ID отсутствует");
        }
        if (adsComment.getText() != null) {
            adsCommentBD.setText(adsComment.getText());
        }
        adsCommentRepository.save(adsCommentBD);
    }

    /**
     * Удаляет отзыв.
     *
     * @param adsComment отзыв, который должен быть удален.
     */
    public void deleteAdsComment(AdsCommentEntity adsComment) {
        adsCommentRepository.delete(adsComment);
    }
}

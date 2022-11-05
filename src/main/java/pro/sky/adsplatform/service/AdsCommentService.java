package pro.sky.adsplatform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.adsplatform.entity.AdsCommentEntity;
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
    public AdsCommentEntity getAdsComment(long id, long idAds) {
        return adsCommentRepository.findFirstByIdAndAds_Id(id, idAds).orElse(null);
    }

    /**
     * Возвращает все отзывы для объявления.
     *
     * @param idAds ID объявления.
     * @return отзыв. Может вернуть null, если такой отзыв отсутствует.
     */
    public List<AdsCommentEntity> getAllAdsComments(long idAds) {
        return adsCommentRepository.findAllByAds_Id(idAds);
    }

    /**
     * Сохраняет комментарий.
     */
    public void saveAddAdsCommentsUsingPOST(AdsCommentEntity adsCommentEntity) {
        adsCommentRepository.save(adsCommentEntity);

    }
}

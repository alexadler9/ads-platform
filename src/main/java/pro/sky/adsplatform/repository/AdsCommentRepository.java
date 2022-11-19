package pro.sky.adsplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.adsplatform.entity.AdsCommentEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdsCommentRepository extends JpaRepository<AdsCommentEntity, Long> {
    Optional<AdsCommentEntity> findFirstByIdAndAdsId(Long id, Long idAds);

    List<AdsCommentEntity> findAllByAdsId(Long idAds);
}

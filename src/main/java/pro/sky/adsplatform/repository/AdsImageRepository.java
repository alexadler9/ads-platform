package pro.sky.adsplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.adsplatform.entity.AdsImageEntity;

import java.util.List;

@Repository
public interface AdsImageRepository extends JpaRepository<AdsImageEntity, Long> {
    public List<AdsImageEntity> findAllByAds_Id(Integer id);
}

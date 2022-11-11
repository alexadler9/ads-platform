package pro.sky.adsplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import pro.sky.adsplatform.entity.AdsEntity;

import java.util.List;

@Repository
public interface AdsRepository extends JpaRepository<AdsEntity, Long> {

    List<AdsEntity> findAllByTitleLikeIgnoreCase(String title);
}

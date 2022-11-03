package pro.sky.adsplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.adsplatform.entity.AdsEntity;

@Repository
public interface AdsRepository extends JpaRepository<AdsEntity, Long> {
}

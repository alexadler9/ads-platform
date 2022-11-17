package pro.sky.adsplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.adsplatform.entity.AdsEntity;
import pro.sky.adsplatform.entity.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdsRepository extends JpaRepository<AdsEntity, Long> {
    Optional<AdsEntity> findById(long id);

    List<AdsEntity> findAllByAuthor(UserEntity userEntity);

    List<AdsEntity> findAllByTitleLikeIgnoreCase(String title);
}

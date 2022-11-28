package pro.sky.adsplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.adsplatform.entity.AvatarEntity;

@Repository
public interface AvatarRepository extends JpaRepository<AvatarEntity, Long> {
}

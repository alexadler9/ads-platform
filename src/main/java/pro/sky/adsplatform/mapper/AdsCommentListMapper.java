package pro.sky.adsplatform.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import pro.sky.adsplatform.dto.AdsCommentDto;
import pro.sky.adsplatform.entity.AdsCommentEntity;

import java.util.List;

@Mapper(componentModel = "spring", uses = AdsCommentMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AdsCommentListMapper {
    List<AdsCommentDto> adsCommentListToAdsCommentDtoList(List<AdsCommentEntity> entityList);
}

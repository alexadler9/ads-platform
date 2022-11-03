package pro.sky.adsplatform.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pro.sky.adsplatform.dto.AdsCommentDto;
import pro.sky.adsplatform.entity.AdsCommentEntity;

@Mapper(componentModel = "spring")
public interface AdsCommentMapper {
    @Mapping(target = "pk", source = "entity.id")
    @Mapping(target = "author", source = "entity.author.id")
    @Mapping(target = "createdAt", source = "entity.dateTime")
    AdsCommentDto adsCommentToAdsCommentDto(AdsCommentEntity entity);

    @Mapping(target = "id", source = "dto.pk")
    @Mapping(target = "author.id", source = "dto.author")
    @Mapping(target = "dateTime", source = "dto.createdAt")
    AdsCommentEntity adsCommentDtoToAdsComment(AdsCommentDto dto);
}

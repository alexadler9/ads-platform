package pro.sky.adsplatform.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pro.sky.adsplatform.dto.AdsCommentDto;
import pro.sky.adsplatform.entity.AdsCommentEntity;

@Mapper(componentModel = "spring")
public interface AdsCommentMapper {
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "createdAt", source = "dateTime")
    AdsCommentDto adsCommentToAdsCommentDto(AdsCommentEntity entity);

    @Mapping(target = "id", source = "pk")
    @Mapping(target = "author.id", source = "author")
    @Mapping(target = "dateTime", source = "createdAt")
    AdsCommentEntity adsCommentDtoToAdsComment(AdsCommentDto dto);
}

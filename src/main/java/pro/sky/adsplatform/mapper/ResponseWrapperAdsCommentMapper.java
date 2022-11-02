package pro.sky.adsplatform.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pro.sky.adsplatform.dto.ResponseWrapperAdsCommentDto;
import pro.sky.adsplatform.entity.AdsCommentEntity;

import java.util.List;

@Mapper(componentModel = "spring", uses = AdsCommentListMapper.class)
public interface ResponseWrapperAdsCommentMapper {
    @Mapping(target = "count", source = "sizeList")
    @Mapping(target = "results", source = "entityList")
    ResponseWrapperAdsCommentDto adsCommentListToResponseWrapperAdsCommentDto(Integer sizeList, List<AdsCommentEntity> entityList);
}

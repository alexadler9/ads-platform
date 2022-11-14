package pro.sky.adsplatform.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pro.sky.adsplatform.dto.ResponseWrapperAdsDto;
import pro.sky.adsplatform.entity.AdsEntity;

import java.util.List;

@Mapper(componentModel = "spring", uses = AdsListMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ResponseWrapperAdsMapper {
    @Mapping(target = "count", source = "sizeList")
    @Mapping(target = "results", source = "entityList")
    ResponseWrapperAdsDto adsListToResponseWrapperAdsDto(Integer sizeList, List<AdsEntity> entityList);
}

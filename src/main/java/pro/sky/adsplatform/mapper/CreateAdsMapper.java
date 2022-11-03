package pro.sky.adsplatform.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pro.sky.adsplatform.dto.CreateAdsDto;
import pro.sky.adsplatform.entity.AdsEntity;

@Mapper(componentModel = "spring")
public interface CreateAdsMapper {
    @Mapping(target = "id", source = "pk")
    @Mapping(target = "images", ignore = true)
    AdsEntity createAdsDtoToAds(CreateAdsDto dto);
}

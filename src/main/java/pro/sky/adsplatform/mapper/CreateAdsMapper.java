package pro.sky.adsplatform.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pro.sky.adsplatform.dto.CreateAdsDto;
import pro.sky.adsplatform.entity.AdsEntity;
import pro.sky.adsplatform.entity.AdsImageEntity;

import java.nio.charset.StandardCharsets;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CreateAdsMapper {
    AdsEntity createAdsDtoToAds(CreateAdsDto dto);

    CreateAdsDto adsToCreateAdsDto(AdsEntity entity);
}

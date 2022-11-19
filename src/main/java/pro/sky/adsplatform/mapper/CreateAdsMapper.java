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
    @Mapping(target = "id", source = "pk")
    @Mapping(target = "images", ignore = true)
    AdsEntity createAdsDtoToAds(CreateAdsDto dto);

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "image", source = "entity", qualifiedByName = "getLastImageString")
    CreateAdsDto adsToCreateAdsDto(AdsEntity entity);

    @Named("getLastImageString")
    default String getLastImageString(AdsEntity entity) {
        AdsImageEntity lastImage = entity.getLastImage();
        return (lastImage == null) ? null : "/ads/image/" + lastImage.getId().toString();
    }
}

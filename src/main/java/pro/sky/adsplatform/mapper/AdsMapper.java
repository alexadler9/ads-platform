package pro.sky.adsplatform.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pro.sky.adsplatform.dto.AdsDto;
import pro.sky.adsplatform.entity.AdsEntity;
import pro.sky.adsplatform.entity.AdsImageEntity;

import java.util.Arrays;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AdsMapper {
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "author", source = "author.id")
    @Mapping(target = "image", source = "entity", qualifiedByName = "getLastImageString")
    AdsDto adsToAdsDto(AdsEntity entity);

    @Named("getLastImageString")
    default String getLastImageString(AdsEntity entity) {
        AdsImageEntity lastImage = entity.getLastImage();
        return (lastImage == null) ? null : Arrays.toString(lastImage.getImage());
    }

    @Mapping(target = "id", source = "pk")
    @Mapping(target = "author.id", source = "author")
    @Mapping(target = "images", ignore = true)
    AdsEntity adsDtoToAds(AdsDto ads);
}

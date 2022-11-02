package pro.sky.adsplatform.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pro.sky.adsplatform.dto.AdsDto;
import pro.sky.adsplatform.entity.AdsEntity;
import pro.sky.adsplatform.entity.AdsImageEntity;

import java.util.Arrays;

@Mapper(componentModel = "spring")
public interface AdsMapper {
    @Mapping(target = "pk", source = "entity.id")
    @Mapping(target = "author", source = "entity.author.id")
    @Mapping(target = "image", source = "entity", qualifiedByName = "getLastImageString")
    AdsDto adsToAdsDto(AdsEntity entity);

    @Named("getLastImageString")
    default String getLastImageString(AdsEntity entity) {
        AdsImageEntity lastImage = entity.getLastImage();
        return (lastImage == null) ? null : Arrays.toString(lastImage.getImage());
    }

    @Mapping(target = "id", source = "ads.pk")
    @Mapping(target = "author.id", source = "ads.author")
//    @Mapping(target = "image", source = "entity", qualifiedByName = "getLastImageString")
    AdsEntity adsDtoToAds(AdsDto ads);
}

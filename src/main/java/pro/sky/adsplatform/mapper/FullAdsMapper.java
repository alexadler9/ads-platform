package pro.sky.adsplatform.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pro.sky.adsplatform.dto.FullAdsDto;
import pro.sky.adsplatform.entity.AdsEntity;
import pro.sky.adsplatform.entity.AdsImageEntity;

import java.nio.charset.StandardCharsets;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface FullAdsMapper {
    @Mapping(target = "authorFirstName", source = "author.firstName")
    @Mapping(target = "authorLastName", source = "author.lastName")
    @Mapping(target = "email", source = "author.username")
    @Mapping(target = "phone", source = "author.phone")
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "image", source = "entity", qualifiedByName = "getLastImageString")
    FullAdsDto adsToFullAdsDto(AdsEntity entity);

    @Named("getLastImageString")
    default String getLastImageString(AdsEntity entity) {
        AdsImageEntity lastImage = entity.getLastImage();
        return (lastImage == null) ? null : "ads/image/" + lastImage.getId().toString();
    }
}

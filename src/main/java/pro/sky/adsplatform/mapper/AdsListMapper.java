package pro.sky.adsplatform.mapper;

import org.mapstruct.Mapper;
import pro.sky.adsplatform.dto.AdsDto;
import pro.sky.adsplatform.entity.AdsEntity;

import java.util.List;

@Mapper(componentModel = "spring", uses = AdsMapper.class)
public interface AdsListMapper {
    List<AdsDto> adsListToAdsDtoList(List<AdsEntity> entityList);
}

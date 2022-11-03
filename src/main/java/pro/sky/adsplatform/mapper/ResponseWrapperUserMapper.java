package pro.sky.adsplatform.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pro.sky.adsplatform.dto.ResponseWrapperUserDto;
import pro.sky.adsplatform.entity.UserEntity;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserListMapper.class)
public interface ResponseWrapperUserMapper {
    @Mapping(target = "count", source = "sizeList")
    @Mapping(target = "results", source = "entityList")
    ResponseWrapperUserDto userListToResponseWrapperUserDto(Integer sizeList, List<UserEntity> entityList);
}

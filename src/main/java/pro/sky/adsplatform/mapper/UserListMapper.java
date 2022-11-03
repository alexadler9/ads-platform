package pro.sky.adsplatform.mapper;

import org.mapstruct.Mapper;
import pro.sky.adsplatform.dto.UserDto;
import pro.sky.adsplatform.entity.UserEntity;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface UserListMapper {
    List<UserDto> userListToUserDtoList(List<UserEntity> entityList);
}

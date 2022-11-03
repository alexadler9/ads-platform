package pro.sky.adsplatform.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import pro.sky.adsplatform.dto.UserDto;
import pro.sky.adsplatform.entity.UserEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {
    UserDto userToUserDto(UserEntity entity);

    UserEntity userDtoToUser(UserDto dto);
}

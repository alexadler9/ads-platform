package pro.sky.adsplatform.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pro.sky.adsplatform.dto.UserDto;
import pro.sky.adsplatform.entity.UserEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {
    @Mapping(target = "email", source = "username")
    UserDto userToUserDto(UserEntity entity);

    @Mapping(target = "username", source = "email")
    UserEntity userDtoToUser(UserDto dto);
}

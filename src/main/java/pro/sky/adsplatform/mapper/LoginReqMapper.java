package pro.sky.adsplatform.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pro.sky.adsplatform.dto.LoginReqDto;
import pro.sky.adsplatform.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface LoginReqMapper {
    @Mapping(target = "email", source = "username")
    UserEntity loginReqDtoToUser(LoginReqDto dto);
}

package pro.sky.adsplatform.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pro.sky.adsplatform.dto.RegisterReqDto;
import pro.sky.adsplatform.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface RegisterReqMapper {
    @Mapping(target = "email", source = "dto.username")
    UserEntity registerReqDtoToUser(RegisterReqDto dto);
}

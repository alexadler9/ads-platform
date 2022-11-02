package pro.sky.adsplatform.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pro.sky.adsplatform.dto.NewPasswordDto;
import pro.sky.adsplatform.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface NewPasswordMapper {
    @Mapping(target = "password", source = "dto.newPassword")
    UserEntity newPasswordDtoToUser(NewPasswordDto dto);
}

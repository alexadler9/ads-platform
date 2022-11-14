package pro.sky.adsplatform.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pro.sky.adsplatform.dto.RegisterReqDto;
import pro.sky.adsplatform.entity.UserEntity;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface RegisterReqMapper {
    UserEntity registerReqDtoToUser(RegisterReqDto dto);
}

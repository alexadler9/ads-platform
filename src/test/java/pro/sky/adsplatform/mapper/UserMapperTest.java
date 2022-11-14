package pro.sky.adsplatform.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pro.sky.adsplatform.dto.UserDto;
import pro.sky.adsplatform.entity.UserEntity;

import static pro.sky.adsplatform.mapper.constants.MapperTestConstants.USER;
import static pro.sky.adsplatform.mapper.constants.MapperTestConstants.USER_DTO;

class UserMapperTest {
    private final UserMapper userMapper = new UserMapperImpl();

    @Test
    void shouldProperlyMapUserToUserDto() {
        UserDto userDto = userMapper.userToUserDto(USER);

        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(USER.getUsername(), userDto.getEmail());
        Assertions.assertEquals(USER.getFirstName(), userDto.getFirstName());
        Assertions.assertEquals(USER.getId(), userDto.getId().longValue());
        Assertions.assertEquals(USER.getLastName(), userDto.getLastName());
        Assertions.assertEquals(USER.getPhone(), userDto.getPhone());
    }

    @Test
    void shouldProperlyMapUserDtoToUser() {
        UserEntity user = userMapper.userDtoToUser(USER_DTO);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(USER_DTO.getId(), user.getId().intValue());
        Assertions.assertEquals(USER_DTO.getFirstName(), user.getFirstName());
        Assertions.assertEquals(USER_DTO.getLastName(), user.getLastName());
        Assertions.assertEquals(USER_DTO.getPhone(), user.getPhone());
        Assertions.assertEquals(USER_DTO.getEmail(), user.getUsername());
    }
}
package pro.sky.adsplatform.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pro.sky.adsplatform.dto.UserDto;
import pro.sky.adsplatform.entity.UserEntity;

import java.util.Collections;
import java.util.List;

import static pro.sky.adsplatform.constants.TestEntityConstants.USER;

class UserListMapperTest {
    private final UserListMapper userListMapper = new UserListMapperImpl(new UserMapperImpl());

    @Test
    void shouldProperlyMapUserListToUserDtoList() {
        List<UserEntity> userList = Collections.singletonList(USER);

        List<UserDto> userDtoList = userListMapper.userListToUserDtoList(userList);

        Assertions.assertNotNull(userDtoList);
        Assertions.assertEquals(1, userDtoList.size());
        Assertions.assertEquals(USER.getUsername(), userDtoList.get(0).getEmail());
        Assertions.assertEquals(USER.getFirstName(), userDtoList.get(0).getFirstName());
        Assertions.assertEquals(USER.getId(), userDtoList.get(0).getId().longValue());
        Assertions.assertEquals(USER.getLastName(), userDtoList.get(0).getLastName());
        Assertions.assertEquals(USER.getPhone(), userDtoList.get(0).getPhone());
    }
}
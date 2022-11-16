package pro.sky.adsplatform.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pro.sky.adsplatform.dto.ResponseWrapperUserDto;
import pro.sky.adsplatform.entity.UserEntity;

import java.util.Collections;
import java.util.List;

import static pro.sky.adsplatform.constants.TestEntityConstants.USER;

class ResponseWrapperUserMapperTest {
    private final ResponseWrapperUserMapper responseWrapperUserMapper =
            new ResponseWrapperUserMapperImpl(new UserListMapperImpl(new UserMapperImpl()));

    @Test
    void shouldProperlyMapUserListToResponseWrapperUserDto() {
        List<UserEntity> userList = Collections.singletonList(USER);

        ResponseWrapperUserDto responseWrapperUserDto =
                responseWrapperUserMapper.userListToResponseWrapperUserDto(userList.size(), userList);

        Assertions.assertNotNull(responseWrapperUserDto);
        Assertions.assertEquals(1, responseWrapperUserDto.getCount());
        Assertions.assertEquals(1, responseWrapperUserDto.getResults().size());
        Assertions.assertEquals(USER.getUsername(), responseWrapperUserDto.getResults().get(0).getEmail());
        Assertions.assertEquals(USER.getFirstName(), responseWrapperUserDto.getResults().get(0).getFirstName());
        Assertions.assertEquals(USER.getId(), responseWrapperUserDto.getResults().get(0).getId().longValue());
        Assertions.assertEquals(USER.getLastName(), responseWrapperUserDto.getResults().get(0).getLastName());
        Assertions.assertEquals(USER.getPhone(), responseWrapperUserDto.getResults().get(0).getPhone());
    }
}
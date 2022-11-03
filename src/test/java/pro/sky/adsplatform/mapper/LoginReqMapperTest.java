package pro.sky.adsplatform.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pro.sky.adsplatform.entity.UserEntity;

import static pro.sky.adsplatform.mapper.constants.MapperTestConstants.LOGIN_REQ_DTO;

class LoginReqMapperTest {
    private final LoginReqMapper loginReqMapper = new LoginReqMapperImpl();

    @Test
    void shouldProperlyMapLoginReqDtoToUser() {
        UserEntity user = loginReqMapper.loginReqDtoToUser(LOGIN_REQ_DTO);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(LOGIN_REQ_DTO.getUsername(), user.getEmail());
        Assertions.assertEquals(LOGIN_REQ_DTO.getPassword(), user.getPassword());
    }
}
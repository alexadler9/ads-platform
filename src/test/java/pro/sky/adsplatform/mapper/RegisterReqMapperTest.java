package pro.sky.adsplatform.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pro.sky.adsplatform.entity.UserEntity;

import static pro.sky.adsplatform.mapper.constants.MapperTestConstants.REGISTER_REQ_DTO;

class RegisterReqMapperTest {
    private final RegisterReqMapper registerReqMapper = new RegisterReqMapperImpl();

    @Test
    void shouldProperlyMapRegisterReqDtoToUser() {
        UserEntity user = registerReqMapper.registerReqDtoToUser(REGISTER_REQ_DTO);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(REGISTER_REQ_DTO.getFirstName(), user.getFirstName());
        Assertions.assertEquals(REGISTER_REQ_DTO.getLastName(), user.getLastName());
        Assertions.assertEquals(REGISTER_REQ_DTO.getPhone(), user.getPhone());
        Assertions.assertEquals(REGISTER_REQ_DTO.getUsername(), user.getEmail());
        Assertions.assertEquals(REGISTER_REQ_DTO.getPassword(), user.getPassword());
        Assertions.assertEquals(REGISTER_REQ_DTO.getRole().toString(), user.getRole().toString());
    }
}
package pro.sky.adsplatform.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pro.sky.adsplatform.entity.UserEntity;

import static pro.sky.adsplatform.mapper.constants.MapperTestConstants.NEW_PASSWORD_DTO;

class NewPasswordMapperTest {
    private final NewPasswordMapper newPasswordMapper = new NewPasswordMapperImpl();

    @Test
    void shouldProperlyMapNewPasswordDtoToUser() {
        UserEntity user = newPasswordMapper.newPasswordDtoToUser(NEW_PASSWORD_DTO);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(NEW_PASSWORD_DTO.getNewPassword(), user.getPassword());
    }
}
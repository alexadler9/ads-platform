package pro.sky.adsplatform.constants;

import pro.sky.adsplatform.dto.*;
import pro.sky.adsplatform.entity.AdsImageEntity;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;

public class TestDtoConstants {
    public static final UserDto USER_DTO;

    public static final AdsDto ADS_DTO;

    public static final FullAdsDto FULL_ADS_DTO;

    public static final CreateAdsDto CREATE_ADS_DTO;

    public static final AdsCommentDto ADS_COMMENT_DTO;

    public static final RegisterReqDto REGISTER_REQ_DTO;

    static {
        USER_DTO = new UserDto();
        USER_DTO.setEmail("user@gmail.com");
        USER_DTO.setFirstName("firstName");
        USER_DTO.setId(1);
        USER_DTO.setLastName("lastName");
        USER_DTO.setPhone("phone");

        ADS_DTO = new AdsDto();
        ADS_DTO.setAuthor(1);
        ADS_DTO.setImage("ads/image/1");
        ADS_DTO.setPk(1);
        ADS_DTO.setPrice(1000);
        ADS_DTO.setTitle("title");

        FULL_ADS_DTO = new FullAdsDto();
        FULL_ADS_DTO.setAuthorFirstName("firstName");
        FULL_ADS_DTO.setAuthorLastName("lastName");
        FULL_ADS_DTO.setDescription("description");
        FULL_ADS_DTO.setEmail("user@gmail.com");
        FULL_ADS_DTO.setImage("ads/image/1");
        FULL_ADS_DTO.setPhone("phone");
        FULL_ADS_DTO.setPk(1);
        FULL_ADS_DTO.setPrice(1000);
        FULL_ADS_DTO.setTitle("title");

        CREATE_ADS_DTO = new CreateAdsDto();
        CREATE_ADS_DTO.setDescription("description");
        CREATE_ADS_DTO.setImage("ads/image/1");
        CREATE_ADS_DTO.setPk(1);
        CREATE_ADS_DTO.setPrice(1000);
        CREATE_ADS_DTO.setTitle("title");

        ADS_COMMENT_DTO = new AdsCommentDto();
        ADS_COMMENT_DTO.setAuthor(1);
        ADS_COMMENT_DTO.setCreatedAt(LocalDateTime.of(2022, Month.JANUARY, 1, 12, 0, 0));
        ADS_COMMENT_DTO.setPk(1);
        ADS_COMMENT_DTO.setText("text");

        REGISTER_REQ_DTO = new RegisterReqDto();
        REGISTER_REQ_DTO.setUsername("user@gmail.com");
        REGISTER_REQ_DTO.setPassword("password");
        REGISTER_REQ_DTO.setFirstName("firstName");
        REGISTER_REQ_DTO.setLastName("lastName");
        REGISTER_REQ_DTO.setPhone("phone");
        REGISTER_REQ_DTO.setRole(RoleDto.USER);
    }
}

package pro.sky.adsplatform.mapper.constants;

import pro.sky.adsplatform.dto.*;
import pro.sky.adsplatform.entity.AdsCommentEntity;
import pro.sky.adsplatform.entity.AdsEntity;
import pro.sky.adsplatform.entity.AdsImageEntity;
import pro.sky.adsplatform.entity.UserEntity;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;

public class MapperTestConstants {
    public static final UserEntity USER;

    public static final UserDto USER_DTO;

    public static final AdsEntity ADS;

    public static final AdsDto ADS_DTO;

    public static final CreateAdsDto CREATE_ADS_DTO;

    public static final AdsCommentEntity ADS_COMMENT;

    public static final AdsCommentDto ADS_COMMENT_DTO;

    public static final RegisterReqDto REGISTER_REQ_DTO;

    static {
        USER = new UserEntity();
        USER.setId(1L);
        USER.setFirstName("firstName");
        USER.setLastName("lastName");
        USER.setPhone("phone");
        USER.setUsername("username");
        USER.setPassword("password");
        USER.setEnabled(true);

        USER_DTO = new UserDto();
        USER_DTO.setEmail("email");
        USER_DTO.setFirstName("firstName");
        USER_DTO.setId(1);
        USER_DTO.setLastName("lastName");
        USER_DTO.setPhone("phone");

        ADS = new AdsEntity();
        ADS.setId(1L);
        ADS.setAuthor(USER);
        ADS.setTitle("title");
        ADS.setDescription("description");
        ADS.setPrice(new BigDecimal(1000));
        List<AdsImageEntity> images = Collections.singletonList(new AdsImageEntity());
        images.get(0).setId(2L);
        byte[] imageBytes = new byte[] { 0x01, 0x02, 0x03 };
        images.get(0).setImage(imageBytes);
        ADS.setImages(images);

        ADS_DTO = new AdsDto();
        ADS_DTO.setAuthor(2);
        ADS_DTO.setImage(new String(imageBytes, StandardCharsets.UTF_8));
        ADS_DTO.setPk(1);
        ADS_DTO.setPrice(1000);
        ADS_DTO.setTitle("title");

        CREATE_ADS_DTO = new CreateAdsDto();
        CREATE_ADS_DTO.setDescription("description");
        CREATE_ADS_DTO.setImage(new String(imageBytes, StandardCharsets.UTF_8));
        CREATE_ADS_DTO.setPk(1);
        CREATE_ADS_DTO.setPrice(1000);
        CREATE_ADS_DTO.setTitle("title");

        ADS_COMMENT = new AdsCommentEntity();
        ADS_COMMENT.setId(1L);
        ADS_COMMENT.setAuthor(USER);
        ADS_COMMENT.setAds(ADS);
        ADS_COMMENT.setDateTime(LocalDateTime.of(2022, Month.JANUARY, 1, 12, 0, 0));
        ADS_COMMENT.setText("text");

        ADS_COMMENT_DTO = new AdsCommentDto();
        ADS_COMMENT_DTO.setAuthor(2);
        ADS_COMMENT_DTO.setCreatedAt(LocalDateTime.of(2022, Month.JANUARY, 1, 12, 0, 0));
        ADS_COMMENT_DTO.setPk(1);
        ADS_COMMENT_DTO.setText("text");

        REGISTER_REQ_DTO = new RegisterReqDto();
        REGISTER_REQ_DTO.setUsername("username");
        REGISTER_REQ_DTO.setPassword("password");
        REGISTER_REQ_DTO.setFirstName("firstName");
        REGISTER_REQ_DTO.setLastName("lastName");
        REGISTER_REQ_DTO.setPhone("phone");
        REGISTER_REQ_DTO.setRole(RoleDto.USER);
    }
}

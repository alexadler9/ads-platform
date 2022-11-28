package pro.sky.adsplatform.constants;

import pro.sky.adsplatform.entity.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;

public class TestEntityConstants {
    public static final UserEntity USER;

    public static final AdsEntity ADS;

    public static final AdsCommentEntity ADS_COMMENT;

    public static final AdsImageEntity ADS_IMAGE;

    public static final AvatarEntity AVATAR_IMAGE;

    static {
        USER = new UserEntity();
        USER.setId(1L);
        USER.setFirstName("firstName");
        USER.setLastName("lastName");
        USER.setPhone("phone");
        USER.setUsername("user@gmail.com");
        USER.setPassword("password");
        USER.setEnabled(true);

        ADS = new AdsEntity();
        ADS.setId(1L);
        ADS.setAuthor(USER);
        ADS.setTitle("title");
        ADS.setDescription("description");
        ADS.setPrice(new BigDecimal(1000));
        List<AdsImageEntity> images = Collections.singletonList(new AdsImageEntity());
        images.get(0).setId(1L);
        byte[] imageBytes = new byte[] { 0x01, 0x02, 0x03 };
        images.get(0).setImage(imageBytes);
        ADS.setImages(images);

        ADS_COMMENT = new AdsCommentEntity();
        ADS_COMMENT.setId(1L);
        ADS_COMMENT.setAuthor(USER);
        ADS_COMMENT.setAds(ADS);
        ADS_COMMENT.setDateTime(LocalDateTime.of(2022, Month.JANUARY, 1, 12, 0, 0));
        ADS_COMMENT.setText("text");

        ADS_IMAGE = new AdsImageEntity();
        ADS_IMAGE.setId(1L);
        ADS_IMAGE.setImage(imageBytes);
        ADS_IMAGE.setAds(ADS);

        AVATAR_IMAGE = new AvatarEntity();
        AVATAR_IMAGE.setUser(USER);
        AVATAR_IMAGE.setImage(imageBytes);
    }
}

package pro.sky.adsplatform.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pro.sky.adsplatform.entity.AdsEntity;

import static pro.sky.adsplatform.constants.TestDtoConstants.CREATE_ADS_DTO;

class CreateAdsMapperTest {
    private final CreateAdsMapper createAdsMapper = new CreateAdsMapperImpl();

    @Test
    void shouldProperlyMapCreateAdsDtoToAds() {
        AdsEntity ads = createAdsMapper.createAdsDtoToAds(CREATE_ADS_DTO);

        Assertions.assertNotNull(ads);
        Assertions.assertEquals(CREATE_ADS_DTO.getPk(), ads.getId().intValue());
        Assertions.assertEquals(CREATE_ADS_DTO.getTitle(), ads.getTitle());
        Assertions.assertEquals(CREATE_ADS_DTO.getDescription(), ads.getDescription());
        Assertions.assertEquals(CREATE_ADS_DTO.getPrice(), ads.getPrice().intValue());
    }
}
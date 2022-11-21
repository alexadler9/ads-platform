package pro.sky.adsplatform.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pro.sky.adsplatform.dto.AdsDto;
import pro.sky.adsplatform.entity.AdsEntity;

import java.math.BigDecimal;

import static pro.sky.adsplatform.constants.TestDtoConstants.ADS_DTO;
import static pro.sky.adsplatform.constants.TestEntityConstants.ADS;

class AdsMapperTest {
    private final AdsMapper adsMapper = new AdsMapperImpl();

    @Test
    void shouldProperlyMapAdsToAdsDto() {
        AdsDto adsDto = adsMapper.adsToAdsDto(ADS);

        Assertions.assertNotNull(adsDto);
        Assertions.assertEquals(ADS.getAuthor().getId(), adsDto.getAuthor().longValue());
        Assertions.assertEquals("/ads/image/" + ADS.getLastImage().getId().toString(), adsDto.getImage());
        Assertions.assertEquals(ADS.getId(), adsDto.getPk().longValue());
        Assertions.assertEquals(ADS.getPrice(), new BigDecimal(adsDto.getPrice()));
        Assertions.assertEquals(ADS.getTitle(), adsDto.getTitle());
    }

    @Test
    void shouldProperlyMapAdsDtoToAds() {
        AdsEntity ads = adsMapper.adsDtoToAds(ADS_DTO);

        Assertions.assertNotNull(ads);
        Assertions.assertEquals(ADS_DTO.getPk(), ads.getId().intValue());
        Assertions.assertEquals(ADS_DTO.getAuthor(), ads.getAuthor().getId().intValue());
        Assertions.assertEquals(ADS_DTO.getTitle(), ads.getTitle());
        Assertions.assertEquals(ADS_DTO.getPrice(), ads.getPrice().intValue());
    }
}
package pro.sky.adsplatform.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pro.sky.adsplatform.dto.FullAdsDto;

import java.math.BigDecimal;

import static pro.sky.adsplatform.constants.TestEntityConstants.ADS;

class FullAdsMapperTest {
    private final FullAdsMapper fullAdsMapper = new FullAdsMapperImpl();

    @Test
    void shouldProperlyMapAdsToFullAdsDto() {
        FullAdsDto fullAdsDto = fullAdsMapper.adsToFullAdsDto(ADS);

        Assertions.assertNotNull(fullAdsDto);
        Assertions.assertEquals(ADS.getAuthor().getFirstName(), fullAdsDto.getAuthorFirstName());
        Assertions.assertEquals(ADS.getAuthor().getLastName(), fullAdsDto.getAuthorLastName());
        Assertions.assertEquals(ADS.getDescription(), fullAdsDto.getDescription());
        Assertions.assertEquals(ADS.getAuthor().getUsername(), fullAdsDto.getEmail());
        Assertions.assertEquals("/ads/image/" + ADS.getLastImage().getId().toString(), fullAdsDto.getImage());
        Assertions.assertEquals(ADS.getAuthor().getPhone(), fullAdsDto.getPhone());
        Assertions.assertEquals(ADS.getId(), fullAdsDto.getPk().longValue());
        Assertions.assertEquals(ADS.getPrice(), new BigDecimal(fullAdsDto.getPrice()));
        Assertions.assertEquals(ADS.getTitle(), fullAdsDto.getTitle());
    }
}
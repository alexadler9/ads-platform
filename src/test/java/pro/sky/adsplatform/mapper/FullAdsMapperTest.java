package pro.sky.adsplatform.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pro.sky.adsplatform.dto.FullAdsDto;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static pro.sky.adsplatform.mapper.constants.MapperTestConstants.ADS;

class FullAdsMapperTest {
    private final FullAdsMapper fullAdsMapper = new FullAdsMapperImpl();

    @Test
    void shouldProperlyMapAdsToFullAdsDto() {
        FullAdsDto fullAdsDto = fullAdsMapper.adsToFullAdsDto(ADS);

        Assertions.assertNotNull(fullAdsDto);
        Assertions.assertEquals(ADS.getAuthor().getFirstName(), fullAdsDto.getAuthorFirstName());
        Assertions.assertEquals(ADS.getAuthor().getLastName(), fullAdsDto.getAuthorLastName());
        Assertions.assertEquals(ADS.getDescription(), fullAdsDto.getDescription());
        Assertions.assertEquals(ADS.getAuthor().getEmail(), fullAdsDto.getEmail());
        Assertions.assertEquals(new String(ADS.getLastImage().getImage(), StandardCharsets.UTF_8), fullAdsDto.getImage());
        Assertions.assertEquals(ADS.getAuthor().getPhone(), fullAdsDto.getPhone());
        Assertions.assertEquals(ADS.getId(), fullAdsDto.getPk().longValue());
        Assertions.assertEquals(ADS.getPrice(), new BigDecimal(fullAdsDto.getPrice()));
        Assertions.assertEquals(ADS.getTitle(), fullAdsDto.getTitle());
    }
}
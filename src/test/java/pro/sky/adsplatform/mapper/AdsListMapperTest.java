package pro.sky.adsplatform.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pro.sky.adsplatform.dto.AdsDto;
import pro.sky.adsplatform.entity.AdsEntity;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static pro.sky.adsplatform.mapper.constants.MapperTestConstants.ADS;

class AdsListMapperTest {
    private final AdsListMapper adsListMapper = new AdsListMapperImpl(new AdsMapperImpl());

    @Test
    void shouldProperlyMapAdsListToAdsDtoList() {
        List<AdsEntity> adsList = Collections.singletonList(ADS);

        List<AdsDto> adsDtoList = adsListMapper.adsListToAdsDtoList(adsList);

        Assertions.assertNotNull(adsDtoList);
        Assertions.assertEquals(1, adsDtoList.size());
        Assertions.assertEquals(ADS.getAuthor().getId(), adsDtoList.get(0).getAuthor().longValue());
        Assertions.assertEquals(new String(ADS.getLastImage().getImage(), StandardCharsets.UTF_8), adsDtoList.get(0).getImage());
        Assertions.assertEquals(ADS.getId(), adsDtoList.get(0).getPk().longValue());
        Assertions.assertEquals(ADS.getPrice(), new BigDecimal(adsDtoList.get(0).getPrice()));
        Assertions.assertEquals(ADS.getTitle(), adsDtoList.get(0).getTitle());
    }
}
package pro.sky.adsplatform.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pro.sky.adsplatform.dto.ResponseWrapperAdsDto;
import pro.sky.adsplatform.entity.AdsEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static pro.sky.adsplatform.constants.TestEntityConstants.ADS;

class ResponseWrapperAdsMapperTest {
    private final ResponseWrapperAdsMapper responseWrapperAdsMapper =
            new ResponseWrapperAdsMapperImpl(new AdsListMapperImpl(new AdsMapperImpl()));

    @Test
    void shouldProperlyMapAdsListToResponseWrapperAdsDto() {
        List<AdsEntity> adsList = Collections.singletonList(ADS);

        ResponseWrapperAdsDto responseWrapperAdsDto =
                responseWrapperAdsMapper.adsListToResponseWrapperAdsDto(adsList.size(), adsList);

        Assertions.assertNotNull(responseWrapperAdsDto);
        Assertions.assertEquals(1, responseWrapperAdsDto.getCount());
        Assertions.assertEquals(1, responseWrapperAdsDto.getResults().size());
        Assertions.assertEquals(ADS.getAuthor().getId(), responseWrapperAdsDto.getResults().get(0).getAuthor().longValue());
        Assertions.assertEquals("/ads/image/" + ADS.getLastImage().getId().toString(), responseWrapperAdsDto.getResults().get(0).getImage());
        Assertions.assertEquals(ADS.getId(), responseWrapperAdsDto.getResults().get(0).getPk().longValue());
        Assertions.assertEquals(ADS.getPrice(), new BigDecimal(responseWrapperAdsDto.getResults().get(0).getPrice()));
        Assertions.assertEquals(ADS.getTitle(), responseWrapperAdsDto.getResults().get(0).getTitle());
    }
}
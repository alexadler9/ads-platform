package pro.sky.adsplatform.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pro.sky.adsplatform.dto.AdsCommentDto;
import pro.sky.adsplatform.entity.AdsCommentEntity;

import java.util.Collections;
import java.util.List;

import static pro.sky.adsplatform.constants.TestEntityConstants.ADS_COMMENT;

class AdsCommentListMapperTest {
    private final AdsCommentListMapper adsCommentListMapper = new AdsCommentListMapperImpl(new AdsCommentMapperImpl());

    @Test
    void shouldProperlyMapAdsCommentListToAdsCommentDtoList() {
        List<AdsCommentEntity> adsCommentList = Collections.singletonList(ADS_COMMENT);

        List<AdsCommentDto> AdsCommentDtoList = adsCommentListMapper.adsCommentListToAdsCommentDtoList(adsCommentList);

        Assertions.assertNotNull(AdsCommentDtoList);
        Assertions.assertEquals(1, AdsCommentDtoList.size());
        Assertions.assertEquals(ADS_COMMENT.getAuthor().getId(), AdsCommentDtoList.get(0).getAuthor().longValue());
        Assertions.assertEquals(ADS_COMMENT.getDateTime(), AdsCommentDtoList.get(0).getCreatedAt());
        Assertions.assertEquals(ADS_COMMENT.getId(), AdsCommentDtoList.get(0).getPk().longValue());
        Assertions.assertEquals(ADS_COMMENT.getText(), AdsCommentDtoList.get(0).getText());
    }
}
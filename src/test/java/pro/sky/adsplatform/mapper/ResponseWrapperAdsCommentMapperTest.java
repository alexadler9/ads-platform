package pro.sky.adsplatform.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pro.sky.adsplatform.dto.ResponseWrapperAdsCommentDto;
import pro.sky.adsplatform.entity.AdsCommentEntity;

import java.util.Collections;
import java.util.List;

import static pro.sky.adsplatform.constants.TestEntityConstants.ADS_COMMENT;

class ResponseWrapperAdsCommentMapperTest {
    private final ResponseWrapperAdsCommentMapper responseWrapperAdsCommentMapper =
            new ResponseWrapperAdsCommentMapperImpl(new AdsCommentListMapperImpl(new AdsCommentMapperImpl()));

    @Test
    void shouldProperlyMapUserListToResponseWrapperUserDto() {
        List<AdsCommentEntity> adsCommentList = Collections.singletonList(ADS_COMMENT);

        ResponseWrapperAdsCommentDto responseWrapperAdsCommentDto =
                responseWrapperAdsCommentMapper.adsCommentListToResponseWrapperAdsCommentDto(adsCommentList.size(), adsCommentList);

        Assertions.assertNotNull(responseWrapperAdsCommentDto);
        Assertions.assertEquals(1, responseWrapperAdsCommentDto.getCount());
        Assertions.assertEquals(1, responseWrapperAdsCommentDto.getResults().size());
        Assertions.assertEquals(ADS_COMMENT.getAuthor().getId(), responseWrapperAdsCommentDto.getResults().get(0).getAuthor().longValue());
        Assertions.assertEquals(ADS_COMMENT.getDateTime(), responseWrapperAdsCommentDto.getResults().get(0).getCreatedAt());
        Assertions.assertEquals(ADS_COMMENT.getId(), responseWrapperAdsCommentDto.getResults().get(0).getPk().longValue());
        Assertions.assertEquals(ADS_COMMENT.getText(), responseWrapperAdsCommentDto.getResults().get(0).getText());
    }
}
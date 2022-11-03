package pro.sky.adsplatform.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pro.sky.adsplatform.dto.AdsCommentDto;
import pro.sky.adsplatform.entity.AdsCommentEntity;

import static pro.sky.adsplatform.mapper.constants.MapperTestConstants.ADS_COMMENT;
import static pro.sky.adsplatform.mapper.constants.MapperTestConstants.ADS_COMMENT_DTO;

class AdsCommentMapperTest {
    private final AdsCommentMapper adsCommentMapper = new AdsCommentMapperImpl();

    @Test
    void shouldProperlyMapAdsCommentToAdsCommentDto() {
        AdsCommentDto adsCommentDto = adsCommentMapper.adsCommentToAdsCommentDto(ADS_COMMENT);

        Assertions.assertEquals(ADS_COMMENT.getAuthor().getId(), adsCommentDto.getAuthor().longValue());
        Assertions.assertEquals(ADS_COMMENT.getDateTime(), adsCommentDto.getCreatedAt());
        Assertions.assertEquals(ADS_COMMENT.getId(), adsCommentDto.getPk().longValue());
        Assertions.assertEquals(ADS_COMMENT.getText(), adsCommentDto.getText());
    }

    @Test
    void shouldProperlyMapAdsCommentDtoToAdsComment() {
        AdsCommentEntity adsComment = adsCommentMapper.adsCommentDtoToAdsComment(ADS_COMMENT_DTO);

        Assertions.assertEquals(ADS_COMMENT_DTO.getPk(), adsComment.getId().intValue());
        Assertions.assertEquals(ADS_COMMENT_DTO.getAuthor(), adsComment.getAuthor().getId().intValue());
        Assertions.assertEquals(ADS_COMMENT_DTO.getCreatedAt(), adsComment.getDateTime());
        Assertions.assertEquals(ADS_COMMENT_DTO.getText(), adsComment.getText());
    }
}
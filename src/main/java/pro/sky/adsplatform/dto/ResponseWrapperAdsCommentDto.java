package pro.sky.adsplatform.dto;
import lombok.Data;
import java.util.List;

/**
 * ResponseWrapperAdsComment
 */
@Data
public class ResponseWrapperAdsCommentDto {
  private Integer count;
  private List<AdsCommentDto> results;

}

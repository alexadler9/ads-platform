package pro.sky.adsplatform.dto;
import lombok.Data;
import java.util.List;

@Data
public class ResponseWrapperAdsCommentDto {
  private Integer count;
  private List<AdsCommentDto> results;
}

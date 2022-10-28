package pro.sky.adsplatform.dto;
import lombok.Data;
import java.util.List;

/**
 * ResponseWrapperAdsComment
 */
@Data
public class ResponseWrapperAdsComment   {
  private Integer count;
  private List<AdsComment> results;

}

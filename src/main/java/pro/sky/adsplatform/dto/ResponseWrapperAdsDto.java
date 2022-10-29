package pro.sky.adsplatform.dto;
import lombok.Data;
import java.util.List;

/**
 * ResponseWrapperAds
 */
@Data
public class ResponseWrapperAdsDto {
  private Integer count;
  private List<AdsDto> results;
}

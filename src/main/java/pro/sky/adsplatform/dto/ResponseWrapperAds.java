package pro.sky.adsplatform.dto;
import lombok.Data;
import java.util.List;

/**
 * ResponseWrapperAds
 */
@Data
public class ResponseWrapperAds   {
  private Integer count;
  private List<Ads> results;
}

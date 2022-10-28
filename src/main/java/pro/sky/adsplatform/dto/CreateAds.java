package pro.sky.adsplatform.dto;
import lombok.Data;

/**
 * CreateAds
 */
@Data
public class CreateAds   {
  private String description;
  private String image;
  private Integer pk;
  private Integer price;
  private String title;

}

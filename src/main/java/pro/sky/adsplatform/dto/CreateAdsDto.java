package pro.sky.adsplatform.dto;
import lombok.Data;

@Data
public class CreateAdsDto {
  private String description;
  private Integer price;
  private String title;
}

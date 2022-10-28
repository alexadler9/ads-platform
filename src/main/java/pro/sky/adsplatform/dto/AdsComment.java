package pro.sky.adsplatform.dto;
import lombok.Data;
import java.time.OffsetDateTime;

/**
 * AdsComment
 */
@Data
public class AdsComment   {
  private Integer author;
  private OffsetDateTime createdAt;
  private Integer pk;
  private String text;

}

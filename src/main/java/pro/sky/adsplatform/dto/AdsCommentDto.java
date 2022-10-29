package pro.sky.adsplatform.dto;
import lombok.Data;
import java.time.OffsetDateTime;

/**
 * AdsComment
 */
@Data
public class AdsCommentDto {
  private Integer author;
  private OffsetDateTime createdAt;
  private Integer pk;
  private String text;

}
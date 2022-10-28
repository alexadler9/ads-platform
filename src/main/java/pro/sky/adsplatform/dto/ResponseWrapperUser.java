package pro.sky.adsplatform.dto;
import lombok.Data;
import java.util.List;

/**
 * ResponseWrapperUser
 */
@Data
public class ResponseWrapperUser   {
  private Integer count;
  private List<User> results;
}

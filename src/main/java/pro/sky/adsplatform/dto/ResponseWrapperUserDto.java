package pro.sky.adsplatform.dto;
import lombok.Data;
import java.util.List;

/**
 * ResponseWrapperUser
 */
@Data
public class ResponseWrapperUserDto {
  private Integer count;
  private List<UserDto> results;
}

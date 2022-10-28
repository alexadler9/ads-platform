package pro.sky.adsplatform.dto;
import lombok.Data;

/**
 * CreateUser
 */
@Data
public class CreateUser   {
  private String firstName;
  private String lastName;
  private String password;
  private String phone;
  private String email;
}

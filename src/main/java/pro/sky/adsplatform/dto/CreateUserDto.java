package pro.sky.adsplatform.dto;
import lombok.Data;

@Data
public class CreateUserDto {
  private String firstName;
  private String lastName;
  private String password;
  private String phone;
  private String email;
}

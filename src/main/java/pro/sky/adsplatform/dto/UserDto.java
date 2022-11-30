package pro.sky.adsplatform.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class UserDto {
    @NotBlank(message = "email must be filled")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "email must be correct")
    private String email;
    @NotBlank(message = "firstName must be filled")
    private String firstName;
    @NotNull(message = "id must be not null")
    private Integer id;
    @NotBlank(message = "lastName must be filled")
    private String lastName;
    @NotBlank(message = "phone must be filled")
    @Pattern(regexp = "\\+7[0-9]{10}", message = "phone number must be start from +7, then - 10 numbers")
    private String phone;
}

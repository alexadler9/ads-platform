package pro.sky.adsplatform.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class NewPasswordDto {
    @NotBlank(message = "newPassword must be filled")
    private String currentPassword;
    @NotBlank(message = "newPassword must be filled")
    private String newPassword;
}

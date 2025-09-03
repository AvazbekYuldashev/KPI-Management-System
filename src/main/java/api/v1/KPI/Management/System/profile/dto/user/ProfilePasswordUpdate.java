package api.v1.KPI.Management.System.profile.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfilePasswordUpdate {
    @NotBlank(message = "Wrong old password ")
    private String oldPassword;
    @NotBlank(message = "Wrong new password")
    private String newPassword;
}

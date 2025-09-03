package api.v1.KPI.Management.System.profile.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUsernameUpdateDTO {
    @NotBlank(message = "Wrong username")
    private String username;
}

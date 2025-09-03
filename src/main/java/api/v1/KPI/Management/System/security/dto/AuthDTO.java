package api.v1.KPI.Management.System.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthDTO {
    @NotBlank(message = "Username required")
    private String username;

    @NotBlank(message = "New password required")
    private String password;
}

package api.v1.KPI.Management.System.profile.dto.admin;

import api.v1.KPI.Management.System.security.enums.GeneralStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileChangeStatusDTO {
    @NotNull
    private String id;
    @NotNull
    private GeneralStatus status;
}

package api.v1.KPI.Management.System.profile.dto.admin;

import api.v1.KPI.Management.System.profile.enums.ProfileRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUpdateRole {
    private String id;
    private ProfileRole role;
}

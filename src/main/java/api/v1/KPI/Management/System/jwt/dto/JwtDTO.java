package api.v1.KPI.Management.System.jwt.dto;

import api.v1.KPI.Management.System.profile.enums.ProfileRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtDTO {
    private String id;
    private String username;
    private List<ProfileRole> roleList;
}

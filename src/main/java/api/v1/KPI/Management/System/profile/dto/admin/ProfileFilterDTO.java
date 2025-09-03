package api.v1.KPI.Management.System.profile.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileFilterDTO {
    private String name;
    private String surname;
    private String username;

}

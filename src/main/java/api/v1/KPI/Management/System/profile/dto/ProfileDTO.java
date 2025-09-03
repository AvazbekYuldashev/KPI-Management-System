package api.v1.KPI.Management.System.profile.dto;

import api.v1.KPI.Management.System.attach.dto.AttachDTO;
import api.v1.KPI.Management.System.profile.enums.ProfileRole;
import api.v1.KPI.Management.System.security.enums.GeneralStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {
    private String id;
    private String name;
    private String surname;
    private String username;
    private AttachDTO photo;
    private GeneralStatus status;

    private List<ProfileRole> roleList;
    private String jwt;
    private Boolean isEmployee;
    private String employeeId;
    private LocalDateTime createdDate;
}

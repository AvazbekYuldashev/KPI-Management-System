package api.v1.KPI.Management.System.kpi.dto.request.admin;

import api.v1.KPI.Management.System.attach.dto.AttachDTO;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class KpiAdminCreateDTO {
    private String title;
    private String description;
    private String photoId;
    private String managerId;
}

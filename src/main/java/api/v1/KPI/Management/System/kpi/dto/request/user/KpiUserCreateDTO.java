package api.v1.KPI.Management.System.kpi.dto.request.user;

import api.v1.KPI.Management.System.attach.dto.AttachDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KpiUserCreateDTO {
    private String title;
    private String description;
    private String photoId;
    private String manangerId;
}

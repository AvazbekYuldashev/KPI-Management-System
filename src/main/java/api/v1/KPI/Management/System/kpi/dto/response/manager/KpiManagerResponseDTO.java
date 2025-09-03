package api.v1.KPI.Management.System.kpi.dto.response.manager;

import api.v1.KPI.Management.System.attach.dto.AttachDTO;
import api.v1.KPI.Management.System.kpi.enums.KpiStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class KpiManagerResponseDTO {
    private String id;

    private String profileId;
    private String title;
    private String description;
    private AttachDTO photoId;
    private String manangerId;
    private KpiStatus manangerCheck;
    private KpiStatus adminCheck;
    private Integer points;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}

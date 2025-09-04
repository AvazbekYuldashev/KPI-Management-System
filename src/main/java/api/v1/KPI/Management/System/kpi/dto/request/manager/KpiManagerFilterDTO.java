package api.v1.KPI.Management.System.kpi.dto.request.manager;

import api.v1.KPI.Management.System.attach.dto.AttachDTO;
import api.v1.KPI.Management.System.kpi.enums.KpiStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class KpiManagerFilterDTO {
    private String id;
    private String profileId;
    private String title;
    private String description;

    private AttachDTO photoId;
    private String manangerId;
    private KpiStatus manangerCheck;
    private KpiStatus adminCheck;
    private Integer points;

    private LocalDate createdDateFrom;
    private LocalDate createdDateTo;
    private LocalDate updatedDateFrom;
    private LocalDate updatedDateTo;
}

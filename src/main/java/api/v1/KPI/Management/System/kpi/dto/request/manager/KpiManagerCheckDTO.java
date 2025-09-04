package api.v1.KPI.Management.System.kpi.dto.request.manager;

import api.v1.KPI.Management.System.kpi.enums.KpiStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KpiManagerCheckDTO {
    private String kpiId;
    private KpiStatus status;
}

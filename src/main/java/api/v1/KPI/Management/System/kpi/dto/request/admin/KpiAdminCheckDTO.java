package api.v1.KPI.Management.System.kpi.dto.request.admin;

import api.v1.KPI.Management.System.kpi.enums.KpiStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KpiAdminCheckDTO {
    private String kpiId;
    private KpiStatus status;
}

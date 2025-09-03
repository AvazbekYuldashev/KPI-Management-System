package api.v1.KPI.Management.System.kpi.service.admin;

import api.v1.KPI.Management.System.kpi.mapper.admin.KpiAdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KpiAdminService {
    @Autowired
    private KpiAdminMapper kpiAdminMapper;
}

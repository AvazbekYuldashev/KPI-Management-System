package api.v1.KPI.Management.System.kpi.service.manager;

import api.v1.KPI.Management.System.kpi.mapper.manager.KpiManagerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KpiManagerService {
    @Autowired
    private KpiManagerMapper kpiManagerMapper;
}

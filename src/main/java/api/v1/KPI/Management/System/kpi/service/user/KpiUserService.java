package api.v1.KPI.Management.System.kpi.service.user;

import api.v1.KPI.Management.System.kpi.mapper.user.KpiUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KpiUserService {
    @Autowired
    private KpiUserMapper kpiUserMapper;
}

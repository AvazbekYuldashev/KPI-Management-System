package api.v1.KPI.Management.System.kpi.controller.manager;

import api.v1.KPI.Management.System.kpi.service.manager.KpiManagerService;
import api.v1.KPI.Management.System.kpi.service.user.KpiUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manager/profile")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
public class KpiManagerController {
    @Autowired
    private KpiManagerService  kpiManagerService;
}

package api.v1.KPI.Management.System.kpi.controller.admin;

import api.v1.KPI.Management.System.kpi.service.admin.KpiAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/profile")
@PreAuthorize("hasRole('ADMIN')")
public class KpiAdminController {
    @Autowired
    private KpiAdminService kpiAdminService;


}

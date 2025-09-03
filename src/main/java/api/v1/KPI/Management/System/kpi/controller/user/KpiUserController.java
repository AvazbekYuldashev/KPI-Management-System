package api.v1.KPI.Management.System.kpi.controller.user;

import api.v1.KPI.Management.System.kpi.service.user.KpiUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/profile")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
public class KpiUserController {
    @Autowired
    private KpiUserService kpiUserService;


}

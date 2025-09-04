package api.v1.KPI.Management.System.kpi.controller.manager;

import api.v1.KPI.Management.System.app.dto.AppResponse;
import api.v1.KPI.Management.System.kpi.dto.request.admin.KpiAdminCheckDTO;
import api.v1.KPI.Management.System.kpi.dto.request.admin.KpiAdminCreateDTO;
import api.v1.KPI.Management.System.kpi.dto.request.admin.KpiAdminFilterDTO;
import api.v1.KPI.Management.System.kpi.dto.request.admin.KpiAdminUpdatePhotoDTO;
import api.v1.KPI.Management.System.kpi.dto.request.manager.KpiManagerCheckDTO;
import api.v1.KPI.Management.System.kpi.dto.request.manager.KpiManagerCreateDTO;
import api.v1.KPI.Management.System.kpi.dto.request.manager.KpiManagerFilterDTO;
import api.v1.KPI.Management.System.kpi.dto.request.manager.KpiManagerUpdatePhotoDTO;
import api.v1.KPI.Management.System.kpi.dto.response.admin.KpiAdminResponseDTO;
import api.v1.KPI.Management.System.kpi.dto.response.manager.KpiManagerResponseDTO;
import api.v1.KPI.Management.System.kpi.service.manager.KpiManagerService;
import api.v1.KPI.Management.System.kpi.service.user.KpiUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manager/profile")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
public class KpiManagerController {
    @Autowired
    private KpiManagerService  kpiManagerService;

    @PostMapping("")
    public ResponseEntity<KpiManagerResponseDTO> create(@RequestBody KpiManagerCreateDTO dto){
        return ResponseEntity.ok().body(kpiManagerService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<KpiManagerResponseDTO> getById(@PathVariable String id){
        return ResponseEntity.ok().body(kpiManagerService.getById(id));
    }

    @PostMapping("/filter")
    public ResponseEntity<Page<KpiManagerResponseDTO>> filter(@RequestBody KpiManagerFilterDTO dto,
                                                            @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                            @RequestParam(value = "size", defaultValue = "10") Integer size){
        return ResponseEntity.ok().body(kpiManagerService.filter(dto, getCurrentPage(page), size));
    }

    @PatchMapping("/photo")
    public ResponseEntity<AppResponse<String>> updatePhoto(@RequestBody KpiManagerUpdatePhotoDTO dto){
        return ResponseEntity.ok().body(kpiManagerService.updatePhoto(dto));
    }

    @PatchMapping("/check")
    public ResponseEntity<AppResponse<String>> updateStatus(@RequestBody KpiManagerCheckDTO dto){
        return ResponseEntity.ok().body(kpiManagerService.updateStatus(dto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<AppResponse<String>> delete(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(kpiManagerService.deleteById(id));
    }

    @GetMapping("/ball/{id}")
    public ResponseEntity<AppResponse<Integer>> getBall(@PathVariable String id){
        return ResponseEntity.ok().body(kpiManagerService.getBallByProfileId(id));
    }
    public static int getCurrentPage(Integer page) {
        return page > 0 ? page - 1 : 1;
    }

}

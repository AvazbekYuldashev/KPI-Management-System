package api.v1.KPI.Management.System.kpi.controller.admin;

import api.v1.KPI.Management.System.app.dto.AppResponse;
import api.v1.KPI.Management.System.kpi.dto.request.admin.*;
import api.v1.KPI.Management.System.kpi.dto.response.admin.KpiAdminResponseDTO;
import api.v1.KPI.Management.System.kpi.service.admin.KpiAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/kpi")
@PreAuthorize("hasRole('ADMIN')")
public class  KpiAdminController {
    @Autowired
    private KpiAdminService kpiAdminService;

    @PostMapping("")
    public ResponseEntity<KpiAdminResponseDTO> create(@RequestBody KpiAdminCreateDTO dto){
        return ResponseEntity.ok().body(kpiAdminService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<KpiAdminResponseDTO> getById(@PathVariable String id){
        return ResponseEntity.ok().body(kpiAdminService.getById(id));
    }

    @PostMapping("/filter")
    public ResponseEntity<Page<KpiAdminResponseDTO>> filter(@RequestBody KpiAdminFilterDTO dto,
                                                   @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                   @RequestParam(value = "size", defaultValue = "10") Integer size){
        return ResponseEntity.ok().body(kpiAdminService.filter(dto, getCurrentPage(page), size));
    }

    @PatchMapping("/photo")
    public ResponseEntity<AppResponse<String>> updatePhoto(@RequestBody KpiAdminUpdatePhotoDTO dto){
        return ResponseEntity.ok().body(kpiAdminService.updatePhoto(dto));
    }

    @PatchMapping("/check")
    public ResponseEntity<AppResponse<String>> updateStatus(@RequestBody KpiAdminCheckDTO dto){
        return ResponseEntity.ok().body(kpiAdminService.updateStatus(dto));
    }

    @PatchMapping("/point")
    public ResponseEntity<AppResponse<String>> updatePoint(@RequestBody KpiAdminPointDTO dto){
        return ResponseEntity.ok().body(kpiAdminService.updatePoint(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppResponse<String>> delete(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(kpiAdminService.deleteById(id));
    }
    public static int getCurrentPage(Integer page) {
        return page > 0 ? page - 1 : 1;
    }

    @GetMapping("/ball/{id}")
    public ResponseEntity<AppResponse<Integer>> getBall(@PathVariable String id){
        return ResponseEntity.ok().body(kpiAdminService.getBallByProfileId(id));
    }

}

package api.v1.KPI.Management.System.kpi.controller.user;

import api.v1.KPI.Management.System.app.dto.AppResponse;
import api.v1.KPI.Management.System.kpi.dto.request.admin.*;
import api.v1.KPI.Management.System.kpi.dto.request.user.KpiUserCreateDTO;
import api.v1.KPI.Management.System.kpi.dto.request.user.KpiUserFilterDTO;
import api.v1.KPI.Management.System.kpi.dto.request.user.KpiUserUpdatePhotoDTO;
import api.v1.KPI.Management.System.kpi.dto.response.admin.KpiAdminResponseDTO;
import api.v1.KPI.Management.System.kpi.dto.response.user.KpiUserResponseDTO;
import api.v1.KPI.Management.System.kpi.service.admin.KpiAdminService;
import api.v1.KPI.Management.System.kpi.service.user.KpiUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/profile")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
public class KpiUserController {
    @Autowired
    private KpiUserService kpiUserService;

    @PostMapping("")
    public ResponseEntity<KpiUserResponseDTO> create(@RequestBody KpiUserCreateDTO dto){
        return ResponseEntity.ok().body(kpiUserService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<KpiUserResponseDTO> getById(@PathVariable String id){
        return ResponseEntity.ok().body(kpiUserService.getById(id));
    }

    @PostMapping("/filter")
    public ResponseEntity<Page<KpiUserResponseDTO>> filter(@RequestBody KpiUserFilterDTO dto,
                                                            @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                            @RequestParam(value = "size", defaultValue = "10") Integer size){
        return ResponseEntity.ok().body(kpiUserService.filter(dto, getCurrentPage(page), size));
    }

    @PatchMapping("/photo")
    public ResponseEntity<AppResponse<String>> updatePhoto(@RequestBody KpiUserUpdatePhotoDTO dto){
        return ResponseEntity.ok().body(kpiUserService.updatePhoto(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppResponse<String>> delete(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(kpiUserService.deleteById(id));
    }
    public static int getCurrentPage(Integer page) {
        return page > 0 ? page - 1 : 1;
    }


}

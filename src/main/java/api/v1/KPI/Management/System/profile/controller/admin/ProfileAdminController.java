package api.v1.KPI.Management.System.profile.controller.admin;


import api.v1.KPI.Management.System.app.dto.AppResponse;
import api.v1.KPI.Management.System.app.enums.AppLanguage;
import api.v1.KPI.Management.System.profile.dto.ProfileDTO;
import api.v1.KPI.Management.System.profile.dto.ProfileResponseDTO;
import api.v1.KPI.Management.System.profile.dto.admin.*;
import api.v1.KPI.Management.System.profile.dto.user.ProfilePhotoUpdate;
import api.v1.KPI.Management.System.profile.service.admin.ProfileAdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/profile")
@PreAuthorize("hasRole('ADMIN')")
public class ProfileAdminController {
    @Autowired
    private ProfileAdminService profileAdminService;

    @GetMapping("")
    public ResponseEntity<ProfileDTO> getMe(@RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang){
        return ResponseEntity.ok().body(profileAdminService.getMe(lang));
    }

    @GetMapping("/all")
    public ResponseEntity<PageImpl<ProfileResponseDTO>> getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                @RequestParam(value = "size", defaultValue = "15") int size) {
        return ResponseEntity.ok().body(profileAdminService.getAll(getCurrentPage(page), size));
    }


    @PutMapping("/photo")
    public ResponseEntity<AppResponse<String>> updatePhoto(@Valid @RequestBody ProfilePhotoUpdate dto,
                                                           @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang){
        return ResponseEntity.ok().body(profileAdminService.updatePhoto(dto.getPhotoId(), lang));
    }

    @PatchMapping("/status")
    public ResponseEntity<AppResponse<String>> changeStatus(@Valid @RequestBody ProfileChangeStatusDTO dto,
                                                            @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang){
        return ResponseEntity.ok().body(profileAdminService.changeStatus(dto, lang));
    }

    @PatchMapping("/role")
    public ResponseEntity<AppResponse<String>> changeRole(@Valid @RequestBody ProfileChangeRoleDTO dto,
                                                          @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang){
        return ResponseEntity.ok().body(profileAdminService.changeRole(dto, lang));
    }

    @PatchMapping("/password")
    public ResponseEntity<AppResponse<String>> updatePassword(@Valid @RequestBody ChangePasswordDTO dto,
                                                              @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang){
        return ResponseEntity.ok().body(profileAdminService.changePassword(dto, lang));
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<AppResponse<String>> delete(@PathVariable("id") String id,
                                                       @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang ) {

        return ResponseEntity.ok().body(profileAdminService.deletebyId(id, lang));
    }


    @PostMapping("/filter")
    public ResponseEntity<Page<ProfileResponseDTO>> filter(@RequestBody ProfileFilterDTO dto,
                                                           @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                           @RequestParam(value = "size", defaultValue = "10") Integer size){
        return ResponseEntity.ok().body(profileAdminService.filter(dto, getCurrentPage(page), size));
    }

    public static int getCurrentPage(Integer page) {
        return page > 0 ? page - 1 : 1;
    }
}

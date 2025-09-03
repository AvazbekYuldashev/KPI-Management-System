package api.v1.KPI.Management.System.profile.controller.superAdmin;


import api.v1.KPI.Management.System.app.dto.AppResponse;
import api.v1.KPI.Management.System.app.enums.AppLanguage;
import api.v1.KPI.Management.System.profile.dto.ProfileDTO;
import api.v1.KPI.Management.System.profile.dto.admin.*;
import api.v1.KPI.Management.System.profile.service.admin.ProfileAdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/profile/superAdmin")
@PreAuthorize("hasRole('SUPERADMIN')")
public class ProfileSuperAdminController {
    @Autowired
    private ProfileAdminService profileService;

    @PutMapping("/password")
    public ResponseEntity<AppResponse<String>> updatePassword(@Valid @RequestBody ProfilePasswordUpdateAdmin dto,
                                                              @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang){
        return ResponseEntity.ok().body(profileService.updatePassword(dto, lang));
    }   // TODO DONE

    @PostMapping("/filter")
    public ResponseEntity<Page<ProfileDTO>> filter(@RequestBody ProfileFilterDTO dto,
                                                   @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                   @RequestParam(value = "size", defaultValue = "10") Integer size){
        return ResponseEntity.ok().body(profileService.filter(dto, getCurrentPage(page), size));
    }   // TODO DONE

    @PutMapping("/status/{id}")
    public ResponseEntity<AppResponse<String>> changeStatus(@PathVariable("id") String id,
                                                             @RequestBody ProfileChangeStatusDTO dto,
                                                             @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang) {
        return ResponseEntity.ok().body(profileService.changeStatus(id, dto, lang));
    }   // TODO DONE

    @GetMapping("/{username}")
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable("username") String username,
                                                              @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang) {
        return ResponseEntity.ok().body(profileService.findByUsername(username, lang));
    }   // TODO DONE

    @PatchMapping("/add/admin")
    public ResponseEntity<AppResponse<String>> addRole(@RequestBody ProfileUpdateRole dto,
                                                        @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang){
        return ResponseEntity.ok().body(profileService.addRole(dto,  lang));
    }   // TODO DONE

    @PatchMapping("/remove/admin")
    public ResponseEntity<AppResponse<String>> removeRole(@RequestBody ProfileUpdateRole dto,
                                                            @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang) {
        return ResponseEntity.ok().body(profileService.removeRole(dto, lang));
    }   // TODO DONE

    public static int getCurrentPage(Integer page) {
        return page > 0 ? page - 1 : 1;
    }
}

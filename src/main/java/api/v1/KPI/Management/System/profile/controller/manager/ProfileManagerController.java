package api.v1.KPI.Management.System.profile.controller.manager;


import api.v1.KPI.Management.System.app.enums.AppLanguage;
import api.v1.KPI.Management.System.profile.dto.ProfileDTO;
import api.v1.KPI.Management.System.profile.dto.ProfileResponseDTO;
import api.v1.KPI.Management.System.profile.service.manager.ProfileManagerService;
import api.v1.KPI.Management.System.profile.service.user.ProfileUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/manager/profile")
@PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
public class ProfileManagerController {
    @Autowired
    private ProfileManagerService profileManagerService;

    @GetMapping("")
    public ResponseEntity<ProfileDTO> getMe(@RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang){
        return ResponseEntity.ok().body(profileManagerService.getMe(lang));
    }

    @GetMapping("/manager")
    public ResponseEntity<List<ProfileResponseDTO>> getAllMananger(@RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang){
        return ResponseEntity.ok().body(profileManagerService.getAllManager(lang));
    }
}

package api.v1.KPI.Management.System.profile.controller.profile;


import api.v1.KPI.Management.System.app.dto.AppResponse;
import api.v1.KPI.Management.System.app.enums.AppLanguage;
import api.v1.KPI.Management.System.profile.dto.ProfileDTO;
import api.v1.KPI.Management.System.profile.dto.user.ProfileDetailUpdateDTO;
import api.v1.KPI.Management.System.profile.dto.user.ProfilePasswordUpdate;
import api.v1.KPI.Management.System.profile.dto.user.ProfilePhotoUpdate;
import api.v1.KPI.Management.System.profile.dto.user.ProfileUsernameUpdateDTO;
import api.v1.KPI.Management.System.profile.service.profile.ProfileService;
import api.v1.KPI.Management.System.security.dto.CodeConfirmDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")

public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @GetMapping("")
    public ResponseEntity<ProfileDTO> getMe(@RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang){
        return ResponseEntity.ok().body(profileService.getMe(lang));
    }

    @PutMapping("/detail")
    public ResponseEntity<AppResponse<String>> update(@Valid @RequestBody ProfileDetailUpdateDTO dto,
                                                      @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang){
        return ResponseEntity.ok().body(profileService.updateDetail(dto, lang));
    }

    @PutMapping("/photo")
    public ResponseEntity<AppResponse<String>> updatePhoto(@Valid @RequestBody ProfilePhotoUpdate dto,
                                                      @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang){
        return ResponseEntity.ok().body(profileService.updatePhoto(dto.getPhotoId(), lang));
    }

    @PutMapping("/password")
    public ResponseEntity<AppResponse<String>> updatePassword(@Valid @RequestBody ProfilePasswordUpdate dto,
                                                      @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang){
        return ResponseEntity.ok().body(profileService.updatePassword(dto, lang));
    }

    @PutMapping("/username")
    public ResponseEntity<AppResponse<String>> updateUsername(@Valid @RequestBody ProfileUsernameUpdateDTO dto,
                                                              @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang){
        return ResponseEntity.ok().body(profileService.updateUsername(dto, lang));
    }

    @PutMapping("/username/confirm")
    public ResponseEntity<AppResponse<String>> updateUsernameConfirm(@Valid @RequestBody CodeConfirmDTO dto,
                                                                     @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang){
        return ResponseEntity.ok().body(profileService.updateUsernameConfirm(dto, lang));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AppResponse<String>> delete(@PathVariable("id") String id,
                                                       @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang ) {

        return ResponseEntity.ok().body(profileService.deletebyId(id, lang));
    }

    public static int getCurrentPage(Integer page) {
        return page > 0 ? page - 1 : 1;
    }

}

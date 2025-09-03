package api.v1.KPI.Management.System.security.controller;


import api.v1.KPI.Management.System.app.enums.AppLanguage;
import api.v1.KPI.Management.System.email.dto.EmailResendDTO;
import api.v1.KPI.Management.System.profile.dto.ProfileDTO;
import api.v1.KPI.Management.System.security.dto.AuthDTO;
import api.v1.KPI.Management.System.security.dto.RegistrationDTO;
import api.v1.KPI.Management.System.security.dto.ResetPasswordConfirmDTO;
import api.v1.KPI.Management.System.security.dto.ResetPasswordDTO;
import api.v1.KPI.Management.System.security.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;


    @Operation(summary = "Create User", description = "Api used for creating new User")
    @PostMapping("/registration")
    public ResponseEntity<String> registration(@Valid @RequestBody RegistrationDTO dto,
                                               @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang) {
        return ResponseEntity.ok().body(authService.registration(dto, lang));
    }

    @Operation(summary = "login by username and password", description = "API used for Login")
    @PostMapping("/login")
    public ResponseEntity<ProfileDTO> login(@Valid @RequestBody AuthDTO dto,
                                            @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang) {
        log.info("Login: " + dto.getUsername());
        return ResponseEntity.ok().body(authService.login(dto, lang));
    }

    @Operation(summary = "verification by link", description = "API used for verification")
    @GetMapping("/registration/email-verification/{token}/{lang}")
    public ResponseEntity<String> emailRegVerification(@PathVariable("token") String token,
                                                  @PathVariable("lang") AppLanguage lang) {
        return ResponseEntity.ok().body(authService.registrationEmailVerification(token, lang));
    }


    @Operation(summary = "verification by link", description = "API used for verification")
    @GetMapping("/registration/email-verification-resend")
    public ResponseEntity<String> emailVerificationResend(@RequestBody EmailResendDTO dto,
                                                        @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang) {
        return ResponseEntity.ok().body(authService.registrationEmailVerificationResend(dto, lang));
    }

    @Operation(summary = "login by username and password", description = "API used for Login")
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordDTO dto,
                                            @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang) {
        return ResponseEntity.ok().body(authService.resetPassword(dto, lang));
    }


    @Operation(summary = "login by username and password", description = "API used for Login")
    @PostMapping("/reset-password-confirm")
    public ResponseEntity<String> resetPasswordConfirm(@Valid @RequestBody ResetPasswordConfirmDTO dto,
                                                @RequestHeader(value = "Accept-Language", defaultValue = "UZ") AppLanguage lang) {
        return ResponseEntity.ok().body(authService.resetPasswordConfrim(dto, lang));
    }


}

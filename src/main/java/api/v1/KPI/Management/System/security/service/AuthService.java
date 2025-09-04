package api.v1.KPI.Management.System.security.service;


import api.v1.KPI.Management.System.app.enums.AppLanguage;
import api.v1.KPI.Management.System.app.service.ResourceBoundleService;
import api.v1.KPI.Management.System.attach.service.AttachService;
import api.v1.KPI.Management.System.email.dto.EmailResendDTO;
import api.v1.KPI.Management.System.email.service.EmailHistoryService;
import api.v1.KPI.Management.System.email.service.EmailSendingService;
import api.v1.KPI.Management.System.email.util.EmailUtil;
import api.v1.KPI.Management.System.exception.exps.AppBadException;
import api.v1.KPI.Management.System.exception.exps.ProfileStatusException;
import api.v1.KPI.Management.System.exception.exps.ResourceConflictException;
import api.v1.KPI.Management.System.exception.exps.ResourceNotFoundException;
import api.v1.KPI.Management.System.jwt.util.JwtUtil;
import api.v1.KPI.Management.System.profile.dto.ProfileDTO;
import api.v1.KPI.Management.System.profile.entity.ProfileEntity;
import api.v1.KPI.Management.System.profile.enums.ProfileRole;
import api.v1.KPI.Management.System.profile.repository.ProfileRepository;
import api.v1.KPI.Management.System.profile.service.profile.ProfileService;
import api.v1.KPI.Management.System.security.dto.AuthDTO;
import api.v1.KPI.Management.System.security.dto.RegistrationDTO;
import api.v1.KPI.Management.System.security.dto.ResetPasswordConfirmDTO;
import api.v1.KPI.Management.System.security.dto.ResetPasswordDTO;
import api.v1.KPI.Management.System.security.enums.GeneralStatus;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private BCryptPasswordEncoder bc;
    @Autowired
    private EmailSendingService emailSendingService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ResourceBoundleService boundleService;
    @Autowired
    private AttachService attachService;
    @Autowired
    private EmailHistoryService emailHistoryService;

    /// Registers the user or continues the previous registration process and sends a confirmation email.
    /// The new user is assigned the admin role.
    public String registration(RegistrationDTO dto, AppLanguage lang) {
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(dto.getUsername());
        if (optional.isPresent()) {
            ProfileEntity profile = optional.get();
            if (profile.getStatus().equals(GeneralStatus.IN_REGISTRATION)) {
                return emailSendingService.sendRegistrationEmail(profile.getId(), profile.getUsername(), lang);
            }
            throw new ProfileStatusException(boundleService.getMessage("you.are.not.fully.registered", lang));
        }
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setUsername(dto.getUsername());
        entity.setPassword(bc.encode(dto.getPassword()));
        entity.setStatus(GeneralStatus.IN_REGISTRATION);
        entity.setVisible(true);
        entity.setLanguage(lang);
        entity.setRole(ProfileRole.ROLE_USER);
        ProfileEntity profile = profileRepository.save(entity);
        return emailSendingService.sendRegistrationEmail(profile.getId(), profile.getUsername(), lang);
    }

    /// This method checks the verification link (token) sent to the user via email.
    /// If the token is valid and the user is not yet fully registered (IN_REGISTRATION), it will be set to ACTIVE.
    /// Otherwise, the verification.failed error will be returned.
    public String registrationEmailVerification(String token, AppLanguage lang) {
        try {
            String id = JwtUtil.decodeRegVerToken(token);
            ProfileEntity profile = profileService.getById(id, lang);
            if (profile.getStatus().equals(GeneralStatus.IN_REGISTRATION)) {
                profileRepository.changeStatus(profile.getId(), GeneralStatus.ACTIVE); // ACTIVE
                return boundleService.getMessage("successfully.registered", lang);
            }
        } catch (JwtException e){}
        throw new AppBadException(boundleService.getMessage("verification.failed", lang));
    }


    /// Logs in the user: checks the username and password, returns a login response if the status is ACTIVE, otherwise returns an error.
    public ProfileDTO login(AuthDTO dto, AppLanguage lang) {
        ProfileEntity profile = profileService.getByUsername(dto.getUsername(), lang);

        if (!bc.matches(dto.getPassword(), profile.getPassword())) {
            throw new ResourceNotFoundException(boundleService.getMessage("username.not.found", lang));
        }
        if (!profile.getStatus().equals(GeneralStatus.ACTIVE)) {
            throw new ResourceNotFoundException(boundleService.getMessage("wrong.status", lang));
        }
        return getLoginResponse(profile, lang);
    }

    /// Generates a login response (ProfileDTO) based on the given profile — returns user information, roles, and a JWT token.
    public ProfileDTO getLoginResponse(ProfileEntity profile, AppLanguage lang) {
        ProfileDTO response = new ProfileDTO();
        response.setName(profile.getName());
        response.setSurname(profile.getSurname());
        response.setUsername(profile.getUsername());
        response.setCreatedDate(profile.getCreatedDate());
        response.setStatus(profile.getStatus());
        response.setPhoto(attachService.getDTOById(profile.getPhotoId()));
        response.setRole(profile.getRole());
        response.setJwt(JwtUtil.encode(profile.getUsername(), profile.getId(), response.getRole())); // retnrn jwt
        return response;
    }

    /// Resends an email confirmation link to the user during the registration process.
    /// If the user is already registered, an error will be thrown.
    public String registrationEmailVerificationResend(EmailResendDTO dto, AppLanguage lang) {
        ProfileEntity profile = profileService.getByUsername(dto.getUsername(), lang);
        if (!profile.getStatus().equals(GeneralStatus.IN_REGISTRATION)) {
            throw new ResourceConflictException(boundleService.getMessage("you.have.previously.confirmed.your.registration", lang));
        }
        return emailSendingService.sendRegistrationEmail(profile.getId(), dto.getUsername(), lang);
    }

    /// Checks the user's status to reset the password.
    /// If the status is ACTIVE, a confirmation message is sent via phone or email.
    public String resetPassword(ResetPasswordDTO dto, AppLanguage lang) {
        ProfileEntity profile = profileService.getByUsername(dto.getUsername(), lang);
        // check
        if (!profile.getStatus().equals(GeneralStatus.ACTIVE)) {throw new ProfileStatusException(boundleService.getMessage("wrong.status", lang));}
        // email or phoneNumber send confirm message
        return emailSendingService.sendResetPasswordEmail(profile.getUsername(), lang);
    }

    /// Confirms password reset: checks username, status, and confirmation code.
    /// If the code is correct, it saves the new password. Otherwise, it returns an error.
    public String resetPasswordConfrim(ResetPasswordConfirmDTO dto, AppLanguage lang) {
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(dto.getUsername());
        if (optional.isEmpty()){
            throw new ResourceNotFoundException(boundleService.getMessage("username.not.found", lang));
        }
        ProfileEntity profile = optional.get();
        if (!profile.getStatus().equals(GeneralStatus.ACTIVE)) {
            throw new ProfileStatusException(boundleService.getMessage("you.are.not.fully.registered", lang));
        }
        if (EmailUtil.isEmail(profile.getUsername())) {
            emailHistoryService.check(dto.getUsername(), dto.getConfirmCode(), lang);
        }
        profileRepository.updatePassword(profile.getId(), bc.encode(dto.getPassword()));
        return boundleService.getMessage("password.changed.successfully", lang);
    }
}
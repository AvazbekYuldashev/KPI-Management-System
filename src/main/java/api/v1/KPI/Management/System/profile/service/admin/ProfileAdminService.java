package api.v1.KPI.Management.System.profile.service.admin;


import api.v1.KPI.Management.System.app.dto.AppResponse;
import api.v1.KPI.Management.System.app.dto.FilterResultDTO;
import api.v1.KPI.Management.System.app.enums.AppLanguage;
import api.v1.KPI.Management.System.app.service.ResourceBoundleService;
import api.v1.KPI.Management.System.attach.service.AttachService;
import api.v1.KPI.Management.System.profile.dto.ProfileDTO;
import api.v1.KPI.Management.System.profile.dto.admin.*;
import api.v1.KPI.Management.System.profile.dto.user.ProfileDetailUpdateDTO;
import api.v1.KPI.Management.System.profile.entity.ProfileEntity;
import api.v1.KPI.Management.System.profile.enums.ProfileRole;
import api.v1.KPI.Management.System.profile.mapper.ProfileMapper;
import api.v1.KPI.Management.System.profile.repository.CustomProfileRepository;
import api.v1.KPI.Management.System.profile.repository.ProfileRepository;
import api.v1.KPI.Management.System.profile.service.ProfileRoleService;
import api.v1.KPI.Management.System.profile.service.user.ProfileService;
import api.v1.KPI.Management.System.security.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import api.v1.KPI.Management.System.exception.exps.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileAdminService extends ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ResourceBoundleService boundleService;
    @Autowired
    private BCryptPasswordEncoder bc;
    @Autowired
    private CustomProfileRepository customProfileRepository;
    @Autowired
    private ProfileMapper profileMapper;
    @Autowired
    private ProfileRoleService profileRoleService;
    @Autowired
    private AttachService attachService;

    /// Updates the current user's first and last name.
    /// If successful, returns a message stating that the update was successful.
    public AppResponse<String> updateDetail(ProfileDetailUpdateDTO dto, AppLanguage lang) {
        String id = SpringSecurityUtil.getCurrentUserId();
        profileRepository.updateDetail(id, dto.getName(), dto.getSurname());
        return new AppResponse<>(boundleService.getMessage("update.successfully.completed", lang));
    }

    /// The user checks the old password and updates the new password.
    /// If the old password is incorrect, an error is returned
    public AppResponse<String> updatePassword(ProfilePasswordUpdateAdmin dto, AppLanguage lang) {
        ProfileEntity profile = getById(dto.getId(), lang);
        profileRepository.updatePassword(profile.getId(), bc.encode(dto.getPassword()));
        return new AppResponse<>(boundleService.getMessage("send.change.password.confirm.code", lang)); //todo message     // chhange confirm possword todo
    }

    /// Finds the profile by the given ID.
    /// Returns an error if not found.
    public ProfileEntity getById(String id, AppLanguage lang) {
        Optional<ProfileEntity> optional = profileRepository.findByIdAndVisibleTrue(id);
        if (optional.isEmpty()){
            throw new ResourceNotFoundException(boundleService.getMessage("profile.not.found", lang) + ": " + id);
        }
        return optional.get();
    }

    /// Updates the status for the given user ID.
    public AppResponse<String> changeStatus(String id, ProfileChangeStatusDTO dto, AppLanguage lang) {
        profileRepository.changeStatus(id, dto.getStatus());
        return new AppResponse<>(boundleService.getMessage("update.successfully.completed", lang));

    }

    public Page<ProfileDTO> filter(ProfileFilterDTO dto, int page, Integer size) {
        FilterResultDTO<ProfileEntity> resultDTO = customProfileRepository.filter(dto, page, size);
        List<ProfileDTO> dtoList = resultDTO.getList().stream()
                .map(profileMapper::toInfoDTO).toList();
        return new PageImpl<>(dtoList, PageRequest.of(page, size), resultDTO.getCount());
    }

    public AppResponse<String> removeRole(ProfileUpdateRole dto, AppLanguage lang) {
        ProfileEntity profile = getById(dto.getId(), lang);
        return profileRoleService.deleteAdminByProfileId(dto, lang);
    }

    public AppResponse<String> addRole(ProfileUpdateRole dto, AppLanguage lang) {
        ProfileEntity profile = getById(dto.getId(), lang);
        if (dto.getRole().equals(ProfileRole.ROLE_ADMIN)){
            return new AppResponse<>(profileRoleService.createAdmin(profile.getId(), lang));
        }
        if (dto.getRole().equals(ProfileRole.ROLE_SUPERADMIN)){
            return new AppResponse<>(profileRoleService.createSuperAdmin(profile.getId(), lang));
        }
        return new AppResponse<>("not updated employee");
    }
}

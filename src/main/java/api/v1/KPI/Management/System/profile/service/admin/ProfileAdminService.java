package api.v1.KPI.Management.System.profile.service.admin;


import api.v1.KPI.Management.System.app.dto.AppResponse;
import api.v1.KPI.Management.System.app.dto.FilterResultDTO;
import api.v1.KPI.Management.System.app.enums.AppLanguage;
import api.v1.KPI.Management.System.app.service.ResourceBoundleService;
import api.v1.KPI.Management.System.attach.service.AttachService;
import api.v1.KPI.Management.System.profile.dto.ProfileResponseDTO;
import api.v1.KPI.Management.System.profile.dto.admin.*;
import api.v1.KPI.Management.System.profile.dto.user.ProfileDetailUpdateDTO;
import api.v1.KPI.Management.System.profile.entity.ProfileEntity;
import api.v1.KPI.Management.System.profile.mapper.ProfileMapper;
import api.v1.KPI.Management.System.profile.repository.CustomProfileRepository;
import api.v1.KPI.Management.System.profile.repository.ProfileRepository;
import api.v1.KPI.Management.System.profile.service.profile.ProfileService;
import api.v1.KPI.Management.System.security.util.SpringSecurityUtil;
import jakarta.validation.Valid;
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
    public AppResponse<String> changeStatus(ProfileChangeStatusDTO dto, AppLanguage lang) {
        profileRepository.changeStatus(dto.getId(), dto.getStatus());
        return new AppResponse<>(boundleService.getMessage("update.successfully.completed", lang));
    }

    public Page<ProfileResponseDTO> filter(ProfileFilterDTO dto, int page, int size) {
        FilterResultDTO<ProfileEntity> resultDTO = customProfileRepository.filter(dto, page, size);
        List<ProfileResponseDTO> dtoList = resultDTO.getList().stream()
                .map(profileMapper::toallResponseDTO).toList();
        return new PageImpl<>(dtoList, PageRequest.of(page, size), resultDTO.getCount());
    }

    ///  Update the role for the given user ID.
    public AppResponse<String> changeRole(@Valid ProfileChangeRoleDTO dto, AppLanguage lang) {
        profileRepository.changeRole(dto.getId(), dto.getRole());
        return new AppResponse<>(boundleService.getMessage("update.successfully.completed", lang));
    }

    public AppResponse<String> changePassword(@Valid ChangePasswordDTO dto, AppLanguage lang) {
        profileRepository.changePassword(dto.getId(), dto.getPassword());
        return new AppResponse<>(boundleService.getMessage("update.successfully.completed", lang));
    }
}

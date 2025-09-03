package api.v1.KPI.Management.System.profile.service;

import api.v1.KPI.Management.System.app.dto.AppResponse;
import api.v1.KPI.Management.System.app.enums.AppLanguage;
import api.v1.KPI.Management.System.app.service.ResourceBoundleService;
import api.v1.KPI.Management.System.exception.exps.ResourceNotFoundException;
import api.v1.KPI.Management.System.profile.dto.admin.ProfileUpdateRole;
import api.v1.KPI.Management.System.profile.entity.ProfileRoleEntity;
import api.v1.KPI.Management.System.profile.enums.ProfileRole;
import api.v1.KPI.Management.System.profile.repository.ProfileRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfileRoleService {

    @Autowired
    private ProfileRoleRepository profileRoleRepository;
    @Autowired
    private ResourceBoundleService boundleService;

    /// The ProfileRoleEntities for the given profileId are retrieved from the database.
    /// If the roles are not found, a ResourceNotFoundException error is thrown. Otherwise, a list of roles of type ProfileRole is returned.
    public List<ProfileRole> getByProfileId(String id, AppLanguage lang) {
        List<ProfileRoleEntity> optional = profileRoleRepository.getAllByProfileIdAndVisibleTrue(id);
        if (optional.isEmpty()) {
            throw new ResourceNotFoundException(boundleService.getMessage("profile.role.not.found", lang)+ ": " + id);
        }
        List<ProfileRole> profileRoles = new ArrayList<>();
        for (ProfileRoleEntity entity : optional) {
            profileRoles.add(entity.getRole());
        }
        return profileRoles;
    }

    /// The createUser method assigns the ROLE_USER role to the user with the given profile ID and saves it to the database.
    public ProfileRoleEntity createUser(String id, AppLanguage lang) {
        ProfileRoleEntity entity = new ProfileRoleEntity();
        entity.setProfileId(id);
        entity.setRole(ProfileRole.ROLE_USER);
        return profileRoleRepository.save(entity);
    }

    /// The createAdmin method creates a ROLE_ADMIN role for the given profile ID and saves it to the database. It returns the ID of the saved role object.
    public String createAdmin(String id, AppLanguage lang) {
        getByProfileId(id, lang);
        ProfileRoleEntity entity = new ProfileRoleEntity();
        entity.setProfileId(id);
        entity.setRole(ProfileRole.ROLE_ADMIN);
        return profileRoleRepository.save(entity).getId();
    }
    /// The createAdmin method creates a ROLE_ADMIN role for the given profile ID and saves it to the database. It returns the ID of the saved role object.
    public String createSuperAdmin(String id, AppLanguage lang) {
        getByProfileId(id, lang);
        ProfileRoleEntity entity = new ProfileRoleEntity();
        entity.setProfileId(id);
        entity.setRole(ProfileRole.ROLE_SUPERADMIN);
        return profileRoleRepository.save(entity).getId();
    }

    /// The deleteRoles method deletes all roles associated with the given profileId from the database.
    public void deleteRoles(String profileId) {
        profileRoleRepository.deleteByProfileId(profileId);
    }


    public AppResponse<String> deleteAdminByProfileId(ProfileUpdateRole dto, AppLanguage lang) {
        List<ProfileRoleEntity> list = profileRoleRepository.getAllByProfileIdAndVisibleTrue(dto.getId());
        if (list.isEmpty()) {
            throw new ResourceNotFoundException(boundleService.getMessage("profile.role.not.found", lang)+ ": " + dto.getId());
        }
        for (ProfileRoleEntity entity : list) {
            if (entity.getRole().equals(dto.getRole())){
                profileRoleRepository.delete(entity);
                return new AppResponse<>("success deleted admin");
            }
        }
        return new  AppResponse<>("error deleted admin");
    }
}

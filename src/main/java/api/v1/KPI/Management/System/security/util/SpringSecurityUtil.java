package api.v1.KPI.Management.System.security.util;


import api.v1.KPI.Management.System.profile.entity.ProfileEntity;
import api.v1.KPI.Management.System.profile.enums.ProfileRole;
import api.v1.KPI.Management.System.security.config.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringSecurityUtil {


    public static CustomUserDetails getCurrentProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        return user;
    }

    public static String getCurrentUserId() {
        CustomUserDetails user = getCurrentProfile();
        return user.getId();
    }

    public static ProfileRole haseRole() {
        CustomUserDetails user = getCurrentProfile();
        return user.getRole();
    }
}

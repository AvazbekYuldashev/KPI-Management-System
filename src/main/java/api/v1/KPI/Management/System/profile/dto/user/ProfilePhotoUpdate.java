package api.v1.KPI.Management.System.profile.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfilePhotoUpdate {
    @NotBlank(message = "Photo Id required")
    private String photoId;

}

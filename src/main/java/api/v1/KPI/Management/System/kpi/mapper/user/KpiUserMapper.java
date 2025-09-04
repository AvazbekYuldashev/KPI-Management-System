package api.v1.KPI.Management.System.kpi.mapper.user;

import api.v1.KPI.Management.System.kpi.dto.request.user.KpiUserCreateDTO;
import api.v1.KPI.Management.System.kpi.dto.response.manager.KpiManagerResponseDTO;
import api.v1.KPI.Management.System.kpi.dto.response.user.KpiUserResponseDTO;
import api.v1.KPI.Management.System.kpi.entity.KpiEntity;
import org.springframework.stereotype.Component;

@Component
public class KpiUserMapper {
    public KpiEntity toCreatedEntity(KpiUserCreateDTO dto) {
        KpiEntity entity = new KpiEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setManagerId(dto.getManangerId());
        entity.setPhotoId(dto.getPhotoId());
        return entity;
    }

    public KpiUserResponseDTO toResponseDTO(KpiEntity entity) {
        KpiUserResponseDTO dto = new KpiUserResponseDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setProfileId(entity.getProfileId());
        dto.setManangerId(entity.getManagerId());
        dto.setManangerCheck(entity.getManagerCheck());
        dto.setAdminCheck(entity.getAdminCheck());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdatedDate(entity.getUpdatedDate());
        dto.setVisible(entity.getVisible());
        dto.setPoints(entity.getPoints());
        dto.setPhotoId(entity.getPhotoId());
        return dto;
    }
}

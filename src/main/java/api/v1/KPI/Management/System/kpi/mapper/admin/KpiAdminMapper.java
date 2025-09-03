package api.v1.KPI.Management.System.kpi.mapper.admin;

import api.v1.KPI.Management.System.kpi.dto.request.admin.KpiAdminCreateDTO;
import api.v1.KPI.Management.System.kpi.dto.response.admin.KpiAdminResponseDTO;
import api.v1.KPI.Management.System.kpi.entity.KpiEntity;
import org.springframework.stereotype.Component;

@Component
public class KpiAdminMapper {

    public KpiEntity toCreatedEntity(KpiAdminCreateDTO dto){
        KpiEntity entity = new KpiEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setManangerId(dto.getManangerId());
        entity.setPhotoId(dto.getPhotoId());
        return entity;
    }

    public KpiAdminResponseDTO toResponseDTO(KpiEntity entity) {
        KpiAdminResponseDTO dto = new KpiAdminResponseDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setProfileId(entity.getProfileId());
        dto.setManangerId(entity.getManangerId());
        dto.setManangerCheck(entity.getManangerCheck());
        dto.setAdminCheck(entity.getAdminCheck());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdatedDate(entity.getUpdatedDate());
        dto.setVisible(entity.getVisible());
        dto.setPoints(entity.getPoints());
        dto.setPhotoId(entity.getPhotoId());
        return dto;
    }
}

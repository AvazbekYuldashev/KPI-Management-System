package api.v1.KPI.Management.System.kpi.service.admin;

import api.v1.KPI.Management.System.app.dto.AppResponse;
import api.v1.KPI.Management.System.app.dto.FilterResultDTO;
import api.v1.KPI.Management.System.app.enums.AppLanguage;
import api.v1.KPI.Management.System.app.service.ResourceBoundleService;
import api.v1.KPI.Management.System.attach.service.AttachService;
import api.v1.KPI.Management.System.exception.exps.ResourceNotFoundException;
import api.v1.KPI.Management.System.kpi.dto.request.admin.KpiAdminCreateDTO;
import api.v1.KPI.Management.System.kpi.dto.request.admin.KpiAdminFilterDTO;
import api.v1.KPI.Management.System.kpi.dto.request.admin.KpiAdminUpdatePhotoDTO;
import api.v1.KPI.Management.System.kpi.dto.response.admin.KpiAdminResponseDTO;
import api.v1.KPI.Management.System.kpi.entity.KpiEntity;
import api.v1.KPI.Management.System.kpi.mapper.admin.KpiAdminMapper;
import api.v1.KPI.Management.System.kpi.repository.KpiCustomRepository;
import api.v1.KPI.Management.System.kpi.repository.KpiRepository;
import api.v1.KPI.Management.System.security.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KpiAdminService {
    @Autowired
    private KpiAdminMapper kpiAdminMapper;
    @Autowired
    private KpiRepository kpiRepository;
    @Autowired
    private KpiCustomRepository kpiCustomRepository;
    @Autowired
    private AttachService attachService;
    @Autowired
    private ResourceBoundleService boundleService;

    public KpiAdminResponseDTO create(KpiAdminCreateDTO dto) {
        KpiEntity entity = kpiAdminMapper.toCreatedEntity(dto);
        entity.setProfileId(SpringSecurityUtil.getCurrentUserId());
        KpiEntity response = kpiRepository.save(entity);
        return kpiAdminMapper.toResponseDTO(response);
    }


    public KpiAdminResponseDTO getById(String id) {
        return kpiAdminMapper.toResponseDTO(get(id));
    }

    public Page<KpiAdminResponseDTO> filter(KpiAdminFilterDTO dto, int page, Integer size) {
        FilterResultDTO<KpiEntity> resultDTO = kpiCustomRepository.filter(dto, page, size);
        List<KpiAdminResponseDTO> dtoList = resultDTO.getList().stream()
                .map(kpiAdminMapper::toResponseDTO).toList();
        return new PageImpl<>(dtoList, PageRequest.of(page, size), resultDTO.getCount());
    }

    public AppResponse<String> updatePhoto(KpiAdminUpdatePhotoDTO dto) {
        KpiEntity kpi = get(dto.getKpiId());
        if (kpi.getPhotoId() != null && ! kpi.getPhotoId().equals(kpi)){
            attachService.deleteSoft(kpi.getPhotoId());
        }
        kpiRepository.updatePhoto(kpi.getId(), dto.getPhotoId());

        return new AppResponse<>(boundleService.getMessage("update.successfully.completed", AppLanguage.EN));
    }

    public KpiEntity get(String id) {
        Optional<KpiEntity> optional = kpiRepository.findByIdAndVisibleTrue(id);
        if (optional.isEmpty()) {
            throw new ResourceNotFoundException("KPI NOT FOUND"); // TODO
        }
        return optional.get();
    }
}

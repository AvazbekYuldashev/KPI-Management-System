package api.v1.KPI.Management.System.kpi.service.manager;

import api.v1.KPI.Management.System.app.dto.AppResponse;
import api.v1.KPI.Management.System.app.dto.FilterResultDTO;
import api.v1.KPI.Management.System.app.enums.AppLanguage;
import api.v1.KPI.Management.System.app.service.ResourceBoundleService;
import api.v1.KPI.Management.System.attach.service.AttachService;
import api.v1.KPI.Management.System.exception.exps.ResourceNotFoundException;
import api.v1.KPI.Management.System.kpi.dto.request.manager.KpiManagerCheckDTO;
import api.v1.KPI.Management.System.kpi.dto.request.manager.KpiManagerCreateDTO;
import api.v1.KPI.Management.System.kpi.dto.request.manager.KpiManagerFilterDTO;
import api.v1.KPI.Management.System.kpi.dto.request.manager.KpiManagerUpdatePhotoDTO;
import api.v1.KPI.Management.System.kpi.dto.response.admin.KpiAdminResponseDTO;
import api.v1.KPI.Management.System.kpi.dto.response.manager.KpiManagerResponseDTO;
import api.v1.KPI.Management.System.kpi.entity.KpiEntity;
import api.v1.KPI.Management.System.kpi.enums.KpiStatus;
import api.v1.KPI.Management.System.kpi.mapper.manager.KpiManagerMapper;
import api.v1.KPI.Management.System.kpi.repository.KpiCustomRepository;
import api.v1.KPI.Management.System.kpi.repository.KpiRepository;
import api.v1.KPI.Management.System.security.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class KpiManagerService {
    @Autowired
    private KpiManagerMapper kpiManagerMapper;
    @Autowired
    private KpiRepository kpiRepository;
    @Autowired
    private KpiCustomRepository kpiCustomRepository;
    @Autowired
    private AttachService attachService;
    @Autowired
    private ResourceBoundleService boundleService;

    public KpiManagerResponseDTO create(KpiManagerCreateDTO dto) {
        KpiEntity entity = kpiManagerMapper.toCreatedEntity(dto);
        entity.setProfileId(SpringSecurityUtil.getCurrentUserId());
        KpiEntity response = kpiRepository.save(entity);
        return kpiManagerMapper.toResponseDTO(response);
    }

    public KpiManagerResponseDTO getById(String id) {
        return kpiManagerMapper.toResponseDTO(get(id));
    }

    public Page<KpiManagerResponseDTO> filter(KpiManagerFilterDTO dto, int page, Integer size) {
        FilterResultDTO<KpiEntity> resultDTO = kpiCustomRepository.managerFilter(dto, page, size, SpringSecurityUtil.getCurrentUserId());
        List<KpiManagerResponseDTO> dtoList = resultDTO.getList().stream()
                .map(kpiManagerMapper::toResponseDTO).toList();
        return new PageImpl<>(dtoList, PageRequest.of(page, size), resultDTO.getCount());
    }

    public AppResponse<String> updatePhoto(KpiManagerUpdatePhotoDTO dto) {
        KpiEntity kpi = get(dto.getKpiId());
        if (!kpi.getProfileId().equals(SpringSecurityUtil.getCurrentUserId())){
            throw new AuthorizationDeniedException("You are not allowed to update this KPI."); // todo exp message
        }
        if (kpi.getPhotoId() != null && !kpi.getPhotoId().equals(dto.getPhotoId())){
            attachService.deleteSoft(kpi.getPhotoId());
        }
        kpiRepository.updatePhoto(kpi.getId(), dto.getPhotoId(), LocalDateTime.now());
        return new AppResponse<>(boundleService.getMessage("update.successfully.completed", AppLanguage.EN));
    }

    public AppResponse<String> updateStatus(KpiManagerCheckDTO dto) {
        KpiEntity kpi = get(dto.getKpiId());
        if (!kpi.getMananger().equals(SpringSecurityUtil.getCurrentUserId())){
            throw new AuthorizationDeniedException("You are not allowed to update this KPI."); // todo exp message
        }
        if (kpi.getAdminCheck() != null){
            throw new AuthorizationDeniedException("You are not allowed to update this KPI."); // todo exp message
        }
        kpiRepository.updateManagerCheck(kpi.getId(), dto.getStatus(), LocalDateTime.now());
        return new AppResponse<>(boundleService.getMessage("update.successfully.completed", AppLanguage.EN));
    }

    public AppResponse<String> deleteById(String id) {
        KpiEntity kpi = get(id);
        if (!kpi.getProfileId().equals(SpringSecurityUtil.getCurrentUserId())){
            throw new AuthorizationDeniedException("You are not allowed to update this KPI."); // todo exp message
        }
        kpiRepository.updateAdminVisible(id);
        return new AppResponse<>(boundleService.getMessage("update.successfully.completed", AppLanguage.EN));
    }

    public KpiEntity get(String id) {
        Optional<KpiEntity> optional = kpiRepository.findByIdAndVisibleTrue(id);
        if (optional.isEmpty()) {
            throw new ResourceNotFoundException("KPI NOT FOUND"); // TODO
        }
        return optional.get();
    }

    public AppResponse<Integer> getBallByProfileId(String id) {
        Integer i = 0;
        List<KpiEntity> list = kpiRepository.findByProfileId(id);
        for (KpiEntity kpiEntity : list) {
            i += kpiEntity.getPoints();
        }
        return new AppResponse<>(i);
    }
}

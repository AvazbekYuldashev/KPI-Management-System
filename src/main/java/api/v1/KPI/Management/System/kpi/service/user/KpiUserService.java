package api.v1.KPI.Management.System.kpi.service.user;

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
import api.v1.KPI.Management.System.kpi.dto.request.user.KpiUserCreateDTO;
import api.v1.KPI.Management.System.kpi.dto.request.user.KpiUserFilterDTO;
import api.v1.KPI.Management.System.kpi.dto.request.user.KpiUserUpdatePhotoDTO;
import api.v1.KPI.Management.System.kpi.dto.response.manager.KpiManagerResponseDTO;
import api.v1.KPI.Management.System.kpi.dto.response.user.KpiUserResponseDTO;
import api.v1.KPI.Management.System.kpi.entity.KpiEntity;
import api.v1.KPI.Management.System.kpi.mapper.manager.KpiManagerMapper;
import api.v1.KPI.Management.System.kpi.mapper.user.KpiUserMapper;
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
public class KpiUserService {
    @Autowired
    private KpiUserMapper kpiUserMapper;
    @Autowired
    private KpiRepository kpiRepository;
    @Autowired
    private KpiCustomRepository kpiCustomRepository;
    @Autowired
    private AttachService attachService;
    @Autowired
    private ResourceBoundleService boundleService;

    public KpiUserResponseDTO create(KpiUserCreateDTO dto) {
        KpiEntity entity = kpiUserMapper.toCreatedEntity(dto);
        entity.setProfileId(SpringSecurityUtil.getCurrentUserId());
        KpiEntity response = kpiRepository.save(entity);
        return kpiUserMapper.toResponseDTO(response);
    }

    public KpiUserResponseDTO getById(String id) {
        return kpiUserMapper.toResponseDTO(get(id));
    }

    public Page<KpiUserResponseDTO> filter(KpiUserFilterDTO dto, int page, Integer size) {
        FilterResultDTO<KpiEntity> resultDTO = kpiCustomRepository.userFilter(dto, page, size, SpringSecurityUtil.getCurrentUserId());
        List<KpiUserResponseDTO> dtoList = resultDTO.getList().stream()
                .map(kpiUserMapper::toResponseDTO).toList();
        return new PageImpl<>(dtoList, PageRequest.of(page, size), resultDTO.getCount());
    }

    public AppResponse<String> updatePhoto(KpiUserUpdatePhotoDTO dto) {
        KpiEntity kpi = get(dto.getKpiId());
        if (!kpi.getProfileId().equals(SpringSecurityUtil.getCurrentUserId())){
            throw new AuthorizationDeniedException("You are not allowed to update this KPI."); // todo exp message
        }
        if (kpi.getManagerCheck() != null){
            throw new AuthorizationDeniedException("You are not allowed to update this KPI."); // todo exp message
        }
        if (kpi.getAdminCheck() != null){
            throw new AuthorizationDeniedException("You are not allowed to update this KPI."); // todo exp message
        }
        if (kpi.getPhotoId() != null && !kpi.getPhotoId().equals(dto.getPhotoId())){
            attachService.deleteSoft(kpi.getPhotoId());
        }
        kpiRepository.updatePhoto(kpi.getId(), dto.getPhotoId(), LocalDateTime.now());
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

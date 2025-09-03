package api.v1.KPI.Management.System.kpi.repository;

import api.v1.KPI.Management.System.app.dto.FilterResultDTO;
import api.v1.KPI.Management.System.kpi.dto.request.admin.KpiAdminFilterDTO;
import api.v1.KPI.Management.System.kpi.entity.KpiEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class KpiCustomRepository {
    @Autowired
    private EntityManager entityManager;

    public FilterResultDTO<KpiEntity> filter(KpiAdminFilterDTO dto, int page, Integer size) {
        StringBuilder queryBuilder = new StringBuilder(" WHERE k.visible = true ");
        Map<String, Object> params = new HashMap<>();

        if (dto.getTitle() != null) {
            queryBuilder.append(" and lower(k.title) like :title ");
            params.put("title", "%" + dto.getTitle().toLowerCase() + "%");
        }
        if (dto.getDescription() != null) {
            queryBuilder.append(" and lower(k.description) like :description ");
            params.put("description", "%" + dto.getDescription().toLowerCase() + "%");
        }
        if (dto.getProfileId() != null) {
            queryBuilder.append(" and k.profile.id = :profileId ");
            params.put("profileId", dto.getProfileId());
        }
        if (dto.getPhotoId() != null) {
            queryBuilder.append(" and k.photo.id = :photoId ");
            params.put("photoId", dto.getPhotoId());
        }

        if (dto.getManangerId() != null) {
            queryBuilder.append(" and k.mananger.id = :manangerId ");
            params.put("manangerId", dto.getManangerId());
        }
        if (dto.getManangerCheck() != null) {
            queryBuilder.append(" and k.mananger_check = :mananger_check ");
            params.put("mananger_check", dto.getManangerCheck());
        }
        if (dto.getAdminCheck() != null) {
            queryBuilder.append(" and k.admin_check = :admin_check ");
            params.put("admin_check", dto.getAdminCheck());
        }
        if (dto.getPoints() != null) {
            queryBuilder.append(" and k.points = :points ");
            params.put("points", dto.getPoints());
        }
        if (dto.getCreatedDateFrom() != null) {
            queryBuilder.append(" and k.updated_at >= :createdDateFrom ");
            params.put("createdDateFrom", dto.getCreatedDateFrom());
        }
        if (dto.getCreatedDateTo() != null) {
            queryBuilder.append(" and k.updated_at <= :createdDateTo ");
            params.put("createdDateTo", dto.getCreatedDateTo());
        }
        if (dto.getUpdatedDateFrom() != null) {
            queryBuilder.append(" and k.updated_at >= :updatedDateFrom ");
            params.put("updatedDateFrom", dto.getUpdatedDateFrom());
        }
        if (dto.getUpdatedDateTo() != null) {
            queryBuilder.append(" and k.updated_at <= :updatedDateTo ");
            params.put("updatedDateTo", dto.getUpdatedDateTo());
        }

        StringBuilder selectBuilder = new StringBuilder("SELECT k FROM KpiEntity as k ").
                append(queryBuilder).
                append(" ORDER BY k.createdDate DESC ");

        StringBuilder countBuilder = new StringBuilder("SELECT COUNT(k) FROM KpiEntity as k").
                append(queryBuilder);


        ///  select query
        Query selectQuery = entityManager.createQuery(selectBuilder.toString());
        selectQuery.setFirstResult(page * size);   // 50
        selectQuery.setMaxResults(size);           // 30
        params.forEach(selectQuery::setParameter);
        List<KpiEntity> list = selectQuery.getResultList();

        ///  count query
        Query countQuery = entityManager.createQuery(countBuilder.toString());

        params.forEach(countQuery::setParameter);
        Long count = (Long) countQuery.getSingleResult();

        return new FilterResultDTO<>(list, count);
    }
}

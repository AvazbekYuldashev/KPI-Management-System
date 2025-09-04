package api.v1.KPI.Management.System.kpi.repository;

import api.v1.KPI.Management.System.app.dto.FilterResultDTO;
import api.v1.KPI.Management.System.kpi.dto.request.admin.KpiAdminFilterDTO;
import api.v1.KPI.Management.System.kpi.dto.request.manager.KpiManagerFilterDTO;
import api.v1.KPI.Management.System.kpi.entity.KpiEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Filter;

@Repository
public class KpiCustomRepository {
    @Autowired
    private EntityManager entityManager;




    public FilterResultDTO<KpiEntity> adminFilter(KpiAdminFilterDTO dto, int page, int size) {
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

        if (dto.getManagerId() != null) {
            queryBuilder.append(" and k.manager.id = :managerId ");
            params.put("managerId", dto.getManagerId());
        }
        if (dto.getManagerCheck() != null) {
            queryBuilder.append(" and k.manager_check = :manager_check ");
            params.put("manager_check", dto.getManagerCheck());
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

        return filter(selectBuilder, countBuilder, params, page, size);
    }

    public FilterResultDTO<KpiEntity> managerFilter(KpiManagerFilterDTO dto, int page, int size, String managerId) {
        StringBuilder queryBuilder = new StringBuilder(" WHERE k.visible = true ");
        Map<String, Object> params = new HashMap<>();

        if (managerId != null && !managerId.isEmpty()) {
            queryBuilder.append(" and k.manager.id = :managerId ");
            params.put("managerId", managerId);
        }
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

        return filter(selectBuilder, countBuilder, params, page, size);
    }

    private FilterResultDTO<KpiEntity> filter(StringBuilder selectBuilder, StringBuilder countBuilder, Map<String, Object> params, int page, int size) {
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

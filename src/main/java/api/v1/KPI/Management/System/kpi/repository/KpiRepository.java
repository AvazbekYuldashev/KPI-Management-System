package api.v1.KPI.Management.System.kpi.repository;

import api.v1.KPI.Management.System.kpi.entity.KpiEntity;
import api.v1.KPI.Management.System.kpi.enums.KpiStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface KpiRepository extends JpaRepository<KpiEntity, String> {

    @Query("SELECT k FROM KpiEntity k WHERE k.id = :idP AND k.visible = true ")
    Optional<KpiEntity> findByIdAndVisibleTrue(@Param("idP") String id);

    @Modifying
    @Transactional
    @Query("UPDATE KpiEntity k SET k.photoId = :photoId, k.updatedDate = :now WHERE k.id = :idP ")
    void updatePhoto(@Param("idP") String id, @Param("photoId") String photoId, @Param("now") LocalDateTime now);

    @Modifying
    @Transactional
    @Query("UPDATE KpiEntity k SET k.managerCheck = :status, k.updatedDate = :now WHERE k.id = :idP ")
    void updateManagerCheck(@Param("idP") String id, @Param("status") KpiStatus status, @Param("now") LocalDateTime now);

    @Modifying
    @Transactional
    @Query("UPDATE KpiEntity k SET k.adminCheck = :status, k.updatedDate = :now WHERE k.id = :idP ")
    void updateAdminCheck(String id, KpiStatus status, @Param("now") LocalDateTime now);

    @Modifying
    @Transactional
    @Query("UPDATE KpiEntity k SET k.points = :points, k.adminCheck = :statusP, k.updatedDate = :now WHERE k.id = :idP ")
    void updatePoint(@Param("idP") String id, @Param("points") Integer points, @Param("statusP") KpiStatus status, @Param("now") LocalDateTime now);

    @Modifying
    @Transactional
    @Query("UPDATE KpiEntity k SET k.visible = false WHERE k.id = :idP ")
    void updateAdminVisible(@Param("idP") String id);

}

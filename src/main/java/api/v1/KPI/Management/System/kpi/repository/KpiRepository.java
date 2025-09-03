package api.v1.KPI.Management.System.kpi.repository;

import api.v1.KPI.Management.System.kpi.entity.KpiEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KpiRepository extends JpaRepository<KpiEntity, String> {

    @Query("SELECT k FROM KpiEntity k WHERE k.id = :idP AND k.visible = true ")
    Optional<KpiEntity> findByIdAndVisibleTrue(@Param("idP") String id);

    @Modifying
    @Transactional
    @Query("UPDATE KpiEntity k SET k.photoId = :photoId WHERE k.id = :idP ")
    void updatePhoto(@Param("idP") String id, @Param("photoId") String photoId);

}

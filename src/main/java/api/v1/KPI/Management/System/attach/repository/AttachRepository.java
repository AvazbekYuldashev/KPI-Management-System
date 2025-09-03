package api.v1.KPI.Management.System.attach.repository;


import api.v1.KPI.Management.System.attach.entity.AttachEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachRepository extends JpaRepository<AttachEntity, String> {

    @Modifying
    @Transactional
    @Query("UPDATE AttachEntity SET visible = FALSE WHERE id = :id")
    void updateVisible(@Param("id") String id);
}

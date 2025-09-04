package api.v1.KPI.Management.System.profile.repository;

import api.v1.KPI.Management.System.app.dto.AppResponse;
import api.v1.KPI.Management.System.profile.entity.ProfileEntity;
import api.v1.KPI.Management.System.profile.enums.ProfileRole;
import api.v1.KPI.Management.System.security.enums.GeneralStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity,String> {

    @Query("SELECT p FROM ProfileEntity p WHERE p.username = :username")
    Optional<ProfileEntity> findByUsernameAndVisibleTrue(@Param("username") String username);

    @Query("SELECT p FROM ProfileEntity p WHERE p.id = :id AND p.visible = true ")
    Optional<ProfileEntity> findByIdAndVisibleTrue(String id);

    @Modifying
    @Transactional
    @Query("UPDATE ProfileEntity SET status = :status WHERE id = :id")
    void changeStatus(@Param("id") String id, @Param("status") GeneralStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE ProfileEntity SET password = :encode WHERE id = :id")
    void updatePassword(@Param("id") String id, @Param("encode") String password);


    @Modifying
    @Transactional
    @Query("UPDATE ProfileEntity SET name = :name, surname = :surname WHERE id = :id")
    void updateDetail(@Param("id") String id, @Param("name") String name, @Param("surname") String surname);

    @Modifying
    @Transactional
    @Query("UPDATE ProfileEntity SET username = :username WHERE id = :id")
    void updateUsername(@Param("id") String id, @Param("username") String username);


    @Modifying
    @Transactional
    @Query("UPDATE ProfileEntity SET tempUsername = :tempUsername WHERE id = :id")
    void updateTempUsername(@Param("id") String id, @Param("tempUsername") String tempUsername);

    @Modifying
    @Transactional
    @Query("UPDATE ProfileEntity SET photoId = :photoId WHERE id = :id")
    void updatePhoto(@Param("id") String id, @Param("photoId") String photoId);

    @Modifying
    @Transactional
    @Query("UPDATE ProfileEntity SET visible = :visible WHERE id = :id")
    void deleteSoftById(@Param("id") String id, @Param("visible") Boolean visible);

    @Query("SELECT p FROM ProfileEntity p WHERE p.visible = true ORDER BY p.createdDate DESC")
    Page<ProfileEntity> findAllPage(PageRequest of);

    @Modifying
    @Transactional
    @Query("UPDATE ProfileEntity SET role = :roleP WHERE id = :idP")
    void changeRole(@Param("idP") String id, @Param("roleP") ProfileRole role);

    @Modifying
    @Transactional
    @Query("UPDATE ProfileEntity SET password = :passwordP WHERE id = :idP")
    void changePassword(@Param("idP") String id, @Param("passwordP") String password);


    @Query("SELECT p FROM ProfileEntity p WHERE p.role = :role AND p.visible = true ORDER BY p.createdDate DESC")
    List<ProfileEntity> findAllByRoleManager(ProfileRole role);
}

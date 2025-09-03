package api.v1.KPI.Management.System.kpi.repository;

import api.v1.KPI.Management.System.kpi.entity.KpiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KpiRepository extends JpaRepository<KpiEntity, String> {

}

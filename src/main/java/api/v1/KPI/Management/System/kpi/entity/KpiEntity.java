package api.v1.KPI.Management.System.kpi.entity;

import api.v1.KPI.Management.System.attach.entity.AttachEntity;
import api.v1.KPI.Management.System.kpi.enums.KpiStatus;
import api.v1.KPI.Management.System.profile.entity.ProfileEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "kpi")
public class KpiEntity {
    @Id
    @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "profile_id")
    private String profileId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "photo_id")
    private String photoId;

    @Column(name = "mananger_id")
    private String manangerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "mananger_check")
    private KpiStatus manangerCheck;

    @Enumerated(EnumType.STRING)
    @Column(name = "admin_check")
    private KpiStatus adminCheck;

    @Column(name = "points")
    private Integer points;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "visible")
    private Boolean visible;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id", insertable = false, updatable = false)
    private AttachEntity photo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mananger_id", insertable = false, updatable = false)
    private ProfileEntity mananger;

    @PrePersist
    protected void onCreate() {
        this.visible = Boolean.TRUE;
        this.createdDate = LocalDateTime.now();
        this.updatedDate  = LocalDateTime.now();
    }
}

package by.itacademy.taskservice.dao.entity;

import by.itacademy.taskservice.core.enums.ProjectStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "projects",
        uniqueConstraints = @UniqueConstraint(name = "projects_name_unique", columnNames = {"name"}))
public class ProjectEntity {
    @Id
    private UUID uuid;
    @CreationTimestamp(source = SourceType.DB)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_create")
    private LocalDateTime dtCreate;

    @Version
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_update")
    private LocalDateTime dtUpdate;
    private String name;
    private String description;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "manager", foreignKey = @ForeignKey(name = "project_users_foreign_key"))
    private UserRefEntity manager;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinTable(
            name="projects_staff",
            joinColumns= @JoinColumn(name="project_uuid"),
            foreignKey = @ForeignKey(name = "projects_staff_project_uuid_fkey"),
            inverseJoinColumns= @JoinColumn(name="staff_uuid"),
            inverseForeignKey = @ForeignKey(name = "users_staff_uuid_fkey")
    )
    private List<UserRefEntity> staff;
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    public ProjectEntity() {
    }

    public ProjectEntity(
            UUID uuid,
            LocalDateTime dtCreate,
            LocalDateTime dtUpdate,
            String name,
            String description,
            UserRefEntity manager,
            List<UserRefEntity> staff,
            ProjectStatus status
    ) {
        this.uuid = uuid;
        this.dtCreate = dtCreate;
        this.dtUpdate = dtUpdate;
        this.name = name;
        this.description = description;
        this.manager = manager;
        this.staff = staff;
        this.status = status;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getDtCreate() {
        return dtCreate;
    }

    public void setDtCreate(LocalDateTime dtCreate) {
        this.dtCreate = dtCreate;
    }

    public LocalDateTime getDtUpdate() {
        return dtUpdate;
    }

    public void setDtUpdate(LocalDateTime dtUpdate) {
        this.dtUpdate = dtUpdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserRefEntity getManager() {
        return manager;
    }

    public void setManager(UserRefEntity manager) {
        this.manager = manager;
    }

    public List<UserRefEntity> getStaff() {
        return staff;
    }

    public void setStaff(List<UserRefEntity> staff) {
        this.staff = staff;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }
}

package com.univercloud.hydro.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "basin_sections")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BasinSection implements Auditable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Section name is required")
    @Size(max = 100, message = "Section name cannot exceed 100 characters")
    @Column(nullable = false)   
    private String name;
    
    @NotNull(message = "Water basin is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "water_basin_id", nullable = false)
    private WaterBasin waterBasin;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
    
    @Size(max = 100, message = "Start point cannot exceed 100 characters")
    @Column(name = "start_point", length = 100)
    private String startPoint;
    
    @Size(max = 100, message = "End point cannot exceed 100 characters")
    @Column(name = "end_point", length = 100)
    private String endPoint;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @JsonIgnore
    @OneToMany(mappedBy = "basinSection", fetch = FetchType.LAZY)
    private List<MonitoringStation> monitoringStations = new ArrayList<>();
    
    @JsonIgnore
    @OneToMany(mappedBy = "basinSection", fetch = FetchType.LAZY)
    private List<Discharge> discharges = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "corporation_id", nullable = false)
    private Corporation corporation;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdBy;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by_user_id")
    private User updatedBy;
    
    // Constructors
    public BasinSection() {
        this.createdAt = LocalDateTime.now();
    }
    
    public BasinSection(String name, WaterBasin waterBasin, String description) {
        this();
        this.name = name;
        this.waterBasin = waterBasin;
        this.description = description;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public WaterBasin getWaterBasin() {
        return waterBasin;
    }
    
    public void setWaterBasin(WaterBasin waterBasin) {
        this.waterBasin = waterBasin;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @JsonProperty("isActive")
    public boolean isActive() {
        return isActive;
    }
    
    @JsonProperty("isActive")
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public String getStartPoint() {
        return startPoint;
    }
    
    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }
    
    public String getEndPoint() {
        return endPoint;
    }
    
    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @JsonIgnore
    public List<MonitoringStation> getMonitoringStations() {
        return monitoringStations;
    }
    
    public void setMonitoringStations(List<MonitoringStation> monitoringStations) {
        this.monitoringStations = monitoringStations;
    }
    
    @JsonIgnore
    public List<Discharge> getDischarges() {
        return discharges;
    }
    
    public void setDischarges(List<Discharge> discharges) {
        this.discharges = discharges;
    }
    
    @Override
    @JsonIgnore
    public Corporation getCorporation() {
        return corporation;
    }
    
    @Override
    public void setCorporation(Corporation corporation) {
        this.corporation = corporation;
    }
    
    @Override
    @JsonIgnore
    public User getCreatedBy() {
        return createdBy;
    }
    
    @Override
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
    
    @Override
    @JsonIgnore
    public User getUpdatedBy() {
        return updatedBy;
    }
    
    @Override
    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "BasinSection{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", waterBasin=" + (waterBasin != null ? waterBasin.getName() : null) +
                ", description='" + description + '\'' +
                ", startPoint='" + startPoint + '\'' +
                ", endPoint='" + endPoint + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

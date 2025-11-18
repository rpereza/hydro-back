package com.univercloud.hydro.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "monitoring_stations")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"}, ignoreUnknown = true)
public class MonitoringStation implements Auditable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Station name is required")
    @Size(max = 200, message = "Station name cannot exceed 200 characters")
    @Column(nullable = false)
    private String name;
    
    
    @NotNull(message = "Basin section is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basin_section_id", nullable = false)
    private BasinSection basinSection;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    
    @Column(name = "latitude", precision = 10, scale = 8)
    private BigDecimal latitude;
    
    @Column(name = "longitude", precision = 11, scale = 8)
    private BigDecimal longitude;
    
    
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "corporation_id", nullable = false)
    private Corporation corporation;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdBy;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by_user_id")
    private User updatedBy;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @JsonIgnore
    @OneToMany(mappedBy = "monitoringStation", fetch = FetchType.LAZY)
    private List<Monitoring> monitorings = new ArrayList<>();
    
    // Constructors
    public MonitoringStation() {
        this.createdAt = LocalDateTime.now();
    }
    
    public MonitoringStation(String name, BasinSection basinSection, String description) {
        this();
        this.name = name;
        this.basinSection = basinSection;
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
    
    public BasinSection getBasinSection() {
        return basinSection;
    }
    
    public void setBasinSection(BasinSection basinSection) {
        this.basinSection = basinSection;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getLatitude() {
        return latitude;
    }
    
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }
    
    public BigDecimal getLongitude() {
        return longitude;
    }
    
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }
    
    
    @JsonProperty("isActive")
    public boolean isActive() {
        return isActive;
    }
    
    @JsonProperty("isActive")
    public void setActive(boolean active) {
        isActive = active;
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
    
    public List<Monitoring> getMonitorings() {
        return monitorings;
    }
    
    public void setMonitorings(List<Monitoring> monitorings) {
        this.monitorings = monitorings;
    }
    
    // Auditable interface implementation
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
        return "MonitoringStation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", basinSection=" + (basinSection != null ? basinSection.getName() : null) +
                ", description='" + description + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}

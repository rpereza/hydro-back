package com.univercloud.hydro.dto;

import com.univercloud.hydro.entity.WaterBasin;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para cuencas hidrográficas.
 */
public class WaterBasinResponse {
    
    private Long id;
    private String name;
    private String description;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    
    // Constructors
    public WaterBasinResponse() {}
    
    public WaterBasinResponse(WaterBasin waterBasin) {
        this.id = waterBasin.getId();
        this.name = waterBasin.getName();
        this.description = waterBasin.getDescription();
        this.isActive = waterBasin.isActive();
        this.createdAt = waterBasin.getCreatedAt();
        this.updatedAt = waterBasin.getUpdatedAt();
        this.createdBy = waterBasin.getCreatedBy() != null ? waterBasin.getCreatedBy().getUsername() : null;
        this.updatedBy = waterBasin.getUpdatedBy() != null ? waterBasin.getUpdatedBy().getUsername() : null;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
}

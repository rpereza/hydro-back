package com.univercloud.hydro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "water_basins")
public class WaterBasin implements Auditable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Basin name is required")
    @Size(max = 150, message = "Basin name cannot exceed 150 characters")
    @Column(nullable = false)
    private String name;
    
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "waterBasin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BasinSection> sections = new ArrayList<>();
    
    @NotNull(message = "Corporation is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "corporation_id", nullable = false)
    private Corporation corporation;
    
    @NotNull(message = "Created by is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdBy;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by_user_id")
    private User updatedBy;
    
    // Constructors
    public WaterBasin() {
        this.createdAt = LocalDateTime.now();
    }
    
    public WaterBasin(String name, String description) {
        this();
        this.name = name;
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
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
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
    
    public List<BasinSection> getSections() {
        return sections;
    }
    
    public void setSections(List<BasinSection> sections) {
        this.sections = sections;
    }
    
    @Override
    public Corporation getCorporation() {
        return corporation;
    }
    
    @Override
    public void setCorporation(Corporation corporation) {
        this.corporation = corporation;
    }
    
    @Override
    public User getCreatedBy() {
        return createdBy;
    }
    
    @Override
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
    
    @Override
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
        return "WaterBasin{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isActive=" + isActive +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

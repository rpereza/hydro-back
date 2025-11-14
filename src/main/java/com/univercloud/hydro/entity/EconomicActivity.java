package com.univercloud.hydro.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "economic_activities")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EconomicActivity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Activity name is required")
    @Size(max = 250, message = "Activity name cannot exceed 250 characters")
    @Column(nullable = false)
    private String name;
    
    @Size(max = 4, message = "Activity code cannot exceed 4 characters")
    @Column(unique = true)
    private String code;
    
    
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @JsonIgnore
    @OneToMany(mappedBy = "economicActivity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DischargeUser> users = new ArrayList<>();
    
    // Constructors
    public EconomicActivity() {
        this.createdAt = LocalDateTime.now();
    }
    
    public EconomicActivity(String name, String code) {
        this();
        this.name = name;
        this.code = code;
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
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
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
    
    @JsonIgnore
    public List<DischargeUser> getUsers() {
        return users;
    }
    
    public void setUsers(List<DischargeUser> users) {
        this.users = users;
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "EconomicActivity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}

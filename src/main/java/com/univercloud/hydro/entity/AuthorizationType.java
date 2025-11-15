package com.univercloud.hydro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "authorization_types")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AuthorizationType implements Auditable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Authorization type name is required")
    @Size(max = 100, message = "Authorization type name cannot exceed 100 characters")
    @Column(nullable = false)
    private String name;
    
    
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
    @OneToMany(mappedBy = "authorizationType", fetch = FetchType.LAZY)
    private List<DischargeUser> users = new ArrayList<>();
    
    // Constructors
    public AuthorizationType() {
        this.createdAt = LocalDateTime.now();
    }
    
    public AuthorizationType(String name) {
        this();
        this.name = name;
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
        return "AuthorizationType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}

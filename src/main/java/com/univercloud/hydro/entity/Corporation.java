package com.univercloud.hydro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "corporations")
public class Corporation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Corporation name is required")
    @Size(max = 200, message = "Corporation name cannot exceed 200 characters")
    @Column(unique = true, nullable = false)
    private String name;
    
    @NotBlank(message = "Corporation code is required")
    @Size(max = 50, message = "Corporation code cannot exceed 50 characters")
    @Column(unique = true, nullable = false)
    private String code;
    
    @NotNull(message = "Owner is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_id", nullable = false)
    private User owner;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "corporation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();
    
    // Constructors
    public Corporation() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Corporation(String name, String code, User owner) {
        this();
        this.name = name;
        this.code = code;
        this.owner = owner;
    }
    
    public Corporation(String name, String code, User owner, String description) {
        this(name, code, owner);
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
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public User getOwner() {
        return owner;
    }
    
    public void setOwner(User owner) {
        this.owner = owner;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
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
    
    public List<User> getUsers() {
        return users;
    }
    
    public void setUsers(List<User> users) {
        this.users = users;
    }
    
    // Helper methods
    public void addUser(User user) {
        this.users.add(user);
        user.setCorporation(this);
    }
    
    public void removeUser(User user) {
        this.users.remove(user);
        user.setCorporation(null);
    }
    
    public boolean isOwner(User user) {
        return this.owner != null && this.owner.getId().equals(user.getId());
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "Corporation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", owner=" + (owner != null ? owner.getUsername() : null) +
                ", description='" + description + '\'' +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}

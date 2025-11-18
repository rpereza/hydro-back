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
@Table(name = "municipalities")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Municipality {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Municipality name is required")
    @Size(max = 50, message = "Municipality name cannot exceed 50 characters")
    @Column(nullable = false)
    private String name;
    
    @Size(max = 3, message = "Municipality code cannot exceed 3 characters")
    @Column(nullable = false)
    private String code;
    
    @NotNull(message = "Department is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
    
    @NotNull(message = "Category is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    @Column(name = "nbi", precision = 6, scale = 2)
    private BigDecimal nbi;
    
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @JsonIgnore
    @OneToMany(mappedBy = "municipality", fetch = FetchType.LAZY)
    private List<DischargeUser> users = new ArrayList<>();
    
    @JsonIgnore
    @NotNull(message = "Created by is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdBy;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by_user_id")
    private User updatedBy;
    
    // Constructors
    public Municipality() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Municipality(String name, String code, Department department, Category category) {
        this();
        this.name = name;
        this.code = code;
        this.department = department;
        this.category = category;
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
    
    public Department getDepartment() {
        return department;
    }
    
    public void setDepartment(Department department) {
        this.department = department;
    }
    
    public Category getCategory() {
        return category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }
    
    public BigDecimal getNbi() {
        return nbi;
    }
    
    public void setNbi(BigDecimal nbi) {
        this.nbi = nbi;
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
    
    public List<DischargeUser> getUsers() {
        return users;
    }
    
    public void setUsers(List<DischargeUser> users) {
        this.users = users;
    }
    
    public User getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
    
    public User getUpdatedBy() {
        return updatedBy;
    }
    
    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "Municipality{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", department=" + (department != null ? department.getName() : null) +
                ", category=" + (category != null ? category.getName() : null) +
                ", nbi=" + nbi +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}

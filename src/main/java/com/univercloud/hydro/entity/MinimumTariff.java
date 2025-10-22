package com.univercloud.hydro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "minimum_tariffs")
public class MinimumTariff {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Year is required")
    @Column(name = "year", nullable = false)
    private Integer year;
    
    @NotNull(message = "DBO value is required")
    @Column(name = "dbo_value", precision = 5, scale = 2, nullable = false)
    private BigDecimal dboValue;
    
    @NotNull(message = "SST value is required")
    @Column(name = "sst_value", precision = 5, scale = 2, nullable = false)
    private BigDecimal sstValue;
    
    @NotNull(message = "IPC value is required")
    @Column(name = "ipc_value", precision = 5, scale = 2, nullable = false)
    private BigDecimal ipcValue;
    
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public MinimumTariff() {
        this.createdAt = LocalDateTime.now();
    }
    
    public MinimumTariff(Integer year, BigDecimal dboValue, BigDecimal sstValue, BigDecimal ipcValue) {
        this();
        this.year = year;
        this.dboValue = dboValue;
        this.sstValue = sstValue;
        this.ipcValue = ipcValue;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getYear() {
        return year;
    }
    
    public void setYear(Integer year) {
        this.year = year;
    }
    
    public BigDecimal getDboValue() {
        return dboValue;
    }
    
    public void setDboValue(BigDecimal dboValue) {
        this.dboValue = dboValue;
    }
    
    public BigDecimal getSstValue() {
        return sstValue;
    }
    
    public void setSstValue(BigDecimal sstValue) {
        this.sstValue = sstValue;
    }
    
    public BigDecimal getIpcValue() {
        return ipcValue;
    }
    
    public void setIpcValue(BigDecimal ipcValue) {
        this.ipcValue = ipcValue;
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
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "MinimumTariff{" +
                "id=" + id +
                ", year=" + year +
                ", dboValue=" + dboValue +
                ", sstValue=" + sstValue +
                ", ipcValue=" + ipcValue +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}

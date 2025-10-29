package com.univercloud.hydro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "project_progress")
public class ProjectProgress implements Auditable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Discharge user is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discharge_user_id", nullable = false)
    private DischargeUser dischargeUser;
    
    @NotNull(message = "Year is required")
    @Column(name = "year", nullable = false)
    private Integer year;
    
    @NotNull(message = "CCI percentage is required")
    @Column(name = "cci_percentage", precision = 7, scale = 2, nullable = false)
    private BigDecimal cciPercentage;
    
    @NotNull(message = "CEV percentage is required")
    @Column(name = "cev_percentage", precision = 7, scale = 2, nullable = false)
    private BigDecimal cevPercentage;
    
    @NotNull(message = "CDS percentage is required")
    @Column(name = "cds_percentage", precision = 7, scale = 2, nullable = false)
    private BigDecimal cdsPercentage;
    
    @NotNull(message = "CCS percentage is required")
    @Column(name = "ccs_percentage", precision = 7, scale = 2, nullable = false)
    private BigDecimal ccsPercentage;
    
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
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public ProjectProgress() {
        this.createdAt = LocalDateTime.now();
    }
    
    public ProjectProgress(DischargeUser dischargeUser, Integer year, BigDecimal cciPercentage,
                          BigDecimal cevPercentage, BigDecimal cdsPercentage, BigDecimal ccsPercentage) {
        this();
        this.dischargeUser = dischargeUser;
        this.year = year;
        this.cciPercentage = cciPercentage;
        this.cevPercentage = cevPercentage;
        this.cdsPercentage = cdsPercentage;
        this.ccsPercentage = ccsPercentage;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public DischargeUser getDischargeUser() {
        return dischargeUser;
    }
    
    public void setDischargeUser(DischargeUser dischargeUser) {
        this.dischargeUser = dischargeUser;
    }
    
    public Integer getYear() {
        return year;
    }
    
    public void setYear(Integer year) {
        this.year = year;
    }
    
    public BigDecimal getCciPercentage() {
        return cciPercentage;
    }
    
    public void setCciPercentage(BigDecimal cciPercentage) {
        this.cciPercentage = cciPercentage;
    }
    
    public BigDecimal getCevPercentage() {
        return cevPercentage;
    }
    
    public void setCevPercentage(BigDecimal cevPercentage) {
        this.cevPercentage = cevPercentage;
    }
    
    public BigDecimal getCdsPercentage() {
        return cdsPercentage;
    }
    
    public void setCdsPercentage(BigDecimal cdsPercentage) {
        this.cdsPercentage = cdsPercentage;
    }
    
    public BigDecimal getCcsPercentage() {
        return ccsPercentage;
    }
    
    public void setCcsPercentage(BigDecimal ccsPercentage) {
        this.ccsPercentage = ccsPercentage;
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
    
    // Auditable interface implementation
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
        return "ProjectProgress{" +
                "id=" + id +
                ", dischargeUser=" + (dischargeUser != null ? dischargeUser.getCompanyName() : null) +
                ", year=" + year +
                ", cciPercentage=" + cciPercentage +
                ", cevPercentage=" + cevPercentage +
                ", cdsPercentage=" + cdsPercentage +
                ", ccsPercentage=" + ccsPercentage +
                ", createdAt=" + createdAt +
                '}';
    }
}

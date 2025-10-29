package com.univercloud.hydro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "discharges")
public class Discharge implements Auditable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Discharge user is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discharge_user_id", nullable = false)
    private DischargeUser dischargeUser;
    
    @NotNull(message = "Basin section is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basin_section_id", nullable = false)
    private BasinSection basinSection;
    
    @NotNull(message = "Municipality is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipality_id", nullable = false)
    private Municipality municipality;
    
    @NotNull(message = "Discharge type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "discharge_type", nullable = false)
    private DischargeType dischargeType;
    
    @NotNull(message = "Number is required")
    @Column(name = "number", nullable = false)
    private Integer number;
    
    @NotNull(message = "Year is required")
    @Column(name = "year", nullable = false)
    private Integer year;
    
    @NotNull(message = "Name is required")
    @Size(max = 200, message = "Name cannot exceed 200 characters")
    @Column(nullable = false)
    private String name;
    
    @Column(name = "discharge_point")
    private String dischargePoint;

    @NotNull(message = "Water resource type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "water_resource_type", nullable = false)
    private WaterResourceType waterResourceType;
    
    @Column(name = "latitude", precision = 10, scale = 8)
    private BigDecimal latitude;
    
    @Column(name = "longitude", precision = 11, scale = 8)
    private BigDecimal longitude;
    
    @Column(name = "is_basin_rehuse", nullable = false)
    private boolean isBasinRehuse = false;
    
    @NotNull(message = "CC DBO Vert is required")
    @Column(name = "cc_dbo_vert", precision = 9, scale = 2, nullable = false)
    private BigDecimal ccDboVert;
    
    @NotNull(message = "CC SST Vert is required")
    @Column(name = "cc_sst_vert", precision = 9, scale = 2, nullable = false)
    private BigDecimal ccSstVert;
    
    @NotNull(message = "CC DBO Cap is required")
    @Column(name = "cc_dbo_cap", precision = 9, scale = 2, nullable = false)
    private BigDecimal ccDboCap;
    
    @NotNull(message = "CC SST Cap is required")
    @Column(name = "cc_sst_cap", precision = 9, scale = 2, nullable = false)
    private BigDecimal ccSstCap;
    
    @NotNull(message = "CC DBO Total is required")
    @Column(name = "cc_dbo_total", precision = 9, scale = 2, nullable = false)
    private BigDecimal ccDboTotal;
    
    @NotNull(message = "CC SST Total is required")
    @Column(name = "cc_sst_total", precision = 9, scale = 2, nullable = false)
    private BigDecimal ccSstTotal;
    
    @NotNull(message = "DQO is required")
    @Column(name = "dqo", precision = 9, scale = 2, nullable = false)
    private BigDecimal dqo;
    
    @Column(name = "is_source_monitored", nullable = false)
    private boolean isSourceMonitored = false;
    
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
    
    @OneToMany(mappedBy = "discharge", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DischargeParameter> dischargeParameters = new ArrayList<>();
    
    @OneToMany(mappedBy = "discharge", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DischargeMonitoring> dischargeMonitorings = new ArrayList<>();
    
    @OneToMany(mappedBy = "discharge", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Invoice> invoices = new ArrayList<>();
    
    
    // Constructors
    public Discharge() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Discharge(DischargeUser dischargeUser, BasinSection basinSection, Municipality municipality,
                    DischargeType dischargeType, Integer number, Integer year) {
        this();
        this.dischargeUser = dischargeUser;
        this.basinSection = basinSection;
        this.municipality = municipality;
        this.dischargeType = dischargeType;
        this.number = number;
        this.year = year;
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
    
    public BasinSection getBasinSection() {
        return basinSection;
    }
    
    public void setBasinSection(BasinSection basinSection) {
        this.basinSection = basinSection;
    }
    
    public Municipality getMunicipality() {
        return municipality;
    }
    
    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
    }
    
    public DischargeType getDischargeType() {
        return dischargeType;
    }
    
    public void setDischargeType(DischargeType dischargeType) {
        this.dischargeType = dischargeType;
    }
    
    public Integer getNumber() {
        return number;
    }
    
    public void setNumber(Integer number) {
        this.number = number;
    }
    
    public Integer getYear() {
        return year;
    }
    
    public void setYear(Integer year) {
        this.year = year;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDischargePoint() {
        return dischargePoint;
    }
    
    public void setDischargePoint(String dischargePoint) {
        this.dischargePoint = dischargePoint;
    }

    public WaterResourceType getWaterResourceType() {
        return waterResourceType;
    }
    
    public void setWaterResourceType(WaterResourceType waterResourceType) {
        this.waterResourceType = waterResourceType;
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
    
    public boolean isBasinRehuse() {
        return isBasinRehuse;
    }
    
    public void setBasinRehuse(boolean isBasinRehuse) {
        this.isBasinRehuse = isBasinRehuse;
    }
    
    public BigDecimal getCcDboVert() {
        return ccDboVert;
    }
    
    public void setCcDboVert(BigDecimal ccDboVert) {
        this.ccDboVert = ccDboVert;
    }
    
    public BigDecimal getCcSstVert() {
        return ccSstVert;
    }
    
    public void setCcSstVert(BigDecimal ccSstVert) {
        this.ccSstVert = ccSstVert;
    }
    
    public BigDecimal getCcDboCap() {
        return ccDboCap;
    }
    
    public void setCcDboCap(BigDecimal ccDboCap) {
        this.ccDboCap = ccDboCap;
    }
    
    public BigDecimal getCcSstCap() {
        return ccSstCap;
    }
    
    public void setCcSstCap(BigDecimal ccSstCap) {
        this.ccSstCap = ccSstCap;
    }
    
    public BigDecimal getCcDboTotal() {
        return ccDboTotal;
    }
    
    public void setCcDboTotal(BigDecimal ccDboTotal) {
        this.ccDboTotal = ccDboTotal;
    }
    
    public BigDecimal getCcSstTotal() {
        return ccSstTotal;
    }
    
    public void setCcSstTotal(BigDecimal ccSstTotal) {
        this.ccSstTotal = ccSstTotal;
    }
    
    public BigDecimal getDqo() {
        return dqo;
    }
    
    public void setDqo(BigDecimal dqo) {
        this.dqo = dqo;
    }
    
    public boolean isSourceMonitored() {
        return isSourceMonitored;
    }
    
    public void setSourceMonitored(boolean isSourceMonitored) {
        this.isSourceMonitored = isSourceMonitored;
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
    
    public List<DischargeParameter> getDischargeParameters() {
        return dischargeParameters;
    }
    
    public void setDischargeParameters(List<DischargeParameter> dischargeParameters) {
        this.dischargeParameters = dischargeParameters;
    }
    
    public List<DischargeMonitoring> getDischargeMonitorings() {
        return dischargeMonitorings;
    }
    
    public void setDischargeMonitorings(List<DischargeMonitoring> dischargeMonitorings) {
        this.dischargeMonitorings = dischargeMonitorings;
    }
    
    public List<Invoice> getInvoices() {
        return invoices;
    }
    
    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
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
        return "Discharge{" +
                "id=" + id +
                ", dischargeUser=" + (dischargeUser != null ? dischargeUser.getCompanyName() : null) +
                ", basinSection=" + (basinSection != null ? basinSection.getName() : null) +
                ", municipality=" + (municipality != null ? municipality.getName() : null) +
                ", dischargeType=" + dischargeType +
                ", number=" + number +
                ", year=" + year +
                ", name='" + name + '\'' +
                ", dischargePoint='" + dischargePoint + '\'' +
                ", waterResourceType=" + waterResourceType +
                ", isBasinRehuse=" + isBasinRehuse +
                ", ccDboVert=" + ccDboVert +
                ", ccSstVert=" + ccSstVert +
                ", ccDboCap=" + ccDboCap +
                ", ccSstCap=" + ccSstCap +
                ", ccDboTotal=" + ccDboTotal +
                ", ccSstTotal=" + ccSstTotal +
                ", dqo=" + dqo +
                ", isSourceMonitored=" + isSourceMonitored +
                ", createdAt=" + createdAt +
                '}';
    }
    
    // Enum for discharge types
    public enum DischargeType {
        ARD("Aguas Residuales Domésticas"),
        ARND("Aguas Residuales No Domésticas");
        
        private final String displayName;
        
        DischargeType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }

        // Enum for water resource types
        public enum WaterResourceType {
            RIVER("Lótico"),
            LAKE("Léntico");
            
            private final String displayName;
            
            WaterResourceType(String displayName) {
                this.displayName = displayName;
            }
            
            public String getDisplayName() {
                return displayName;
            }
        }
}

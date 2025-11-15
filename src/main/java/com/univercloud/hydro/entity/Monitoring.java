package com.univercloud.hydro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "monitorings")
public class Monitoring implements Auditable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Monitoring station is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monitoring_station_id", nullable = false)
    private MonitoringStation monitoringStation;
    
    @NotNull(message = "Monitoring date is required")
    @Column(name = "monitoring_date", nullable = false)
    private LocalDate monitoringDate;
    
    
    @Column(name = "weather_conditions")
    private String weatherConditions;
    
    @Column(name = "water_temperature")
    private Double waterTemperature;
    
    @Column(name = "air_temperature")
    private Double airTemperature;
    
    @Column(name = "notes")
    private String notes;
    
    @Column(name = "performed_by")
    private String performedBy;
    
    @NotNull(message = "OD is required")
    @Column(name = "od", precision = 11, scale = 3, nullable = false)
    private BigDecimal od;
    
    @NotNull(message = "SST is required")
    @Column(name = "sst", precision = 11, scale = 3, nullable = false)
    private BigDecimal sst;
    
    @NotNull(message = "DQO is required")
    @Column(name = "dqo", precision = 11, scale = 3, nullable = false)
    private BigDecimal dqo;
    
    @NotNull(message = "CE is required")
    @Column(name = "ce", precision = 11, scale = 3, nullable = false)
    private BigDecimal ce;
    
    @NotNull(message = "PH is required")
    @Column(name = "ph", precision = 11, scale = 3, nullable = false)
    private BigDecimal ph;
    
    @Column(name = "n", precision = 11, scale = 3)
    private BigDecimal n;
    
    @Column(name = "p", precision = 11, scale = 3)
    private BigDecimal p;
    
    @Column(name = "rnp", precision = 11, scale = 3)
    private BigDecimal rnp;
    
    @NotNull(message = "IOD is required")
    @Column(name = "iod", precision = 11, scale = 3, nullable = false)
    private BigDecimal iod;
    
    @NotNull(message = "ISST is required")
    @Column(name = "isst", precision = 11, scale = 3, nullable = false)
    private BigDecimal isst;
    
    @NotNull(message = "IDQO is required")
    @Column(name = "idqo", precision = 11, scale = 3, nullable = false)
    private BigDecimal idqo;
    
    @NotNull(message = "ICE is required")
    @Column(name = "ice", precision = 11, scale = 3, nullable = false)
    private BigDecimal ice;
    
    @NotNull(message = "IPH is required")
    @Column(name = "iph", precision = 11, scale = 3, nullable = false)
    private BigDecimal iph;
    
    @Column(name = "irnp", precision = 11, scale = 3)
    private BigDecimal irnp;
    
    @NotNull(message = "Caudal volumen is required")
    @Column(name = "caudal_volumen", precision = 12, scale = 2, nullable = false)
    private BigDecimal caudalVolumen;
    
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
    public Monitoring() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Monitoring(MonitoringStation monitoringStation, LocalDate monitoringDate) {
        this();
        this.monitoringStation = monitoringStation;
        this.monitoringDate = monitoringDate;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public MonitoringStation getMonitoringStation() {
        return monitoringStation;
    }
    
    public void setMonitoringStation(MonitoringStation monitoringStation) {
        this.monitoringStation = monitoringStation;
    }
    
    public LocalDate getMonitoringDate() {
        return monitoringDate;
    }
    
    public void setMonitoringDate(LocalDate monitoringDate) {
        this.monitoringDate = monitoringDate;
    }
    
    
    public String getWeatherConditions() {
        return weatherConditions;
    }
    
    public void setWeatherConditions(String weatherConditions) {
        this.weatherConditions = weatherConditions;
    }
    
    public Double getWaterTemperature() {
        return waterTemperature;
    }
    
    public void setWaterTemperature(Double waterTemperature) {
        this.waterTemperature = waterTemperature;
    }
    
    public Double getAirTemperature() {
        return airTemperature;
    }
    
    public void setAirTemperature(Double airTemperature) {
        this.airTemperature = airTemperature;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public String getPerformedBy() {
        return performedBy;
    }
    
    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }
    
    public BigDecimal getOd() {
        return od;
    }
    
    public void setOd(BigDecimal od) {
        this.od = od;
    }
    
    public BigDecimal getSst() {
        return sst;
    }
    
    public void setSst(BigDecimal sst) {
        this.sst = sst;
    }
    
    public BigDecimal getDqo() {
        return dqo;
    }
    
    public void setDqo(BigDecimal dqo) {
        this.dqo = dqo;
    }
    
    public BigDecimal getCe() {
        return ce;
    }
    
    public void setCe(BigDecimal ce) {
        this.ce = ce;
    }
    
    public BigDecimal getPh() {
        return ph;
    }
    
    public void setPh(BigDecimal ph) {
        this.ph = ph;
    }
    
    public BigDecimal getN() {
        return n;
    }
    
    public void setN(BigDecimal n) {
        this.n = n;
    }
    
    public BigDecimal getP() {
        return p;
    }
    
    public void setP(BigDecimal p) {
        this.p = p;
    }
    
    public BigDecimal getRnp() {
        return rnp;
    }
    
    public void setRnp(BigDecimal rnp) {
        this.rnp = rnp;
    }
    
    public BigDecimal getIod() {
        return iod;
    }
    
    public void setIod(BigDecimal iod) {
        this.iod = iod;
    }
    
    public BigDecimal getIsst() {
        return isst;
    }
    
    public void setIsst(BigDecimal isst) {
        this.isst = isst;
    }
    
    public BigDecimal getIdqo() {
        return idqo;
    }
    
    public void setIdqo(BigDecimal idqo) {
        this.idqo = idqo;
    }
    
    public BigDecimal getIce() {
        return ice;
    }
    
    public void setIce(BigDecimal ice) {
        this.ice = ice;
    }
    
    public BigDecimal getIph() {
        return iph;
    }
    
    public void setIph(BigDecimal iph) {
        this.iph = iph;
    }
    
    public BigDecimal getIrnp() {
        return irnp;
    }
    
    public void setIrnp(BigDecimal irnp) {
        this.irnp = irnp;
    }
    
    public BigDecimal getCaudalVolumen() {
        return caudalVolumen;
    }
    
    public void setCaudalVolumen(BigDecimal caudalVolumen) {
        this.caudalVolumen = caudalVolumen;
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
        return "Monitoring{" +
                "id=" + id +
                ", monitoringStation=" + (monitoringStation != null ? monitoringStation.getName() : null) +
                ", monitoringDate=" + monitoringDate +
                ", weatherConditions='" + weatherConditions + '\'' +
                ", waterTemperature=" + waterTemperature +
                ", airTemperature=" + airTemperature +
                ", notes='" + notes + '\'' +
                ", performedBy='" + performedBy + '\'' +
                ", od=" + od +
                ", sst=" + sst +
                ", dqo=" + dqo +
                ", ce=" + ce +
                ", ph=" + ph +
                ", n=" + n +
                ", p=" + p +
                ", rnp=" + rnp +
                ", iod=" + iod +
                ", isst=" + isst +
                ", idqo=" + idqo +
                ", ice=" + ice +
                ", iph=" + iph +
                ", irnp=" + irnp +
                ", caudalVolumen=" + caudalVolumen +
                ", createdAt=" + createdAt +
                '}';
    }
}

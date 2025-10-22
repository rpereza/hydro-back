package com.univercloud.hydro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "discharge_monitorings")
public class DischargeMonitoring {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Discharge is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discharge_id", nullable = false)
    private Discharge discharge;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monitoring_station_id")
    private MonitoringStation monitoringStation;
    
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
    
    @Column(name = "latitude", precision = 10, scale = 8)
    private BigDecimal latitude;
    
    @Column(name = "longitude", precision = 11, scale = 8)
    private BigDecimal longitude;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public DischargeMonitoring() {
        this.createdAt = LocalDateTime.now();
    }
    
    public DischargeMonitoring(Discharge discharge, BigDecimal od, BigDecimal sst, BigDecimal dqo,
                              BigDecimal ce, BigDecimal ph, BigDecimal iod, BigDecimal isst,
                              BigDecimal idqo, BigDecimal ice, BigDecimal iph, BigDecimal caudalVolumen) {
        this();
        this.discharge = discharge;
        this.od = od;
        this.sst = sst;
        this.dqo = dqo;
        this.ce = ce;
        this.ph = ph;
        this.iod = iod;
        this.isst = isst;
        this.idqo = idqo;
        this.ice = ice;
        this.iph = iph;
        this.caudalVolumen = caudalVolumen;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Discharge getDischarge() {
        return discharge;
    }
    
    public void setDischarge(Discharge discharge) {
        this.discharge = discharge;
    }
    
    public MonitoringStation getMonitoringStation() {
        return monitoringStation;
    }
    
    public void setMonitoringStation(MonitoringStation monitoringStation) {
        this.monitoringStation = monitoringStation;
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
        return "DischargeMonitoring{" +
                "id=" + id +
                ", discharge=" + (discharge != null ? discharge.getId() : null) +
                ", monitoringStation=" + (monitoringStation != null ? monitoringStation.getName() : null) +
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
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", createdAt=" + createdAt +
                '}';
    }
}

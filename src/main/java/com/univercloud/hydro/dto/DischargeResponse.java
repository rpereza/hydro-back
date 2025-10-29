package com.univercloud.hydro.dto;

import com.univercloud.hydro.entity.Discharge;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para descargas.
 */
public class DischargeResponse {
    
    private Long id;
    private String name;
    private Integer number;
    private Integer year;
    private Discharge.DischargeType dischargeType;
    private Discharge.WaterResourceType waterResourceType;
    private Long municipalityId;
    private String municipalityName;
    private Long basinSectionId;
    private String basinSectionName;
    private Long dischargeUserId;
    private String dischargeUserName;
    private BigDecimal ccDboTotal;
    private BigDecimal ccSstTotal;
    private Boolean isSourceMonitored;
    private Boolean isBasinReuse;
    private String description;
    private String observations;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    
    // Constructors
    public DischargeResponse() {}
    
    public DischargeResponse(Discharge discharge) {
        this.id = discharge.getId();
        this.name = discharge.getName();
        this.number = discharge.getNumber();
        this.year = discharge.getYear();
        this.dischargeType = discharge.getDischargeType();
        this.waterResourceType = discharge.getWaterResourceType();
        this.municipalityId = discharge.getMunicipality() != null ? discharge.getMunicipality().getId() : null;
        this.municipalityName = discharge.getMunicipality() != null ? discharge.getMunicipality().getName() : null;
        this.basinSectionId = discharge.getBasinSection() != null ? discharge.getBasinSection().getId() : null;
        this.basinSectionName = discharge.getBasinSection() != null ? discharge.getBasinSection().getName() : null;
        this.dischargeUserId = discharge.getDischargeUser() != null ? discharge.getDischargeUser().getId() : null;
        this.dischargeUserName = discharge.getDischargeUser() != null ? discharge.getDischargeUser().getCompanyName() : null;
        this.ccDboTotal = discharge.getCcDboTotal();
        this.ccSstTotal = discharge.getCcSstTotal();
        this.isSourceMonitored = discharge.isSourceMonitored();
        this.isBasinReuse = discharge.isBasinRehuse();
        this.description = discharge.getDischargePoint();
        this.observations = discharge.getDischargePoint();
        this.createdAt = discharge.getCreatedAt();
        this.updatedAt = discharge.getUpdatedAt();
        this.createdBy = discharge.getCreatedBy() != null ? discharge.getCreatedBy().getUsername() : null;
        this.updatedBy = discharge.getUpdatedBy() != null ? discharge.getUpdatedBy().getUsername() : null;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Integer getNumber() { return number; }
    public void setNumber(Integer number) { this.number = number; }
    
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    
    public Discharge.DischargeType getDischargeType() { return dischargeType; }
    public void setDischargeType(Discharge.DischargeType dischargeType) { this.dischargeType = dischargeType; }
    
    public Discharge.WaterResourceType getWaterResourceType() { return waterResourceType; }
    public void setWaterResourceType(Discharge.WaterResourceType waterResourceType) { this.waterResourceType = waterResourceType; }
    
    public Long getMunicipalityId() { return municipalityId; }
    public void setMunicipalityId(Long municipalityId) { this.municipalityId = municipalityId; }
    
    public String getMunicipalityName() { return municipalityName; }
    public void setMunicipalityName(String municipalityName) { this.municipalityName = municipalityName; }
    
    public Long getBasinSectionId() { return basinSectionId; }
    public void setBasinSectionId(Long basinSectionId) { this.basinSectionId = basinSectionId; }
    
    public String getBasinSectionName() { return basinSectionName; }
    public void setBasinSectionName(String basinSectionName) { this.basinSectionName = basinSectionName; }
    
    public Long getDischargeUserId() { return dischargeUserId; }
    public void setDischargeUserId(Long dischargeUserId) { this.dischargeUserId = dischargeUserId; }
    
    public String getDischargeUserName() { return dischargeUserName; }
    public void setDischargeUserName(String dischargeUserName) { this.dischargeUserName = dischargeUserName; }
    
    public BigDecimal getCcDboTotal() { return ccDboTotal; }
    public void setCcDboTotal(BigDecimal ccDboTotal) { this.ccDboTotal = ccDboTotal; }
    
    public BigDecimal getCcSstTotal() { return ccSstTotal; }
    public void setCcSstTotal(BigDecimal ccSstTotal) { this.ccSstTotal = ccSstTotal; }
    
    public Boolean getIsSourceMonitored() { return isSourceMonitored; }
    public void setIsSourceMonitored(Boolean isSourceMonitored) { this.isSourceMonitored = isSourceMonitored; }
    
    public Boolean getIsBasinReuse() { return isBasinReuse; }
    public void setIsBasinReuse(Boolean isBasinReuse) { this.isBasinReuse = isBasinReuse; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
}

package com.univercloud.hydro.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO para descarga usado en generación de factura.
 * Incluye solo los campos necesarios para los cálculos de facturación.
 */
public class DischargeInvoiceDto {
    
    private Long id;
    private Integer year;
    private Long dischargeUserId;
    private Boolean dischargeUserIsPublicServiceCompany;
    private BigDecimal municipalityNbi;
    private BigDecimal municipalityCategoryValue;
    private BigDecimal ccDboTotal;
    private BigDecimal ccSstTotal;
    private BigDecimal dqo;
    private List<DischargeParameterInvoiceDto> dischargeParameters;
    private List<DischargeMonitoringInvoiceDto> dischargeMonitorings;
    
    public DischargeInvoiceDto() {
        this.dischargeParameters = new ArrayList<>();
        this.dischargeMonitorings = new ArrayList<>();
    }
    
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
    
    public Long getDischargeUserId() {
        return dischargeUserId;
    }
    
    public void setDischargeUserId(Long dischargeUserId) {
        this.dischargeUserId = dischargeUserId;
    }
    
    public Boolean getDischargeUserIsPublicServiceCompany() {
        return dischargeUserIsPublicServiceCompany;
    }
    
    public void setDischargeUserIsPublicServiceCompany(Boolean dischargeUserIsPublicServiceCompany) {
        this.dischargeUserIsPublicServiceCompany = dischargeUserIsPublicServiceCompany;
    }
    
    public BigDecimal getMunicipalityNbi() {
        return municipalityNbi;
    }
    
    public void setMunicipalityNbi(BigDecimal municipalityNbi) {
        this.municipalityNbi = municipalityNbi;
    }
    
    public BigDecimal getMunicipalityCategoryValue() {
        return municipalityCategoryValue;
    }
    
    public void setMunicipalityCategoryValue(BigDecimal municipalityCategoryValue) {
        this.municipalityCategoryValue = municipalityCategoryValue;
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
    
    public List<DischargeParameterInvoiceDto> getDischargeParameters() {
        return dischargeParameters;
    }
    
    public void setDischargeParameters(List<DischargeParameterInvoiceDto> dischargeParameters) {
        this.dischargeParameters = dischargeParameters;
    }
    
    public List<DischargeMonitoringInvoiceDto> getDischargeMonitorings() {
        return dischargeMonitorings;
    }
    
    public void setDischargeMonitorings(List<DischargeMonitoringInvoiceDto> dischargeMonitorings) {
        this.dischargeMonitorings = dischargeMonitorings;
    }
}

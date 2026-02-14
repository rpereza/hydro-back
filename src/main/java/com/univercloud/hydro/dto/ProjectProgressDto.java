package com.univercloud.hydro.dto;

import java.math.BigDecimal;

/**
 * DTO para progreso de proyecto usado en generación de factura.
 * Solo incluye campos necesarios: porcentajes CCI, CEV, CDS y CCS.
 */
public class ProjectProgressDto {
    
    private BigDecimal cciPercentage;
    private BigDecimal cevPercentage;
    private BigDecimal cdsPercentage;
    private BigDecimal ccsPercentage;
    
    public ProjectProgressDto() {
    }
    
    public ProjectProgressDto(BigDecimal cciPercentage, BigDecimal cevPercentage, BigDecimal cdsPercentage, BigDecimal ccsPercentage) {
        this.cciPercentage = cciPercentage;
        this.cevPercentage = cevPercentage;
        this.cdsPercentage = cdsPercentage;
        this.ccsPercentage = ccsPercentage;
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
}

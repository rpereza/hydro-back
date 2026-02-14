package com.univercloud.hydro.dto;

import com.univercloud.hydro.enums.QualityClasification;
import java.math.BigDecimal;

/**
 * DTO para monitoreos de descarga usados en generación de factura.
 * Solo incluye campos necesarios: numberIcaVariables, qualityClasification y caudalVolumen.
 */
public class DischargeMonitoringInvoiceDto {
    
    private Integer numberIcaVariables;
    private QualityClasification qualityClasification;
    private BigDecimal caudalVolumen;
    
    public DischargeMonitoringInvoiceDto() {
    }
    
    public DischargeMonitoringInvoiceDto(Integer numberIcaVariables, QualityClasification qualityClasification, BigDecimal caudalVolumen) {
        this.numberIcaVariables = numberIcaVariables;
        this.qualityClasification = qualityClasification;
        this.caudalVolumen = caudalVolumen;
    }
    
    public Integer getNumberIcaVariables() {
        return numberIcaVariables;
    }
    
    public void setNumberIcaVariables(Integer numberIcaVariables) {
        this.numberIcaVariables = numberIcaVariables;
    }
    
    public QualityClasification getQualityClasification() {
        return qualityClasification;
    }
    
    public void setQualityClasification(QualityClasification qualityClasification) {
        this.qualityClasification = qualityClasification;
    }
    
    public BigDecimal getCaudalVolumen() {
        return caudalVolumen;
    }
    
    public void setCaudalVolumen(BigDecimal caudalVolumen) {
        this.caudalVolumen = caudalVolumen;
    }
}

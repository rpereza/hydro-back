package com.univercloud.hydro.dto;

import java.math.BigDecimal;

/**
 * DTO para tarifa mínima usada en generación de factura.
 * Solo incluye campos necesarios: dboValue y sstValue.
 */
public class MinimumTariffDto {
    
    private BigDecimal dboValue;
    private BigDecimal sstValue;
    
    public MinimumTariffDto() {
    }
    
    public MinimumTariffDto(BigDecimal dboValue, BigDecimal sstValue) {
        this.dboValue = dboValue;
        this.sstValue = sstValue;
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
}

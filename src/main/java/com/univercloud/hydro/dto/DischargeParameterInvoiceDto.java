package com.univercloud.hydro.dto;

import java.math.BigDecimal;

/**
 * DTO para parámetros de descarga usados en generación de factura.
 * Solo incluye campos necesarios: concDbo y caudalVolumen.
 */
public class DischargeParameterInvoiceDto {
    
    private BigDecimal concDbo;
    private BigDecimal caudalVolumen;
    
    public DischargeParameterInvoiceDto() {
    }
    
    public DischargeParameterInvoiceDto(BigDecimal concDbo, BigDecimal caudalVolumen) {
        this.concDbo = concDbo;
        this.caudalVolumen = caudalVolumen;
    }
    
    public BigDecimal getConcDbo() {
        return concDbo;
    }
    
    public void setConcDbo(BigDecimal concDbo) {
        this.concDbo = concDbo;
    }
    
    public BigDecimal getCaudalVolumen() {
        return caudalVolumen;
    }
    
    public void setCaudalVolumen(BigDecimal caudalVolumen) {
        this.caudalVolumen = caudalVolumen;
    }
}

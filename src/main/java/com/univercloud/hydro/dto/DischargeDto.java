package com.univercloud.hydro.dto;

import com.univercloud.hydro.entity.Discharge;
import java.math.BigDecimal;

/**
 * DTO simplificado para listado de descargas.
 * Contiene solo los campos esenciales para la visualización en listados.
 */
public class DischargeDto {
    
    private Long id;
    private String name;
    private Integer number;
    private Integer year;
    private String dischargePoint;
    private BigDecimal ccDboTotal;
    private BigDecimal ccSstTotal;
    private String companyName;
    
    // Constructors
    public DischargeDto() {}
    
    public DischargeDto(Discharge discharge) {
        this.id = discharge.getId();
        this.name = discharge.getName();
        this.number = discharge.getNumber();
        this.year = discharge.getYear();
        this.dischargePoint = discharge.getDischargePoint();
        this.ccDboTotal = discharge.getCcDboTotal();
        this.ccSstTotal = discharge.getCcSstTotal();
        this.companyName = discharge.getDischargeUser() != null ? discharge.getDischargeUser().getCompanyName() : null;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
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
    
    public String getDischargePoint() {
        return dischargePoint;
    }
    
    public void setDischargePoint(String dischargePoint) {
        this.dischargePoint = dischargePoint;
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
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}

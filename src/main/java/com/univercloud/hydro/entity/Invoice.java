package com.univercloud.hydro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
public class Invoice {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Discharge is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discharge_id", nullable = false)
    private Discharge discharge;
    
    @NotNull(message = "Number is required")
    @Column(name = "number", nullable = false)
    private Integer number;
    
    @NotNull(message = "Year is required")
    @Column(name = "year", nullable = false)
    private Integer year;
    
    @NotNull(message = "Environmental variable is required")
    @Column(name = "environmental_variable", precision = 5, scale = 2, nullable = false)
    private BigDecimal environmentalVariable;
    
    @NotNull(message = "Socioeconomic variable is required")
    @Column(name = "socioeconomic_variable", precision = 5, scale = 2, nullable = false)
    private BigDecimal socioeconomicVariable;

    @NotNull(message = "Economic variable is required")
    @Column(name = "Economic_variable", precision = 5, scale = 2, nullable = false)
    private BigDecimal economicVariable;
    
    @NotNull(message = "Regional factor is required")
    @Column(name = "regional_factor", precision = 5, scale = 2, nullable = false)
    private BigDecimal regionalFactor;
    
    @NotNull(message = "CC DBO is required")
    @Column(name = "cc_dbo", precision = 9, scale = 2, nullable = false)
    private BigDecimal ccDbo;
    
    @NotNull(message = "CC SST is required")
    @Column(name = "cc_sst", precision = 9, scale = 2, nullable = false)
    private BigDecimal ccSst;
    
    @NotNull(message = "Minimum tariff DBO is required")
    @Column(name = "minimum_tariff_dbo", precision = 8, scale = 2, nullable = false)
    private BigDecimal minimumTariffDbo;
    
    @NotNull(message = "Minimum tariff SST is required")
    @Column(name = "minimum_tariff_sst", precision = 8, scale = 2, nullable = false)
    private BigDecimal minimumTariffSst;
    
    @NotNull(message = "Amount to pay DBO is required")
    @Column(name = "amount_to_pay_dbo", precision = 16, scale = 2, nullable = false)
    private BigDecimal amountToPayDbo;
    
    @NotNull(message = "Amount to pay SST is required")
    @Column(name = "amount_to_pay_sst", precision = 16, scale = 2, nullable = false)
    private BigDecimal amountToPaySst;
    
    @NotNull(message = "Total amount to pay is required")
    @Column(name = "total_amount_to_pay", precision = 16, scale = 2, nullable = false)
    private BigDecimal totalAmountToPay;
    
    @NotNull(message = "Number of ICA variables is required")
    @Column(name = "number_ica_variables", nullable = false)
    private Integer numberIcaVariables;
    
    @NotNull(message = "ICA coefficient is required")
    @Column(name = "ica_coefficient", precision = 5, scale = 2, nullable = false)
    private BigDecimal icaCoefficient;
    
    @NotNull(message = "R coefficient is required")
    @Column(name = "r_coefficient", precision = 5, scale = 2, nullable = false)
    private BigDecimal rCoefficient;
    
    @NotNull(message = "B coefficient is required")
    @Column(name = "b_coefficient", precision = 5, scale = 2, nullable = false)
    private BigDecimal bCoefficient;
    
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public Invoice() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Invoice(Discharge discharge, Integer number, Integer year, BigDecimal environmentalVariable,
                   BigDecimal socioeconomicVariable, BigDecimal economicVariable, BigDecimal regionalFactor, 
                   BigDecimal ccDbo, BigDecimal ccSst, BigDecimal minimumTariffDbo, BigDecimal minimumTariffSst,
                   BigDecimal amountToPayDbo, BigDecimal amountToPaySst, BigDecimal totalAmountToPay,
                   Integer numberIcaVariables, BigDecimal icaCoefficient, BigDecimal rCoefficient,
                   BigDecimal bCoefficient) {
        this();
        this.discharge = discharge;
        this.number = number;
        this.year = year;
        this.environmentalVariable = environmentalVariable;
        this.socioeconomicVariable = socioeconomicVariable;
        this.economicVariable = economicVariable;
        this.regionalFactor = regionalFactor;
        this.ccDbo = ccDbo;
        this.ccSst = ccSst;
        this.minimumTariffDbo = minimumTariffDbo;
        this.minimumTariffSst = minimumTariffSst;
        this.amountToPayDbo = amountToPayDbo;
        this.amountToPaySst = amountToPaySst;
        this.totalAmountToPay = totalAmountToPay;
        this.numberIcaVariables = numberIcaVariables;
        this.icaCoefficient = icaCoefficient;
        this.rCoefficient = rCoefficient;
        this.bCoefficient = bCoefficient;
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
    
    public BigDecimal getEnvironmentalVariable() {
        return environmentalVariable;
    }
    
    public void setEnvironmentalVariable(BigDecimal environmentalVariable) {
        this.environmentalVariable = environmentalVariable;
    }
    
    public BigDecimal getSocioeconomicVariable() {
        return socioeconomicVariable;
    }
    
    public void setSocioeconomicVariable(BigDecimal socioeconomicVariable) {
        this.socioeconomicVariable = socioeconomicVariable;
    }

    public BigDecimal getEconomicVariable() {
        return economicVariable;
    }
    
    public void setEconomicVariable(BigDecimal economicVariable) {
        this.economicVariable = economicVariable;
    }
    
    public BigDecimal getRegionalFactor() {
        return regionalFactor;
    }
    
    public void setRegionalFactor(BigDecimal regionalFactor) {
        this.regionalFactor = regionalFactor;
    }
    
    public BigDecimal getCcDbo() {
        return ccDbo;
    }
    
    public void setCcDbo(BigDecimal ccDbo) {
        this.ccDbo = ccDbo;
    }
    
    public BigDecimal getCcSst() {
        return ccSst;
    }
    
    public void setCcSst(BigDecimal ccSst) {
        this.ccSst = ccSst;
    }
    
    public BigDecimal getMinimumTariffDbo() {
        return minimumTariffDbo;
    }
    
    public void setMinimumTariffDbo(BigDecimal minimumTariffDbo) {
        this.minimumTariffDbo = minimumTariffDbo;
    }
    
    public BigDecimal getMinimumTariffSst() {
        return minimumTariffSst;
    }
    
    public void setMinimumTariffSst(BigDecimal minimumTariffSst) {
        this.minimumTariffSst = minimumTariffSst;
    }
    
    public BigDecimal getAmountToPayDbo() {
        return amountToPayDbo;
    }
    
    public void setAmountToPayDbo(BigDecimal amountToPayDbo) {
        this.amountToPayDbo = amountToPayDbo;
    }
    
    public BigDecimal getAmountToPaySst() {
        return amountToPaySst;
    }
    
    public void setAmountToPaySst(BigDecimal amountToPaySst) {
        this.amountToPaySst = amountToPaySst;
    }
    
    public BigDecimal getTotalAmountToPay() {
        return totalAmountToPay;
    }
    
    public void setTotalAmountToPay(BigDecimal totalAmountToPay) {
        this.totalAmountToPay = totalAmountToPay;
    }
    
    public Integer getNumberIcaVariables() {
        return numberIcaVariables;
    }
    
    public void setNumberIcaVariables(Integer numberIcaVariables) {
        this.numberIcaVariables = numberIcaVariables;
    }
    
    public BigDecimal getIcaCoefficient() {
        return icaCoefficient;
    }
    
    public void setIcaCoefficient(BigDecimal icaCoefficient) {
        this.icaCoefficient = icaCoefficient;
    }
    
    public BigDecimal getrCoefficient() {
        return rCoefficient;
    }
    
    public void setrCoefficient(BigDecimal rCoefficient) {
        this.rCoefficient = rCoefficient;
    }
    
    public BigDecimal getbCoefficient() {
        return bCoefficient;
    }
    
    public void setbCoefficient(BigDecimal bCoefficient) {
        this.bCoefficient = bCoefficient;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
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
        return "Invoice{" +
                "id=" + id +
                ", discharge=" + (discharge != null ? discharge.getId() : null) +
                ", number=" + number +
                ", year=" + year +
                ", environmentalVariable=" + environmentalVariable +
                ", socioeconomicVariable=" + socioeconomicVariable +
                ", economicVariable=" + economicVariable +
                ", regionalFactor=" + regionalFactor +
                ", ccDbo=" + ccDbo +
                ", ccSst=" + ccSst +
                ", minimumTariffDbo=" + minimumTariffDbo +
                ", minimumTariffSst=" + minimumTariffSst +
                ", amountToPayDbo=" + amountToPayDbo +
                ", amountToPaySst=" + amountToPaySst +
                ", totalAmountToPay=" + totalAmountToPay +
                ", numberIcaVariables=" + numberIcaVariables +
                ", icaCoefficient=" + icaCoefficient +
                ", rCoefficient=" + rCoefficient +
                ", bCoefficient=" + bCoefficient +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}

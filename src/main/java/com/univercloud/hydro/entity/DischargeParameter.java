package com.univercloud.hydro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "discharge_parameters")
public class DischargeParameter {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Discharge is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discharge_id", nullable = false)
    private Discharge discharge;
    
    @NotNull(message = "Month is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "month", nullable = false)
    private Month month;
    
    @NotNull(message = "Origin is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "origin", nullable = false)
    private Origin origin;
    
    @NotNull(message = "Caudal volumen is required")
    @Column(name = "caudal_volumen", precision = 12, scale = 2, nullable = false)
    private BigDecimal caudalVolumen;
    
    @NotNull(message = "Frequency is required")
    @Column(name = "frequency", nullable = false)
    private Short frequency;
    
    @NotNull(message = "Duration is required")
    @Column(name = "duration", nullable = false)
    private Short duration;
    
    @NotNull(message = "Conc DBO is required")
    @Column(name = "conc_dbo", precision = 9, scale = 2, nullable = false)
    private BigDecimal concDbo;
    
    @NotNull(message = "Conc SST is required")
    @Column(name = "conc_sst", precision = 9, scale = 2, nullable = false)
    private BigDecimal concSst;
    
    @NotNull(message = "CC DBO is required")
    @Column(name = "cc_dbo", precision = 9, scale = 2, nullable = false)
    private BigDecimal ccDbo;
    
    @NotNull(message = "CC SST is required")
    @Column(name = "cc_sst", precision = 9, scale = 2, nullable = false)
    private BigDecimal ccSst;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public DischargeParameter() {
        this.createdAt = LocalDateTime.now();
    }
    
    public DischargeParameter(Discharge discharge, Month month, Origin origin, BigDecimal caudalVolumen,
                             Short frequency, Short duration, BigDecimal concDbo, BigDecimal concSst,
                             BigDecimal ccDbo, BigDecimal ccSst) {
        this();
        this.discharge = discharge;
        this.month = month;
        this.origin = origin;
        this.caudalVolumen = caudalVolumen;
        this.frequency = frequency;
        this.duration = duration;
        this.concDbo = concDbo;
        this.concSst = concSst;
        this.ccDbo = ccDbo;
        this.ccSst = ccSst;
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
    
    public Month getMonth() {
        return month;
    }
    
    public void setMonth(Month month) {
        this.month = month;
    }
    
    public Origin getOrigin() {
        return origin;
    }
    
    public void setOrigin(Origin origin) {
        this.origin = origin;
    }
    
    public BigDecimal getCaudalVolumen() {
        return caudalVolumen;
    }
    
    public void setCaudalVolumen(BigDecimal caudalVolumen) {
        this.caudalVolumen = caudalVolumen;
    }
    
    public Short getFrequency() {
        return frequency;
    }
    
    public void setFrequency(Short frequency) {
        this.frequency = frequency;
    }
    
    public Short getDuration() {
        return duration;
    }
    
    public void setDuration(Short duration) {
        this.duration = duration;
    }
    
    public BigDecimal getConcDbo() {
        return concDbo;
    }
    
    public void setConcDbo(BigDecimal concDbo) {
        this.concDbo = concDbo;
    }
    
    public BigDecimal getConcSst() {
        return concSst;
    }
    
    public void setConcSst(BigDecimal concSst) {
        this.concSst = concSst;
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
        return "DischargeParameter{" +
                "id=" + id +
                ", discharge=" + (discharge != null ? discharge.getId() : null) +
                ", month=" + month +
                ", origin=" + origin +
                ", caudalVolumen=" + caudalVolumen +
                ", frequency=" + frequency +
                ", duration=" + duration +
                ", concDbo=" + concDbo +
                ", concSst=" + concSst +
                ", ccDbo=" + ccDbo +
                ", ccSst=" + ccSst +
                ", createdAt=" + createdAt +
                '}';
    }
    
    // Enum for months
    public enum Month {
        ENERO("Enero"),
        FEBRERO("Febrero"),
        MARZO("Marzo"),
        ABRIL("Abril"),
        MAYO("Mayo"),
        JUNIO("Junio"),
        JULIO("Julio"),
        AGOSTO("Agosto"),
        SEPTIEMBRE("Septiembre"),
        OCTUBRE("Octubre"),
        NOVIEMBRE("Noviembre"),
        DICIEMBRE("Diciembre");
        
        private final String displayName;
        
        Month(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    // Enum for origin
    public enum Origin {
        VERTIMIENTO("Vertimiento"),
        CAPTACION("Captación");
        
        private final String displayName;
        
        Origin(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}

package com.univercloud.hydro.dto;

import com.univercloud.hydro.entity.Discharge;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO para crear una nueva descarga.
 */
public class CreateDischargeRequest {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 255, message = "El nombre no puede exceder 255 caracteres")
    private String name;
    
    @NotNull(message = "El número es obligatorio")
    @Min(value = 1, message = "El número debe ser mayor a 0")
    private Integer number;
    
    @NotNull(message = "El año es obligatorio")
    @Min(value = 2000, message = "El año debe ser mayor o igual a 2000")
    @Max(value = 2100, message = "El año debe ser menor o igual a 2100")
    private Integer year;
    
    @NotNull(message = "El tipo de descarga es obligatorio")
    private Discharge.DischargeType dischargeType;
    
    @NotNull(message = "El tipo de recurso hídrico es obligatorio")
    private Discharge.WaterResourceType waterResourceType;
    
    @NotNull(message = "El ID del municipio es obligatorio")
    private Long municipalityId;
    
    @NotNull(message = "El ID de la sección de cuenca es obligatorio")
    private Long basinSectionId;
    
    @NotNull(message = "El ID del usuario de descarga es obligatorio")
    private Long dischargeUserId;
    
    @NotNull(message = "El CC DBO Total es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El CC DBO Total debe ser mayor a 0")
    @Digits(integer = 9, fraction = 2, message = "El CC DBO Total debe tener máximo 9 dígitos enteros y 2 decimales")
    private BigDecimal ccDboTotal;
    
    @NotNull(message = "El CC SST Total es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El CC SST Total debe ser mayor a 0")
    @Digits(integer = 9, fraction = 2, message = "El CC SST Total debe tener máximo 9 dígitos enteros y 2 decimales")
    private BigDecimal ccSstTotal;
    
    @NotNull(message = "El indicador de fuente monitoreada es obligatorio")
    private Boolean isSourceMonitored;
    
    @NotNull(message = "El indicador de reúso de cuenca es obligatorio")
    private Boolean isBasinReuse;
    
    @Size(max = 255, message = "El punto de descarga no puede exceder 255 caracteres")
    private String dischargePoint;
    
    // Constructors
    public CreateDischargeRequest() {}
    
    // Getters and Setters
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
    
    public Long getBasinSectionId() { return basinSectionId; }
    public void setBasinSectionId(Long basinSectionId) { this.basinSectionId = basinSectionId; }
    
    public Long getDischargeUserId() { return dischargeUserId; }
    public void setDischargeUserId(Long dischargeUserId) { this.dischargeUserId = dischargeUserId; }
    
    public BigDecimal getCcDboTotal() { return ccDboTotal; }
    public void setCcDboTotal(BigDecimal ccDboTotal) { this.ccDboTotal = ccDboTotal; }
    
    public BigDecimal getCcSstTotal() { return ccSstTotal; }
    public void setCcSstTotal(BigDecimal ccSstTotal) { this.ccSstTotal = ccSstTotal; }
    
    public Boolean getIsSourceMonitored() { return isSourceMonitored; }
    public void setIsSourceMonitored(Boolean isSourceMonitored) { this.isSourceMonitored = isSourceMonitored; }
    
    public Boolean getIsBasinReuse() { return isBasinReuse; }
    public void setIsBasinReuse(Boolean isBasinReuse) { this.isBasinReuse = isBasinReuse; }
    
    public String getDischargePoint() { return dischargePoint; }
    public void setDischargePoint(String dischargePoint) { this.dischargePoint = dischargePoint; }
}

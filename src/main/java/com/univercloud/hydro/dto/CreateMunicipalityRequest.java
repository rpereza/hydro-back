package com.univercloud.hydro.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO para crear un nuevo municipio.
 */
public class CreateMunicipalityRequest {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    private String name;
    
    @NotBlank(message = "El código es obligatorio")
    @Size(max = 3, message = "El código no puede exceder 3 caracteres")
    private String code;
    
    @NotNull(message = "El ID del departamento es obligatorio")
    private Long departmentId;
    
    @NotNull(message = "El ID de la categoría es obligatorio")
    private Long categoryId;
    
    @DecimalMin(value = "0.0", message = "El NBI debe ser mayor o igual a 0")
    @DecimalMax(value = "100.0", message = "El NBI debe ser menor o igual a 100")
    @Digits(integer = 3, fraction = 2, message = "El NBI debe tener máximo 3 dígitos enteros y 2 decimales")
    private BigDecimal nbi;
    
    // Constructors
    public CreateMunicipalityRequest() {}
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    
    public BigDecimal getNbi() { return nbi; }
    public void setNbi(BigDecimal nbi) { this.nbi = nbi; }
}

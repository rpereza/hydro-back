package com.univercloud.hydro.dto;

import com.univercloud.hydro.entity.Municipality;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de respuesta para municipios.
 */
public class MunicipalityResponse {
    
    private Long id;
    private String name;
    private String code;
    private Long departmentId;
    private String departmentName;
    private Long categoryId;
    private String categoryName;
    private BigDecimal nbi;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public MunicipalityResponse() {}
    
    public MunicipalityResponse(Municipality municipality) {
        this.id = municipality.getId();
        this.name = municipality.getName();
        this.code = municipality.getCode();
        this.departmentId = municipality.getDepartment() != null ? municipality.getDepartment().getId() : null;
        this.departmentName = municipality.getDepartment() != null ? municipality.getDepartment().getName() : null;
        this.categoryId = municipality.getCategory() != null ? municipality.getCategory().getId() : null;
        this.categoryName = municipality.getCategory() != null ? municipality.getCategory().getName() : null;
        this.nbi = municipality.getNbi();
        this.createdAt = municipality.getCreatedAt();
        this.updatedAt = municipality.getUpdatedAt();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    
    public BigDecimal getNbi() { return nbi; }
    public void setNbi(BigDecimal nbi) { this.nbi = nbi; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

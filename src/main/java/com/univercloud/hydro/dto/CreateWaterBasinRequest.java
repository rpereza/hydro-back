package com.univercloud.hydro.dto;

import jakarta.validation.constraints.*;

/**
 * DTO para crear una nueva cuenca hidrográfica.
 */
public class CreateWaterBasinRequest {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150, message = "El nombre no puede exceder 150 caracteres")
    private String name;
    
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String description;
    
    @NotNull(message = "El indicador de estado activo es obligatorio")
    private Boolean isActive;
    
    // Constructors
    public CreateWaterBasinRequest() {}
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}

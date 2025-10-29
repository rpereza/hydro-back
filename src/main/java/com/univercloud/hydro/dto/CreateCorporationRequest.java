package com.univercloud.hydro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para la creación de una nueva Corporación.
 */
public class CreateCorporationRequest {
    
    @NotBlank(message = "Corporation name is required")
    @Size(max = 200, message = "Corporation name cannot exceed 200 characters")
    private String name;
    
    @NotBlank(message = "Corporation code is required")
    @Size(max = 50, message = "Corporation code cannot exceed 50 characters")
    private String code;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
    
    // Constructors
    public CreateCorporationRequest() {}
    
    public CreateCorporationRequest(String name, String code) {
        this.name = name;
        this.code = code;
    }
    
    public CreateCorporationRequest(String name, String code, String description) {
        this.name = name;
        this.code = code;
        this.description = description;
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return "CreateCorporationRequest{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

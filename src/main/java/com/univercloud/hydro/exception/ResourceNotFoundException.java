package com.univercloud.hydro.exception;

/**
 * Excepción lanzada cuando un recurso solicitado no se encuentra.
 * Esta excepción debe ser capturada por el GlobalExceptionHandler
 * y mapeada a un código de estado HTTP 404 (Not Found).
 */
public class ResourceNotFoundException extends RuntimeException {
    
    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;
    
    /**
     * Constructor con información detallada del recurso no encontrado.
     * 
     * @param resourceName el nombre del recurso (ej: "MinimumTariff", "WaterBasin")
     * @param fieldName el nombre del campo usado para buscar (ej: "id", "year")
     * @param fieldValue el valor del campo usado para buscar
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
    
    /**
     * Constructor con mensaje simple.
     * 
     * @param message el mensaje de error
     */
    public ResourceNotFoundException(String message) {
        super(message);
        this.resourceName = null;
        this.fieldName = null;
        this.fieldValue = null;
    }
    
    public String getResourceName() {
        return resourceName;
    }
    
    public String getFieldName() {
        return fieldName;
    }
    
    public Object getFieldValue() {
        return fieldValue;
    }
}


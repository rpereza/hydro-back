package com.univercloud.hydro.exception;

/**
 * Excepción lanzada cuando se intenta crear o actualizar un recurso
 * que viola una restricción de unicidad.
 * Esta excepción debe ser capturada por el GlobalExceptionHandler
 * y mapeada a un código de estado HTTP 409 (Conflict).
 */
public class DuplicateResourceException extends RuntimeException {
    
    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;
    
    /**
     * Constructor con información detallada del recurso duplicado.
     * 
     * @param resourceName el nombre del recurso (ej: "BasinSection", "DischargeUser")
     * @param fieldName el nombre del campo que viola la unicidad (ej: "name", "code")
     * @param fieldValue el valor del campo que causa el conflicto
     */
    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s with %s '%s' already exists", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
    
    /**
     * Constructor con mensaje simple.
     * 
     * @param message el mensaje de error
     */
    public DuplicateResourceException(String message) {
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


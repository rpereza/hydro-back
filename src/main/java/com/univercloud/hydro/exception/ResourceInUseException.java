package com.univercloud.hydro.exception;

/**
 * Excepción lanzada cuando se intenta eliminar un recurso que está siendo utilizado por otros recursos.
 * Esta excepción debe ser capturada por el GlobalExceptionHandler
 * y mapeada a un código de estado HTTP 409 (Conflict).
 */
public class ResourceInUseException extends RuntimeException {
    
    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;
    private final String conflictingResourceName;
    private final long conflictingResourceCount;
    
    /**
     * Constructor con información detallada del recurso en uso.
     * 
     * @param resourceName el nombre del recurso que se intenta eliminar (ej: "BasinSection", "WaterBasin")
     * @param fieldName el nombre del campo identificador (ej: "id", "name")
     * @param fieldValue el valor del campo identificador
     * @param conflictingResourceName el nombre del recurso que está usando este recurso (ej: "MonitoringStation", "Discharge")
     * @param conflictingResourceCount el número de recursos que están usando este recurso
     */
    public ResourceInUseException(String resourceName, String fieldName, Object fieldValue, 
                                  String conflictingResourceName, long conflictingResourceCount) {
        super(String.format("Cannot delete %s with %s '%s' because it is being used by %d %s(s)", 
              resourceName, fieldName, fieldValue, conflictingResourceCount, conflictingResourceName));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.conflictingResourceName = conflictingResourceName;
        this.conflictingResourceCount = conflictingResourceCount;
    }
    
    /**
     * Constructor con mensaje simple.
     * 
     * @param message el mensaje de error
     */
    public ResourceInUseException(String message) {
        super(message);
        this.resourceName = null;
        this.fieldName = null;
        this.fieldValue = null;
        this.conflictingResourceName = null;
        this.conflictingResourceCount = 0;
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
    
    public String getConflictingResourceName() {
        return conflictingResourceName;
    }
    
    public long getConflictingResourceCount() {
        return conflictingResourceCount;
    }
}


package com.univercloud.hydro.entity;

/**
 * Interface que define el contrato para entidades que requieren control de acceso
 * basado en Corporación y auditoría de creación/modificación.
 */
public interface Auditable {
    
    /**
     * Obtiene la Corporación a la que pertenece esta entidad.
     * @return la Corporación
     */
    Corporation getCorporation();
    
    /**
     * Establece la Corporación a la que pertenece esta entidad.
     * @param corporation la Corporación
     */
    void setCorporation(Corporation corporation);
    
    /**
     * Obtiene el Usuario que creó esta entidad.
     * @return el Usuario creador
     */
    User getCreatedBy();
    
    /**
     * Establece el Usuario que creó esta entidad.
     * @param user el Usuario creador
     */
    void setCreatedBy(User user);
    
    /**
     * Obtiene el Usuario que modificó por última vez esta entidad.
     * @return el Usuario que modificó
     */
    User getUpdatedBy();
    
    /**
     * Establece el Usuario que modificó por última vez esta entidad.
     * @param user el Usuario que modificó
     */
    void setUpdatedBy(User user);
}

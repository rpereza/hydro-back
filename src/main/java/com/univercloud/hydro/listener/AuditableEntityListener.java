package com.univercloud.hydro.listener;

import com.univercloud.hydro.entity.Auditable;
import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.User;
import com.univercloud.hydro.util.AuthorizationUtils;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Entity Listener que automáticamente asigna la Corporación y campos de auditoría
 * a las entidades que implementan la interface Auditable.
 */
@Component
public class AuditableEntityListener {
    
    @Autowired
    private AuthorizationUtils authorizationUtils;
    
    /**
     * Se ejecuta antes de persistir una entidad.
     * Asigna automáticamente la Corporación del usuario autenticado y el campo createdBy.
     * 
     * @param entity la entidad que se está persistiendo
     */
    @PrePersist
    public void prePersist(Object entity) {
        if (entity instanceof Auditable) {
            User currentUser = authorizationUtils.getCurrentUser();
            if (currentUser != null) {
                Auditable auditable = (Auditable) entity;
                
                // Asignar Corporación si no está asignada
                if (auditable.getCorporation() == null) {
                    Corporation userCorporation = currentUser.getCorporation();
                    if (userCorporation != null) {
                        auditable.setCorporation(userCorporation);
                    }
                }
                
                // Asignar createdBy si no está asignado
                if (auditable.getCreatedBy() == null) {
                    auditable.setCreatedBy(currentUser);
                }
            }
        }
    }
    
    /**
     * Se ejecuta antes de actualizar una entidad.
     * Asigna automáticamente el campo updatedBy con el usuario autenticado.
     * 
     * @param entity la entidad que se está actualizando
     */
    @PreUpdate
    public void preUpdate(Object entity) {
        if (entity instanceof Auditable) {
            User currentUser = authorizationUtils.getCurrentUser();
            if (currentUser != null) {
                Auditable auditable = (Auditable) entity;
                auditable.setUpdatedBy(currentUser);
            }
        }
    }
}

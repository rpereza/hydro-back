package com.univercloud.hydro.util;

import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Utilidad para manejar autorización y obtener información del usuario autenticado.
 */
@Component
public class AuthorizationUtils {
    
    /**
     * Obtiene el usuario autenticado actual.
     * @return el usuario autenticado o null si no hay autenticación
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() || 
            "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }
        
        return (User) authentication.getPrincipal();
    }
    
    /**
     * Obtiene la Corporación del usuario autenticado actual.
     * @return la Corporación del usuario o null si no está autenticado o no tiene corporación
     */
    public Corporation getCurrentUserCorporation() {
        User currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getCorporation() : null;
    }
    
    /**
     * Verifica si el usuario autenticado tiene acceso completo (es ADMIN o SUPER_ADMIN).
     * @return true si tiene acceso completo, false en caso contrario
     */
    public boolean hasFullAccess() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        
        return currentUser.getRoles().stream()
                .anyMatch(role -> "ADMIN".equals(role.getName()) || "SUPER_ADMIN".equals(role.getName()));
    }
    
    /**
     * Verifica si el usuario autenticado es SUPER_ADMIN.
     * @return true si es SUPER_ADMIN, false en caso contrario
     */
    public boolean isSuperAdmin() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        
        return currentUser.getRoles().stream()
                .anyMatch(role -> "SUPER_ADMIN".equals(role.getName()));
    }
    
    /**
     * Verifica si el usuario autenticado es ADMIN.
     * @return true si es ADMIN, false en caso contrario
     */
    public boolean isAdmin() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        
        return currentUser.getRoles().stream()
                .anyMatch(role -> "ADMIN".equals(role.getName()));
    }
    
    /**
     * Verifica si el usuario autenticado pertenece a una Corporación específica.
     * @param corporation la Corporación a verificar
     * @return true si pertenece a la Corporación, false en caso contrario
     */
    public boolean belongsToCorporation(Corporation corporation) {
        if (corporation == null) {
            return false;
        }
        
        Corporation currentUserCorporation = getCurrentUserCorporation();
        return currentUserCorporation != null && 
               currentUserCorporation.getId().equals(corporation.getId());
    }
    
    /**
     * Verifica si el usuario autenticado es el propietario de su Corporación.
     * @return true si es el propietario, false en caso contrario
     */
    public boolean isCorporationOwner() {
        User currentUser = getCurrentUser();
        Corporation currentUserCorporation = getCurrentUserCorporation();
        
        if (currentUser == null || currentUserCorporation == null) {
            return false;
        }
        
        return currentUserCorporation.isOwner(currentUser);
    }
    
    /**
     * Verifica si el usuario autenticado puede acceder a una entidad específica.
     * Un usuario puede acceder si:
     * - Es SUPER_ADMIN (acceso total)
     * - Es ADMIN y la entidad pertenece a su Corporación
     * - Es usuario normal y la entidad pertenece a su Corporación
     * 
     * @param entityCorporation la Corporación de la entidad
     * @return true si puede acceder, false en caso contrario
     */
    public boolean canAccessEntity(Corporation entityCorporation) {
        if (entityCorporation == null) {
            return false;
        }
        
        // SUPER_ADMIN tiene acceso total
        if (isSuperAdmin()) {
            return true;
        }
        
        // ADMIN y usuarios normales solo pueden acceder a su propia Corporación
        return belongsToCorporation(entityCorporation);
    }
    
    /**
     * Verifica si el usuario autenticado puede crear una Corporación.
     * Un usuario puede crear una Corporación si:
     * - No tiene una Corporación asignada
     * - No ha creado una Corporación previamente
     * 
     * @return true si puede crear una Corporación, false en caso contrario
     */
    public boolean canCreateCorporation() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return false;
        }
        
        return currentUser.getCorporation() == null && !currentUser.isHasCreatedCorporation();
    }
    
    /**
     * Verifica si el usuario autenticado puede invitar usuarios a su Corporación.
     * Un usuario puede invitar si:
     * - Es el propietario de su Corporación
     * - Es ADMIN de su Corporación
     * 
     * @return true si puede invitar, false en caso contrario
     */
    public boolean canInviteUsers() {
        return isCorporationOwner() || isAdmin();
    }
    
    /**
     * Obtiene el usuario autenticado como Optional.
     * @return Optional con el usuario autenticado
     */
    public Optional<User> getCurrentUserOptional() {
        return Optional.ofNullable(getCurrentUser());
    }
    
    /**
     * Obtiene la Corporación del usuario autenticado como Optional.
     * @return Optional con la Corporación del usuario
     */
    public Optional<Corporation> getCurrentUserCorporationOptional() {
        return Optional.ofNullable(getCurrentUserCorporation());
    }
}

package com.univercloud.hydro.service;

import com.univercloud.hydro.entity.AuthorizationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de Tipos de Autorización.
 * Proporciona operaciones CRUD y lógica de negocio para tipos de autorización.
 */
public interface AuthorizationTypeService {
    
    /**
     * Crea un nuevo tipo de autorización.
     * @param authorizationType el tipo de autorización a crear
     * @return el tipo de autorización creado
     * @throws IllegalArgumentException si los datos son inválidos
     */
    AuthorizationType createAuthorizationType(AuthorizationType authorizationType);
    
    /**
     * Actualiza un tipo de autorización existente.
     * @param authorizationType el tipo de autorización a actualizar
     * @return el tipo de autorización actualizado
     * @throws IllegalArgumentException si el tipo de autorización no existe
     */
    AuthorizationType updateAuthorizationType(AuthorizationType authorizationType);
    
    /**
     * Obtiene un tipo de autorización por su ID.
     * @param id el ID del tipo de autorización
     * @return el tipo de autorización, si existe
     */
    Optional<AuthorizationType> getAuthorizationTypeById(Long id);
    
    /**
     * Obtiene todos los tipos de autorización de la corporación del usuario autenticado.
     * @param pageable parámetros de paginación
     * @return página de tipos de autorización
     */
    Page<AuthorizationType> getMyCorporationAuthorizationTypes(Pageable pageable);
    
    /**
     * Obtiene todos los tipos de autorización de la corporación del usuario autenticado.
     * @return lista de tipos de autorización
     */
    List<AuthorizationType> getAllMyCorporationAuthorizationTypes();
    
    /**
     * Obtiene todos los tipos de autorización activos de la corporación del usuario autenticado.
     * @return lista de tipos de autorización activos
     */
    List<AuthorizationType> getActiveMyCorporationAuthorizationTypes();
    
    /**
     * Obtiene todos los tipos de autorización activos.
     * @return lista de tipos de autorización activos
     */
    List<AuthorizationType> getAllActiveAuthorizationTypes();
    
    /**
     * Obtiene todos los tipos de autorización inactivos.
     * @return lista de tipos de autorización inactivos
     */
    List<AuthorizationType> getAllInactiveAuthorizationTypes();
    
    /**
     * Busca un tipo de autorización por nombre.
     * @param name el nombre del tipo de autorización
     * @return el tipo de autorización si existe
     */
    Optional<AuthorizationType> getAuthorizationTypeByName(String name);
    
    /**
     * Busca tipos de autorización por nombre (búsqueda parcial).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de tipos de autorización que coinciden
     */
    List<AuthorizationType> searchAuthorizationTypesByName(String name);
    
    /**
     * Busca tipos de autorización activos por nombre (búsqueda parcial).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de tipos de autorización activos que coinciden
     */
    List<AuthorizationType> searchActiveAuthorizationTypesByName(String name);
    
    /**
     * Obtiene tipos de autorización creados en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de tipos de autorización creados en el rango
     */
    List<AuthorizationType> getAuthorizationTypesByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Cuenta el número de tipos de autorización de la corporación del usuario autenticado.
     * @return número de tipos de autorización
     */
    long countMyCorporationAuthorizationTypes();
    
    /**
     * Cuenta tipos de autorización activos de la corporación del usuario autenticado.
     * @return número de tipos de autorización activos
     */
    long countActiveMyCorporationAuthorizationTypes();
    
    /**
     * Activa un tipo de autorización.
     * @param id el ID del tipo de autorización a activar
     * @return true si se activó correctamente
     * @throws IllegalArgumentException si el tipo de autorización no existe
     */
    boolean activateAuthorizationType(Long id);
    
    /**
     * Desactiva un tipo de autorización.
     * @param id el ID del tipo de autorización a desactivar
     * @return true si se desactivó correctamente
     * @throws IllegalArgumentException si el tipo de autorización no existe
     */
    boolean deactivateAuthorizationType(Long id);
    
    /**
     * Elimina un tipo de autorización.
     * @param id el ID del tipo de autorización a eliminar
     * @return true si se eliminó correctamente
     * @throws IllegalArgumentException si el tipo de autorización no existe
     */
    boolean deleteAuthorizationType(Long id);
    
    /**
     * Verifica si existe un tipo de autorización con el nombre especificado.
     * @param name el nombre del tipo de autorización
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);
    
    /**
     * Obtiene tipos de autorización ordenados por fecha de creación (más recientes primero).
     * @return lista de tipos de autorización ordenados por fecha de creación descendente
     */
    List<AuthorizationType> getAuthorizationTypesOrderByCreatedAtDesc();
    
    /**
     * Obtiene tipos de autorización activos ordenados por nombre.
     * @return lista de tipos de autorización activos ordenados por nombre
     */
    List<AuthorizationType> getActiveAuthorizationTypesOrderByName();
    
    /**
     * Obtiene tipos de autorización de la corporación ordenados por nombre.
     * @return lista de tipos de autorización de la corporación ordenados por nombre
     */
    List<AuthorizationType> getMyCorporationAuthorizationTypesOrderByName();
    
    /**
     * Obtiene tipos de autorización activos de la corporación ordenados por nombre.
     * @return lista de tipos de autorización activos de la corporación ordenados por nombre
     */
    List<AuthorizationType> getActiveMyCorporationAuthorizationTypesOrderByName();
    
    /**
     * Obtiene estadísticas de tipos de autorización de la corporación del usuario autenticado.
     * @return estadísticas de tipos de autorización
     */
    AuthorizationTypeStats getMyCorporationAuthorizationTypeStats();
    
    /**
     * Clase interna para estadísticas de tipos de autorización
     */
    class AuthorizationTypeStats {
        private long totalAuthorizationTypes;
        private long activeAuthorizationTypes;
        private long inactiveAuthorizationTypes;
        private long totalUsers;
        
        // Constructors
        public AuthorizationTypeStats() {}
        
        // Getters and Setters
        public long getTotalAuthorizationTypes() { return totalAuthorizationTypes; }
        public void setTotalAuthorizationTypes(long totalAuthorizationTypes) { this.totalAuthorizationTypes = totalAuthorizationTypes; }
        
        public long getActiveAuthorizationTypes() { return activeAuthorizationTypes; }
        public void setActiveAuthorizationTypes(long activeAuthorizationTypes) { this.activeAuthorizationTypes = activeAuthorizationTypes; }
        
        public long getInactiveAuthorizationTypes() { return inactiveAuthorizationTypes; }
        public void setInactiveAuthorizationTypes(long inactiveAuthorizationTypes) { this.inactiveAuthorizationTypes = inactiveAuthorizationTypes; }
        
        public long getTotalUsers() { return totalUsers; }
        public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
    }
}

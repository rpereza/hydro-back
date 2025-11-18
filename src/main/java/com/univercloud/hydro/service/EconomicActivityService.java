package com.univercloud.hydro.service;

import com.univercloud.hydro.entity.EconomicActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de Actividades Económicas.
 * Proporciona operaciones CRUD y lógica de negocio para actividades económicas.
 */
public interface EconomicActivityService {
    
    /**
     * Crea una nueva actividad económica.
     * @param economicActivity la actividad económica a crear
     * @return la actividad económica creada
     * @throws IllegalArgumentException si los datos son inválidos
     */
    EconomicActivity createEconomicActivity(EconomicActivity economicActivity);
    
    /**
     * Actualiza una actividad económica existente.
     * @param economicActivity la actividad económica a actualizar
     * @return la actividad económica actualizada
     * @throws IllegalArgumentException si la actividad económica no existe
     */
    EconomicActivity updateEconomicActivity(EconomicActivity economicActivity);
    
    /**
     * Obtiene una actividad económica por su ID.
     * @param id el ID de la actividad económica
     * @return la actividad económica, si existe
     */
    Optional<EconomicActivity> getEconomicActivityById(Long id);
    
    /**
     * Obtiene todas las actividades económicas.
     * @param pageable parámetros de paginación
     * @return página de actividades económicas
     */
    Page<EconomicActivity> getAllEconomicActivities(Pageable pageable);
    
    /**
     * Obtiene todas las actividades económicas.
     * @return lista de actividades económicas
     */
    List<EconomicActivity> getAllEconomicActivities();
    
    /**
     * Obtiene todas las actividades económicas activas.
     * @return lista de actividades económicas activas
     */
    List<EconomicActivity> getAllActiveEconomicActivities();
    
    /**
     * Obtiene todas las actividades económicas inactivas.
     * @return lista de actividades económicas inactivas
     */
    List<EconomicActivity> getAllInactiveEconomicActivities();
    
    /**
     * Busca una actividad económica por nombre.
     * @param name el nombre de la actividad económica
     * @return la actividad económica si existe
     */
    Optional<EconomicActivity> getEconomicActivityByName(String name);
    
    /**
     * Busca una actividad económica por código.
     * @param code el código de la actividad económica
     * @return la actividad económica si existe
     */
    Optional<EconomicActivity> getEconomicActivityByCode(String code);
    
    /**
     * Busca actividades económicas por nombre (búsqueda parcial).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de actividades económicas que coinciden
     */
    List<EconomicActivity> searchEconomicActivitiesByName(String name);
    
    /**
     * Busca actividades económicas por nombre (búsqueda parcial) con paginación.
     * @param name el nombre o parte del nombre a buscar
     * @param pageable parámetros de paginación
     * @return página de actividades económicas que coinciden
     */
    Page<EconomicActivity> searchEconomicActivitiesByName(String name, Pageable pageable);
    
    /**
     * Busca actividades económicas por código (búsqueda parcial).
     * @param code el código o parte del código a buscar
     * @return lista de actividades económicas que coinciden
     */
    List<EconomicActivity> searchEconomicActivitiesByCode(String code);
    
    /**
     * Busca actividades económicas cuyo código comience con el código dado.
     * @param code el código o parte inicial del código a buscar
     * @param pageable parámetros de paginación
     * @return página de actividades económicas cuyo código comienza con el código dado
     */
    Page<EconomicActivity> searchEconomicActivitiesByCode(String code, Pageable pageable);
    
    /**
     * Busca actividades económicas activas por nombre (búsqueda parcial).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de actividades económicas activas que coinciden
     */
    List<EconomicActivity> searchActiveEconomicActivitiesByName(String name);
    
    /**
     * Busca actividades económicas activas por código (búsqueda parcial).
     * @param code el código o parte del código a buscar
     * @return lista de actividades económicas activas que coinciden
     */
    List<EconomicActivity> searchActiveEconomicActivitiesByCode(String code);
    
    /**
     * Obtiene actividades económicas creadas en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de actividades económicas creadas en el rango
     */
    List<EconomicActivity> getEconomicActivitiesByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Obtiene actividades económicas creadas en un rango de fechas con paginación.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @param pageable parámetros de paginación
     * @return página de actividades económicas creadas en el rango
     */
    Page<EconomicActivity> getEconomicActivitiesByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    /**
     * Cuenta el número total de actividades económicas.
     * @return número de actividades económicas
     */
    long countAllEconomicActivities();
    
    /**
     * Cuenta actividades económicas activas.
     * @return número de actividades económicas activas
     */
    long countActiveEconomicActivities();
    
    /**
     * Activa una actividad económica.
     * @param id el ID de la actividad económica a activar
     * @return true si se activó correctamente
     * @throws IllegalArgumentException si la actividad económica no existe
     */
    boolean activateEconomicActivity(Long id);
    
    /**
     * Desactiva una actividad económica.
     * @param id el ID de la actividad económica a desactivar
     * @return true si se desactivó correctamente
     * @throws IllegalArgumentException si la actividad económica no existe
     */
    boolean deactivateEconomicActivity(Long id);
    
    /**
     * Elimina una actividad económica.
     * @param id el ID de la actividad económica a eliminar
     * @return true si se eliminó correctamente
     * @throws IllegalArgumentException si la actividad económica no existe
     */
    boolean deleteEconomicActivity(Long id);
    
    /**
     * Verifica si existe una actividad económica con el nombre especificado.
     * @param name el nombre de la actividad económica
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);
    
    /**
     * Verifica si existe una actividad económica con el código especificado.
     * @param code el código de la actividad económica
     * @return true si existe, false en caso contrario
     */
    boolean existsByCode(String code);
    
    /**
     * Obtiene actividades económicas ordenadas por fecha de creación (más recientes primero).
     * @return lista de actividades económicas ordenadas por fecha de creación descendente
     */
    List<EconomicActivity> getEconomicActivitiesOrderByCreatedAtDesc();
    
    /**
     * Obtiene actividades económicas ordenadas por fecha de creación (más recientes primero) con paginación.
     * @param pageable parámetros de paginación
     * @return página de actividades económicas ordenadas por fecha de creación descendente
     */
    Page<EconomicActivity> getEconomicActivitiesOrderByCreatedAtDesc(Pageable pageable);
    
    /**
     * Obtiene actividades económicas activas ordenadas por nombre.
     * @return lista de actividades económicas activas ordenadas por nombre
     */
    List<EconomicActivity> getActiveEconomicActivitiesOrderByName();
    
    /**
     * Obtiene actividades económicas ordenadas por código.
     * @return lista de actividades económicas ordenadas por código
     */
    List<EconomicActivity> getEconomicActivitiesOrderByCode();
    
    /**
     * Obtiene actividades económicas activas ordenadas por código.
     * @return lista de actividades económicas activas ordenadas por código
     */
    List<EconomicActivity> getActiveEconomicActivitiesOrderByCode();
    
    /**
     * Búsqueda unificada de actividades económicas por código o nombre (búsqueda parcial) con paginación.
     * Busca en ambos campos simultáneamente.
     * @param query el término de búsqueda (código o nombre)
     * @param pageable parámetros de paginación
     * @return página de actividades económicas que coinciden con el código o nombre
     */
    Page<EconomicActivity> searchEconomicActivitiesByCodeOrName(String query, Pageable pageable);

    /**
     * Obtiene estadísticas de actividades económicas.
     * @return estadísticas de actividades económicas
     */
    EconomicActivityStats getEconomicActivityStats();
    
    /**
     * Clase interna para estadísticas de actividades económicas
     */
    class EconomicActivityStats {
        private long totalEconomicActivities;
        private long activeEconomicActivities;
        private long inactiveEconomicActivities;
        private long totalUsers;
        private long activitiesWithCode;
        private long activitiesWithoutCode;
        
        // Constructors
        public EconomicActivityStats() {}
        
        // Getters and Setters
        public long getTotalEconomicActivities() { return totalEconomicActivities; }
        public void setTotalEconomicActivities(long totalEconomicActivities) { this.totalEconomicActivities = totalEconomicActivities; }
        
        public long getActiveEconomicActivities() { return activeEconomicActivities; }
        public void setActiveEconomicActivities(long activeEconomicActivities) { this.activeEconomicActivities = activeEconomicActivities; }
        
        public long getInactiveEconomicActivities() { return inactiveEconomicActivities; }
        public void setInactiveEconomicActivities(long inactiveEconomicActivities) { this.inactiveEconomicActivities = inactiveEconomicActivities; }
        
        public long getTotalUsers() { return totalUsers; }
        public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
        
        public long getActivitiesWithCode() { return activitiesWithCode; }
        public void setActivitiesWithCode(long activitiesWithCode) { this.activitiesWithCode = activitiesWithCode; }
        
        public long getActivitiesWithoutCode() { return activitiesWithoutCode; }
        public void setActivitiesWithoutCode(long activitiesWithoutCode) { this.activitiesWithoutCode = activitiesWithoutCode; }
    }
}

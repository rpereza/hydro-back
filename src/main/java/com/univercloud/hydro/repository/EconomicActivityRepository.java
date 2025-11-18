package com.univercloud.hydro.repository;

import com.univercloud.hydro.entity.EconomicActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository para la entidad EconomicActivity.
 * Proporciona métodos de consulta para gestionar actividades económicas.
 */
@Repository
public interface EconomicActivityRepository extends JpaRepository<EconomicActivity, Long> {
    
   /**
     * Busca actividades activas.
     * @return lista de actividades activas
     */
    List<EconomicActivity> findByIsActiveTrue();
    
    /**
     * Busca actividades inactivas.
     * @return lista de actividades inactivas
     */
    List<EconomicActivity> findByIsActiveFalse();
    
    /**
     * Busca una actividad por nombre.
     * @param name el nombre de la actividad
     * @return la actividad si existe
     */
    Optional<EconomicActivity> findByName(String name);
    
    /**
     * Verifica si existe una actividad con el nombre especificado.
     * @param name el nombre de la actividad
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);
    
    /**
     * Busca actividades por nombre (búsqueda parcial, case-insensitive).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de actividades que coinciden con el nombre
     */
    @Query("SELECT ea FROM EconomicActivity ea WHERE LOWER(ea.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<EconomicActivity> findByNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * Busca actividades por nombre (búsqueda parcial, case-insensitive) con paginación.
     * @param name el nombre o parte del nombre a buscar
     * @param pageable parámetros de paginación
     * @return página de actividades que coinciden con el nombre
     */
    @Query("SELECT ea FROM EconomicActivity ea WHERE LOWER(ea.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<EconomicActivity> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);
    
    /**
     * Busca actividades activas por nombre (búsqueda parcial, case-insensitive).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de actividades activas que coinciden con el nombre
     */
    @Query("SELECT ea FROM EconomicActivity ea WHERE ea.isActive = true AND LOWER(ea.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<EconomicActivity> findActiveByNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * Busca actividades creadas en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de actividades creadas en el rango
     */
    List<EconomicActivity> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Busca actividades creadas en un rango de fechas con paginación.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @param pageable parámetros de paginación
     * @return página de actividades creadas en el rango
     */
    Page<EconomicActivity> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    /**
     * Busca actividades ordenadas por nombre.
     * @return lista de actividades ordenadas por nombre
     */
    @Query("SELECT ea FROM EconomicActivity ea ORDER BY ea.name ASC")
    List<EconomicActivity> findAllOrderByName();
    
    /**
     * Busca actividades activas ordenadas por nombre.
     * @return lista de actividades activas ordenadas por nombre
     */
    @Query("SELECT ea FROM EconomicActivity ea WHERE ea.isActive = true ORDER BY ea.name ASC")
    List<EconomicActivity> findActiveOrderByName();
    
    /**
     * Busca actividades ordenadas por fecha de creación (más recientes primero).
     * @return lista de actividades ordenadas por fecha de creación descendente
     */
    @Query("SELECT ea FROM EconomicActivity ea ORDER BY ea.createdAt DESC")
    List<EconomicActivity> findAllOrderByCreatedAtDesc();
    
    /**
     * Busca actividades ordenadas por fecha de creación (más recientes primero) con paginación.
     * @param pageable parámetros de paginación
     * @return página de actividades ordenadas por fecha de creación descendente
     */
    @Query("SELECT ea FROM EconomicActivity ea ORDER BY ea.createdAt DESC")
    Page<EconomicActivity> findAllOrderByCreatedAtDesc(Pageable pageable);
    
    // Métodos adicionales requeridos por los servicios
    
    /**
     * Busca una actividad por código.
     * @param code el código de la actividad
     * @return la actividad si existe
     */
    Optional<EconomicActivity> findByCode(String code);
    
    /**
     * Verifica si existe una actividad con el código especificado.
     * @param code el código de la actividad
     * @return true si existe, false en caso contrario
     */
    boolean existsByCode(String code);
    
    /**
     * Busca actividades por código (búsqueda parcial, case-insensitive).
     * @param code el código o parte del código a buscar
     * @return lista de actividades que coinciden con el código
     */
    @Query("SELECT ea FROM EconomicActivity ea WHERE LOWER(ea.code) LIKE LOWER(CONCAT('%', :code, '%'))")
    List<EconomicActivity> findByCodeContainingIgnoreCase(@Param("code") String code);
    
    /**
     * Busca actividades por código que comience con el código dado (case-insensitive).
     * @param code el código o parte inicial del código a buscar
     * @return lista de actividades cuyo código comienza con el código dado
     */
    @Query("SELECT ea FROM EconomicActivity ea WHERE LOWER(ea.code) LIKE LOWER(CONCAT(:code, '%'))")
    List<EconomicActivity> findByCodeStartingWithIgnoreCase(@Param("code") String code);
    
    /**
     * Busca actividades por código que comience con el código dado (case-insensitive) con paginación.
     * @param code el código o parte inicial del código a buscar
     * @param pageable parámetros de paginación
     * @return página de actividades cuyo código comienza con el código dado
     */
    @Query("SELECT ea FROM EconomicActivity ea WHERE LOWER(ea.code) LIKE LOWER(CONCAT(:code, '%'))")
    Page<EconomicActivity> findByCodeStartingWithIgnoreCase(@Param("code") String code, Pageable pageable);
    
    /**
     * Busca actividades activas por código (búsqueda parcial, case-insensitive).
     * @param code el código o parte del código a buscar
     * @return lista de actividades activas que coinciden con el código
     */
    @Query("SELECT ea FROM EconomicActivity ea WHERE ea.isActive = true AND LOWER(ea.code) LIKE LOWER(CONCAT('%', :code, '%'))")
    List<EconomicActivity> findByIsActiveTrueAndCodeContainingIgnoreCase(@Param("code") String code);
    
    /**
     * Cuenta actividades activas.
     * @return número de actividades activas
     */
    long countByIsActiveTrue();
    
    /**
     * Busca actividades ordenadas por código.
     * @return lista de actividades ordenadas por código
     */
    @Query("SELECT ea FROM EconomicActivity ea ORDER BY ea.code ASC")
    List<EconomicActivity> findAllOrderByCode();
    
    /**
     * Busca actividades activas ordenadas por código.
     * @return lista de actividades activas ordenadas por código
     */
    @Query("SELECT ea FROM EconomicActivity ea WHERE ea.isActive = true ORDER BY ea.code ASC")
    List<EconomicActivity> findByIsActiveTrueOrderByCode();
    
    /**
     * Búsqueda unificada de actividades económicas por código o nombre (búsqueda parcial, case-insensitive) con paginación.
     * Busca en ambos campos simultáneamente.
     * @param query el término de búsqueda (código o nombre)
     * @param pageable parámetros de paginación
     * @return página de actividades que coinciden con el código o nombre
     */
    @Query("SELECT ea FROM EconomicActivity ea WHERE " +
           "LOWER(ea.code) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(ea.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "ORDER BY ea.code ASC, ea.name ASC")
    Page<EconomicActivity> findByCodeOrNameContainingIgnoreCase(@Param("query") String query, Pageable pageable);
}

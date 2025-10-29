package com.univercloud.hydro.repository;

import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.MonitoringStation;
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
 * Repository para la entidad MonitoringStation.
 * Proporciona métodos de consulta para gestionar estaciones de monitoreo.
 */
@Repository
public interface MonitoringStationRepository extends JpaRepository<MonitoringStation, Long> {
    
    /**
     * Busca estaciones por corporación.
     * @param corporation la corporación
     * @return lista de estaciones de la corporación
     */
    List<MonitoringStation> findByCorporation(Corporation corporation);
    
    /**
     * Busca estaciones por corporación con paginación.
     * @param corporation la corporación
     * @param pageable parámetros de paginación
     * @return página de estaciones de la corporación
     */
    Page<MonitoringStation> findByCorporation(Corporation corporation, Pageable pageable);
    
    /**
     * Busca estaciones activas.
     * @return lista de estaciones activas
     */
    List<MonitoringStation> findByIsActiveTrue();
    
    /**
     * Busca estaciones inactivas.
     * @return lista de estaciones inactivas
     */
    List<MonitoringStation> findByIsActiveFalse();
    
    /**
     * Busca estaciones activas por corporación.
     * @param corporation la corporación
     * @return lista de estaciones activas de la corporación
     */
    List<MonitoringStation> findByCorporationAndIsActiveTrue(Corporation corporation);
    
    /**
     * Busca una estación por nombre.
     * @param name el nombre de la estación
     * @return la estación si existe
     */
    Optional<MonitoringStation> findByName(String name);
    
    /**
     * Verifica si existe una estación con el nombre especificado.
     * @param name el nombre de la estación
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);
    
    /**
     * Busca estaciones por nombre (búsqueda parcial, case-insensitive).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de estaciones que coinciden con el nombre
     */
    @Query("SELECT ms FROM MonitoringStation ms WHERE LOWER(ms.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<MonitoringStation> findByNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * Busca estaciones activas por nombre (búsqueda parcial, case-insensitive).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de estaciones activas que coinciden con el nombre
     */
    @Query("SELECT ms FROM MonitoringStation ms WHERE ms.isActive = true AND LOWER(ms.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<MonitoringStation> findActiveByNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * Busca estaciones por corporación y nombre (búsqueda parcial, case-insensitive).
     * @param corporation la corporación
     * @param name el nombre o parte del nombre a buscar
     * @return lista de estaciones de la corporación que coinciden con el nombre
     */
    @Query("SELECT ms FROM MonitoringStation ms WHERE ms.corporation = :corporation AND LOWER(ms.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<MonitoringStation> findByCorporationAndNameContainingIgnoreCase(@Param("corporation") Corporation corporation, @Param("name") String name);
    
    /**
     * Busca estaciones creadas en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de estaciones creadas en el rango
     */
    List<MonitoringStation> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Cuenta estaciones por corporación.
     * @param corporationId el ID de la corporación
     * @return número de estaciones de la corporación
     */
    @Query("SELECT COUNT(ms) FROM MonitoringStation ms WHERE ms.corporation.id = :corporationId")
    long countByCorporationId(@Param("corporationId") Long corporationId);
    
    /**
     * Cuenta estaciones activas por corporación.
     * @param corporationId el ID de la corporación
     * @return número de estaciones activas de la corporación
     */
    @Query("SELECT COUNT(ms) FROM MonitoringStation ms WHERE ms.corporation.id = :corporationId AND ms.isActive = true")
    long countActiveByCorporationId(@Param("corporationId") Long corporationId);
    
    /**
     * Busca estaciones ordenadas por nombre.
     * @return lista de estaciones ordenadas por nombre
     */
    @Query("SELECT ms FROM MonitoringStation ms ORDER BY ms.name ASC")
    List<MonitoringStation> findAllOrderByName();
    
    /**
     * Busca estaciones activas ordenadas por nombre.
     * @return lista de estaciones activas ordenadas por nombre
     */
    @Query("SELECT ms FROM MonitoringStation ms WHERE ms.isActive = true ORDER BY ms.name ASC")
    List<MonitoringStation> findActiveOrderByName();
    
    /**
     * Busca estaciones por corporación ordenadas por nombre.
     * @param corporation la corporación
     * @return lista de estaciones de la corporación ordenadas por nombre
     */
    @Query("SELECT ms FROM MonitoringStation ms WHERE ms.corporation = :corporation ORDER BY ms.name ASC")
    List<MonitoringStation> findByCorporationOrderByName(@Param("corporation") Corporation corporation);
    
    /**
     * Busca estaciones activas por corporación ordenadas por nombre.
     * @param corporation la corporación
     * @return lista de estaciones activas de la corporación ordenadas por nombre
     */
    @Query("SELECT ms FROM MonitoringStation ms WHERE ms.corporation = :corporation AND ms.isActive = true ORDER BY ms.name ASC")
    List<MonitoringStation> findActiveByCorporationOrderByName(@Param("corporation") Corporation corporation);
    
    /**
     * Busca estaciones ordenadas por fecha de creación (más recientes primero).
     * @return lista de estaciones ordenadas por fecha de creación descendente
     */
    @Query("SELECT ms FROM MonitoringStation ms ORDER BY ms.createdAt DESC")
    List<MonitoringStation> findAllOrderByCreatedAtDesc();
}

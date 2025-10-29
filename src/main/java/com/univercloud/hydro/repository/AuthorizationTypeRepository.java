package com.univercloud.hydro.repository;

import com.univercloud.hydro.entity.AuthorizationType;
import com.univercloud.hydro.entity.Corporation;
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
 * Repository para la entidad AuthorizationType.
 * Proporciona métodos de consulta para gestionar tipos de autorización.
 */
@Repository
public interface AuthorizationTypeRepository extends JpaRepository<AuthorizationType, Long> {
    
    /**
     * Busca un tipo de autorización por nombre.
     * @param name el nombre del tipo de autorización
     * @return el tipo de autorización si existe
     */
    Optional<AuthorizationType> findByName(String name);
    
    /**
     * Verifica si existe un tipo de autorización con el nombre especificado.
     * @param name el nombre del tipo de autorización
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);
    
    /**
     * Busca tipos de autorización por nombre (búsqueda parcial, case-insensitive).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de tipos de autorización que coinciden con el nombre
     */
    @Query("SELECT at FROM AuthorizationType at WHERE LOWER(at.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<AuthorizationType> findByNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * Busca tipos de autorización creados en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de tipos de autorización creados en el rango
     */
    List<AuthorizationType> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Busca tipos de autorización ordenados por nombre.
     * @return lista de tipos de autorización ordenados por nombre
     */
    @Query("SELECT at FROM AuthorizationType at ORDER BY at.name ASC")
    List<AuthorizationType> findAllOrderByName();
    
    /**
     * Busca tipos de autorización ordenados por fecha de creación (más recientes primero).
     * @return lista de tipos de autorización ordenados por fecha de creación descendente
     */
    @Query("SELECT at FROM AuthorizationType at ORDER BY at.createdAt DESC")
    List<AuthorizationType> findAllOrderByCreatedAtDesc();
    
    // Métodos adicionales requeridos por los servicios
    
    /**
     * Busca tipos de autorización por corporación.
     * @param corporation la corporación
     * @return lista de tipos de autorización de la corporación
     */
    List<AuthorizationType> findByCorporation(Corporation corporation);
    
    /**
     * Busca tipos de autorización activos.
     * @return lista de tipos de autorización activos
     */
    List<AuthorizationType> findByIsActiveTrue();
    
    /**
     * Busca tipos de autorización inactivos.
     * @return lista de tipos de autorización inactivos
     */
    List<AuthorizationType> findByIsActiveFalse();
    
    /**
     * Busca tipos de autorización activos por corporación.
     * @param corporation la corporación
     * @return lista de tipos de autorización activos de la corporación
     */
    List<AuthorizationType> findByCorporationAndIsActiveTrue(Corporation corporation);
    
    /**
     * Busca tipos de autorización activos por nombre (búsqueda parcial, case-insensitive).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de tipos de autorización activos que coinciden con el nombre
     */
    @Query("SELECT at FROM AuthorizationType at WHERE at.isActive = true AND LOWER(at.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<AuthorizationType> findByIsActiveTrueAndNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * Cuenta tipos de autorización por corporación.
     * @param corporation la corporación
     * @return número de tipos de autorización de la corporación
     */
    long countByCorporation(Corporation corporation);
    
    /**
     * Cuenta tipos de autorización activos por corporación.
     * @param corporation la corporación
     * @return número de tipos de autorización activos de la corporación
     */
    long countByCorporationAndIsActiveTrue(Corporation corporation);
    
    /**
     * Busca tipos de autorización activos ordenados por nombre.
     * @return lista de tipos de autorización activos ordenados por nombre
     */
    @Query("SELECT at FROM AuthorizationType at WHERE at.isActive = true ORDER BY at.name ASC")
    List<AuthorizationType> findByIsActiveTrueOrderByName();
    
    /**
     * Busca tipos de autorización por corporación ordenados por nombre.
     * @param corporation la corporación
     * @return lista de tipos de autorización de la corporación ordenados por nombre
     */
    @Query("SELECT at FROM AuthorizationType at WHERE at.corporation = :corporation ORDER BY at.name ASC")
    List<AuthorizationType> findByCorporationOrderByName(@Param("corporation") Corporation corporation);
    
    /**
     * Busca tipos de autorización activos por corporación ordenados por nombre.
     * @param corporation la corporación
     * @return lista de tipos de autorización activos de la corporación ordenados por nombre
     */
    @Query("SELECT at FROM AuthorizationType at WHERE at.corporation = :corporation AND at.isActive = true ORDER BY at.name ASC")
    List<AuthorizationType> findByCorporationAndIsActiveTrueOrderByName(@Param("corporation") Corporation corporation);
    
    /**
     * Busca tipos de autorización por corporación con paginación.
     * @param corporation la corporación
     * @param pageable parámetros de paginación
     * @return página de tipos de autorización de la corporación
     */
    Page<AuthorizationType> findByCorporation(Corporation corporation, Pageable pageable);
}

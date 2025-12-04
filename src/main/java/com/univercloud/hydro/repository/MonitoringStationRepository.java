package com.univercloud.hydro.repository;

import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.MonitoringStation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
     * Carga la relación waterBasin para evitar problemas de lazy loading.
     * @param corporation la corporación
     * @return lista de estaciones de la corporación
     */
    @EntityGraph(attributePaths = {"basinSection.waterBasin"})
    List<MonitoringStation> findByCorporation(Corporation corporation);
    
    /**
     * Busca estaciones por corporación con paginación.
     * Carga la relación waterBasin para evitar problemas de lazy loading.
     * @param corporation la corporación
     * @param pageable parámetros de paginación
     * @return página de estaciones de la corporación
     */
    @EntityGraph(attributePaths = {"basinSection.waterBasin"})
    Page<MonitoringStation> findByCorporation(Corporation corporation, Pageable pageable);
    
    /**
     * Busca estaciones activas por corporación.
     * Carga la relación waterBasin para evitar problemas de lazy loading.
     * @param corporation la corporación
     * @return lista de estaciones activas de la corporación
     */
    @EntityGraph(attributePaths = {"basinSection.waterBasin"})
    List<MonitoringStation> findByCorporationAndIsActiveTrue(Corporation corporation);
    
    /**
     * Verifica si existe una estación con el nombre especificado en la corporación (comparación case-insensitive).
     * @param corporation la corporación
     * @param name el nombre de la estación
     * @return true si existe, false en caso contrario
     */
    @Query("SELECT COUNT(ms) > 0 FROM MonitoringStation ms WHERE ms.corporation = :corporation AND LOWER(ms.name) = LOWER(:name)")
    boolean existsByCorporationAndName(@Param("corporation") Corporation corporation, @Param("name") String name);
    
    /**
     * Verifica si existe una estación con el nombre especificado en la corporación, excluyendo un ID específico.
     * @param corporation la corporación
     * @param name el nombre de la estación
     * @param excludeId el ID a excluir de la búsqueda
     * @return true si existe, false en caso contrario
     */
    @Query("SELECT COUNT(ms) > 0 FROM MonitoringStation ms WHERE ms.corporation = :corporation AND LOWER(ms.name) = LOWER(:name) AND ms.id != :excludeId")
    boolean existsByCorporationAndNameExcludingId(@Param("corporation") Corporation corporation, @Param("name") String name, @Param("excludeId") Long excludeId);
    
    /**
     * Busca estaciones por nombre (búsqueda parcial, case-insensitive).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de estaciones que coinciden con el nombre
     */
    @Query("SELECT ms FROM MonitoringStation ms WHERE LOWER(ms.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<MonitoringStation> findByNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * Busca una estación por ID y corporación.
     * @param id el ID de la estación
     * @param corporationId el ID de la corporación
     * @return la estación si existe y pertenece a la corporación
     */
    @Query("SELECT ms FROM MonitoringStation ms WHERE ms.id = :id AND ms.corporation.id = :corporationId")
    Optional<MonitoringStation> findByIdAndCorporationId(@Param("id") Long id, @Param("corporationId") Long corporationId);
    
    /**
     * Cuenta estaciones de monitoreo por sección de cuenca.
     * @param basinSectionId el ID de la sección de cuenca
     * @return número de estaciones asociadas a la sección
     */
    @Query("SELECT COUNT(ms) FROM MonitoringStation ms WHERE ms.basinSection.id = :basinSectionId")
    long countByBasinSectionId(@Param("basinSectionId") Long basinSectionId);
    
    /**
     * Busca estaciones de monitoreo por nombre y sección de cuenca (búsqueda parcial, case-insensitive).
     * @param name el nombre o parte del nombre de la estación a buscar
     * @param basinSectionId el ID de la sección de cuenca
     * @return lista de estaciones que coinciden con el nombre y la sección de cuenca
     */
    @Query("SELECT ms FROM MonitoringStation ms WHERE LOWER(ms.name) LIKE LOWER(CONCAT('%', :name, '%')) AND ms.basinSection.id = :basinSectionId")
    List<MonitoringStation> findByNameAndBasinSectionId(@Param("name") String name, @Param("basinSectionId") Long basinSectionId);
}


package com.univercloud.hydro.repository;

import com.univercloud.hydro.entity.BasinSection;
import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.WaterBasin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para la entidad BasinSection.
 * Proporciona métodos de consulta para gestionar secciones de cuenca.
 */
@Repository
public interface BasinSectionRepository extends JpaRepository<BasinSection, Long> {
    
    /**
     * Busca secciones por cuenca hidrográfica.
     * @param waterBasin la cuenca hidrográfica
     * @return lista de secciones de la cuenca
     */
    List<BasinSection> findByWaterBasin(WaterBasin waterBasin);
    
    /**
     * Busca secciones activas por cuenca hidrográfica.
     * @param waterBasin la cuenca hidrográfica
     * @return lista de secciones activas de la cuenca
     */
    List<BasinSection> findByWaterBasinAndIsActiveTrue(WaterBasin waterBasin);
    
    /**
     * Busca secciones por corporación.
     * @param corporation la corporación
     * @return lista de secciones de la corporación
     */
    List<BasinSection> findByCorporation(Corporation corporation);

    /**
     * Busca secciones de la corporación con paginación.
     * @param corporation la corporación
     * @param pageable parámetros de paginación
     * @return página de secciones de la corporación
     */
    Page<BasinSection> findByCorporation(Corporation corporation, Pageable pageable);
    
    /**
     * Verifica si existe una sección con el nombre especificado en la corporación (comparación case-insensitive).
     * @param corporation la corporación
     * @param name el nombre de la sección
     * @return true si existe, false en caso contrario
     */
    @Query("SELECT COUNT(bs) > 0 FROM BasinSection bs WHERE bs.corporation = :corporation AND LOWER(bs.name) = LOWER(:name)")
    boolean existsByCorporationAndName(@Param("corporation") Corporation corporation, @Param("name") String name);
    
    /**
     * Verifica si existe una sección con el nombre especificado en la corporación, excluyendo un ID específico.
     * @param corporation la corporación
     * @param name el nombre de la sección
     * @param excludeId el ID a excluir de la búsqueda
     * @return true si existe, false en caso contrario
     */
    @Query("SELECT COUNT(bs) > 0 FROM BasinSection bs WHERE bs.corporation = :corporation AND LOWER(bs.name) = LOWER(:name) AND bs.id != :excludeId")
    boolean existsByCorporationAndNameExcludingId(@Param("corporation") Corporation corporation, @Param("name") String name, @Param("excludeId") Long excludeId);
    
    /**
     * Verifica si existe una sección con el nombre especificado en la corporación y cuenca hidrográfica (comparación case-insensitive).
     * @param corporation la corporación
     * @param waterBasin la cuenca hidrográfica
     * @param name el nombre de la sección
     * @return true si existe, false en caso contrario
     */
    @Query("SELECT COUNT(bs) > 0 FROM BasinSection bs WHERE bs.corporation = :corporation AND bs.waterBasin = :waterBasin AND LOWER(bs.name) = LOWER(:name)")
    boolean existsByCorporationAndWaterBasinAndName(@Param("corporation") Corporation corporation, @Param("waterBasin") WaterBasin waterBasin, @Param("name") String name);
    
    /**
     * Verifica si existe una sección con el nombre especificado en la corporación y cuenca hidrográfica, excluyendo un ID específico.
     * @param corporation la corporación
     * @param waterBasin la cuenca hidrográfica
     * @param name el nombre de la sección
     * @param excludeId el ID a excluir de la búsqueda
     * @return true si existe, false en caso contrario
     */
    @Query("SELECT COUNT(bs) > 0 FROM BasinSection bs WHERE bs.corporation = :corporation AND bs.waterBasin = :waterBasin AND LOWER(bs.name) = LOWER(:name) AND bs.id != :excludeId")
    boolean existsByCorporationAndWaterBasinAndNameExcludingId(@Param("corporation") Corporation corporation, @Param("waterBasin") WaterBasin waterBasin, @Param("name") String name, @Param("excludeId") Long excludeId);
    
    /**
     * Busca secciones por nombre (búsqueda parcial, case-insensitive).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de secciones que coinciden con el nombre
     */
    @Query("SELECT bs FROM BasinSection bs WHERE LOWER(bs.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<BasinSection> findByNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * Busca una sección por ID y corporación.
     * @param id el ID de la sección
     * @param corporationId el ID de la corporación
     * @return la sección si existe y pertenece a la corporación
     */
    @Query("SELECT bs FROM BasinSection bs WHERE bs.id = :id AND bs.corporation.id = :corporationId")
    Optional<BasinSection> findByIdAndCorporationId(@Param("id") Long id, @Param("corporationId") Long corporationId);
    
    /**
     * Cuenta estaciones de monitoreo por sección de cuenca.
     * @param basinSectionId el ID de la sección de cuenca
     * @return número de estaciones asociadas a la sección
     */
    @Query("SELECT COUNT(ms) FROM MonitoringStation ms WHERE ms.basinSection.id = :basinSectionId")
    long countMonitoringStationsByBasinSectionId(@Param("basinSectionId") Long basinSectionId);
}


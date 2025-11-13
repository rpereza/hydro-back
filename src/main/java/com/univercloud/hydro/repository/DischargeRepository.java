package com.univercloud.hydro.repository;

import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.Discharge;
import com.univercloud.hydro.entity.DischargeUser;
import com.univercloud.hydro.entity.Municipality;
import com.univercloud.hydro.entity.BasinSection;
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
 * Repository para la entidad Discharge.
 * Proporciona métodos de consulta para gestionar descargas de agua.
 */
@Repository
public interface DischargeRepository extends JpaRepository<Discharge, Long> {
    
    /**
     * Busca descargas por usuario de descarga.
     * @param dischargeUser el usuario de descarga
     * @return lista de descargas del usuario
     */
    List<Discharge> findByDischargeUser(DischargeUser dischargeUser);
    
    /**
     * Busca descargas por municipio.
     * @param municipality el municipio
     * @return lista de descargas del municipio
     */
    List<Discharge> findByMunicipality(Municipality municipality);
    
    /**
     * Busca descargas por sección de cuenca.
     * @param basinSection la sección de cuenca
     * @return lista de descargas de la sección
     */
    List<Discharge> findByBasinSection(BasinSection basinSection);
    
    /**
     * Busca descargas por corporación.
     * @param corporation la corporación
     * @return lista de descargas de la corporación
     */
    List<Discharge> findByCorporation(Corporation corporation);
    
    /**
     * Busca descargas por año.
     * @param year el año
     * @return lista de descargas del año
     */
    List<Discharge> findByYear(Integer year);
    
    /**
     * Busca descargas por tipo de descarga.
     * @param dischargeType el tipo de descarga
     * @return lista de descargas del tipo especificado
     */
    List<Discharge> findByDischargeType(Discharge.DischargeType dischargeType);
    
    /**
     * Busca descargas por tipo de recurso hídrico.
     * @param waterResourceType el tipo de recurso hídrico
     * @return lista de descargas del tipo de recurso
     */
    List<Discharge> findByWaterResourceType(Discharge.WaterResourceType waterResourceType);
    
    /**
     * Busca una descarga por número y año.
     * @param number el número de descarga
     * @param year el año
     * @return la descarga si existe
     */
    Optional<Discharge> findByNumberAndYear(Integer number, Integer year);
    
    /**
     * Verifica si existe una descarga con el número y año especificados.
     * @param number el número de descarga
     * @param year el año
     * @return true si existe, false en caso contrario
     */
    boolean existsByNumberAndYear(Integer number, Integer year);
    
    /**
     * Busca descargas por nombre (búsqueda parcial, case-insensitive).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de descargas que coinciden con el nombre
     */
    @Query("SELECT d FROM Discharge d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Discharge> findByNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * Busca descargas que están siendo monitoreadas.
     * @return lista de descargas monitoreadas
     */
    List<Discharge> findByIsSourceMonitoredTrue();
    
    /**
     * Busca descargas que no están siendo monitoreadas.
     * @return lista de descargas no monitoreadas
     */
    List<Discharge> findByIsSourceMonitoredFalse();
    
    /**
     * Busca descargas con reúso de cuenca.
     * @return lista de descargas con reúso de cuenca
     */
    List<Discharge> findByIsBasinRehuseTrue();
    
    /**
     * Busca descargas sin reúso de cuenca.
     * @return lista de descargas sin reúso de cuenca
     */
    List<Discharge> findByIsBasinRehuseFalse();
    
    /**
     * Busca descargas creadas en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de descargas creadas en el rango
     */
    List<Discharge> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Cuenta descargas por corporación.
     * @param corporationId el ID de la corporación
     * @return número de descargas de la corporación
     */
    @Query("SELECT COUNT(d) FROM Discharge d WHERE d.corporation.id = :corporationId")
    long countByCorporationId(@Param("corporationId") Long corporationId);
    
    /**
     * Busca descargas por municipio y año.
     * @param municipality el municipio
     * @param year el año
     * @return lista de descargas del municipio y año
     */
    List<Discharge> findByMunicipalityAndYear(Municipality municipality, Integer year);
    
    /**
     * Busca descargas por sección de cuenca y año.
     * @param basinSection la sección de cuenca
     * @param year el año
     * @return lista de descargas de la sección y año
     */
    List<Discharge> findByBasinSectionAndYear(BasinSection basinSection, Integer year);
    
    /**
     * Busca descargas ordenadas por fecha de creación (más recientes primero).
     * @return lista de descargas ordenadas por fecha de creación descendente
     */
    @Query("SELECT d FROM Discharge d ORDER BY d.createdAt DESC")
    List<Discharge> findAllOrderByCreatedAtDesc();
    
    /**
     * Busca descargas por corporación ordenadas por año descendente.
     * @param corporation la corporación
     * @return lista de descargas ordenadas por año descendente
     */
    @Query("SELECT d FROM Discharge d WHERE d.corporation = :corporation ORDER BY d.year DESC, d.number DESC")
    List<Discharge> findByCorporationOrderByYearDescNumberDesc(@Param("corporation") Corporation corporation);
    
    /**
     * Busca descargas por corporación con paginación.
     * @param corporation la corporación
     * @param pageable parámetros de paginación
     * @return página de descargas de la corporación
     */
    Page<Discharge> findByCorporation(Corporation corporation, Pageable pageable);
    
    /**
     * Busca descargas por corporación y año.
     * @param corporation la corporación
     * @param year el año
     * @return lista de descargas de la corporación y año
     */
    List<Discharge> findByCorporationAndYear(Corporation corporation, Integer year);
    
    /**
     * Busca descargas por corporación y tipo de descarga.
     * @param corporation la corporación
     * @param dischargeType el tipo de descarga
     * @return lista de descargas de la corporación y tipo
     */
    List<Discharge> findByCorporationAndDischargeType(Corporation corporation, Discharge.DischargeType dischargeType);
    
    /**
     * Busca descargas por corporación y tipo de recurso hídrico.
     * @param corporation la corporación
     * @param waterResourceType el tipo de recurso hídrico
     * @return lista de descargas de la corporación y tipo de recurso
     */
    List<Discharge> findByCorporationAndWaterResourceType(Corporation corporation, Discharge.WaterResourceType waterResourceType);
    
    /**
     * Busca descargas por corporación y nombre (búsqueda parcial, case-insensitive).
     * @param corporation la corporación
     * @param name el nombre o parte del nombre a buscar
     * @return lista de descargas de la corporación que coinciden con el nombre
     */
    @Query("SELECT d FROM Discharge d WHERE d.corporation = :corporation AND LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Discharge> findByCorporationAndNameContainingIgnoreCase(@Param("corporation") Corporation corporation, @Param("name") String name);
    
    /**
     * Busca descargas monitoreadas por corporación.
     * @param corporation la corporación
     * @return lista de descargas monitoreadas de la corporación
     */
    List<Discharge> findByCorporationAndIsSourceMonitoredTrue(Corporation corporation);
    
    /**
     * Busca descargas no monitoreadas por corporación.
     * @param corporation la corporación
     * @return lista de descargas no monitoreadas de la corporación
     */
    List<Discharge> findByCorporationAndIsSourceMonitoredFalse(Corporation corporation);
    
    /**
     * Busca descargas con reúso de cuenca por corporación.
     * @param corporation la corporación
     * @return lista de descargas con reúso de cuenca de la corporación
     */
    List<Discharge> findByCorporationAndIsBasinRehuseTrue(Corporation corporation);
    
    /**
     * Busca descargas sin reúso de cuenca por corporación.
     * @param corporation la corporación
     * @return lista de descargas sin reúso de cuenca de la corporación
     */
    List<Discharge> findByCorporationAndIsBasinRehuseFalse(Corporation corporation);
    
    /**
     * Busca descargas por corporación creadas en un rango de fechas.
     * @param corporation la corporación
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de descargas de la corporación creadas en el rango
     */
    List<Discharge> findByCorporationAndCreatedAtBetween(Corporation corporation, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Cuenta descargas por municipio.
     * @param municipalityId el ID del municipio
     * @return número de descargas del municipio
     */
    @Query("SELECT COUNT(d) FROM Discharge d WHERE d.municipality.id = :municipalityId")
    long countByMunicipalityId(@Param("municipalityId") Long municipalityId);
    
    /**
     * Cuenta descargas por sección de cuenca.
     * @param basinSectionId el ID de la sección de cuenca
     * @return número de descargas de la sección
     */
    @Query("SELECT COUNT(d) FROM Discharge d WHERE d.basinSection.id = :basinSectionId")
    long countByBasinSectionId(@Param("basinSectionId") Long basinSectionId);
    
    /**
     * Cuenta descargas por corporación y año.
     * @param corporationId el ID de la corporación
     * @param year el año
     * @return número de descargas de la corporación y año
     */
    @Query("SELECT COUNT(d) FROM Discharge d WHERE d.corporation.id = :corporationId AND d.year = :year")
    long countByCorporationAndYear(@Param("corporationId") Long corporationId, @Param("year") Integer year);
    
    /**
     * Busca descargas por corporación ordenadas por fecha de creación descendente.
     * @param corporation la corporación
     * @return lista de descargas de la corporación ordenadas por fecha descendente
     */
    @Query("SELECT d FROM Discharge d WHERE d.corporation = :corporation ORDER BY d.createdAt DESC")
    List<Discharge> findByCorporationOrderByCreatedAtDesc(@Param("corporation") Corporation corporation);
    
    /**
     * Busca una descarga por ID y corporación.
     * @param id el ID de la descarga
     * @param corporationId el ID de la corporación
     * @return la descarga si existe y pertenece a la corporación
     */
    @Query("SELECT d FROM Discharge d WHERE d.id = :id AND d.corporation.id = :corporationId")
    Optional<Discharge> findByIdAndCorporationId(@Param("id") Long id, @Param("corporationId") Long corporationId);
}

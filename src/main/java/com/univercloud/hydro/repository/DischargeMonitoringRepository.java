package com.univercloud.hydro.repository;

import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.Discharge;
import com.univercloud.hydro.entity.DischargeMonitoring;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository para la entidad DischargeMonitoring.
 * Proporciona métodos de consulta para gestionar monitoreos de descarga.
 */
@Repository
public interface DischargeMonitoringRepository extends JpaRepository<DischargeMonitoring, Long> {
    
    /**
     * Busca monitoreos por descarga.
     * @param discharge la descarga
     * @return lista de monitoreos de la descarga
     */
    List<DischargeMonitoring> findByDischarge(Discharge discharge);
    
    /**
     * Busca monitoreos por corporación.
     * @param corporation la corporación
     * @return lista de monitoreos de la corporación
     */
    List<DischargeMonitoring> findByCorporation(Corporation corporation);
    
    /**
     * Busca monitoreos creados en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de monitoreos creados en el rango
     */
    List<DischargeMonitoring> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Cuenta monitoreos por descarga.
     * @param dischargeId el ID de la descarga
     * @return número de monitoreos de la descarga
     */
    @Query("SELECT COUNT(dm) FROM DischargeMonitoring dm WHERE dm.discharge.id = :dischargeId")
    long countByDischargeId(@Param("dischargeId") Long dischargeId);
    
    /**
     * Cuenta monitoreos por corporación.
     * @param corporationId el ID de la corporación
     * @return número de monitoreos de la corporación
     */
    @Query("SELECT COUNT(dm) FROM DischargeMonitoring dm WHERE dm.corporation.id = :corporationId")
    long countByCorporationId(@Param("corporationId") Long corporationId);
    
    /**
     * Busca el último monitoreo de una descarga.
     * @param discharge la descarga
     * @return el último monitoreo de la descarga
     */
    @Query("SELECT dm FROM DischargeMonitoring dm WHERE dm.discharge = :discharge ORDER BY dm.createdAt DESC")
    Optional<DischargeMonitoring> findLatestByDischarge(@Param("discharge") Discharge discharge);
    
    /**
     * Busca monitoreos ordenados por fecha de creación (más recientes primero).
     * @return lista de monitoreos ordenados por fecha de creación descendente
     */
    @Query("SELECT dm FROM DischargeMonitoring dm ORDER BY dm.createdAt DESC")
    List<DischargeMonitoring> findAllOrderByCreatedAtDesc();
    
    /**
     * Busca monitoreos por descarga ordenados por fecha de creación descendente.
     * @param discharge la descarga
     * @return lista de monitoreos de la descarga ordenados por fecha descendente
     */
    @Query("SELECT dm FROM DischargeMonitoring dm WHERE dm.discharge = :discharge ORDER BY dm.createdAt DESC")
    List<DischargeMonitoring> findByDischargeOrderByCreatedAtDesc(@Param("discharge") Discharge discharge);
    
    /**
     * Busca monitoreos por corporación ordenados por fecha de creación descendente.
     * @param corporation la corporación
     * @return lista de monitoreos de la corporación ordenados por fecha descendente
     */
    @Query("SELECT dm FROM DischargeMonitoring dm WHERE dm.corporation = :corporation ORDER BY dm.createdAt DESC")
    List<DischargeMonitoring> findByCorporationOrderByCreatedAtDesc(@Param("corporation") Corporation corporation);
    
    /**
     * Busca monitoreos por año (basado en la fecha de creación).
     * @param year el año
     * @return lista de monitoreos del año
     */
    @Query("SELECT dm FROM DischargeMonitoring dm WHERE YEAR(dm.createdAt) = :year")
    List<DischargeMonitoring> findByYear(@Param("year") int year);
    
    /**
     * Busca monitoreos por descarga y año.
     * @param discharge la descarga
     * @param year el año
     * @return lista de monitoreos de la descarga y año
     */
    @Query("SELECT dm FROM DischargeMonitoring dm WHERE dm.discharge = :discharge AND YEAR(dm.createdAt) = :year")
    List<DischargeMonitoring> findByDischargeAndYear(@Param("discharge") Discharge discharge, @Param("year") int year);
    
    // Métodos adicionales requeridos por los servicios
    
    /**
     * Busca monitoreos por corporación con paginación.
     * @param corporation la corporación
     * @param pageable parámetros de paginación
     * @return página de monitoreos de la corporación
     */
    Page<DischargeMonitoring> findByCorporation(Corporation corporation, Pageable pageable);
    
    /**
     * Busca monitoreos por ID de descarga.
     * @param dischargeId el ID de la descarga
     * @return lista de monitoreos de la descarga
     */
    @Query("SELECT dm FROM DischargeMonitoring dm WHERE dm.discharge.id = :dischargeId")
    List<DischargeMonitoring> findByDischargeId(@Param("dischargeId") Long dischargeId);
    
    /**
     * Busca monitoreos por ID de estación de monitoreo.
     * @param monitoringStationId el ID de la estación de monitoreo
     * @return lista de monitoreos de la estación
     */
    @Query("SELECT dm FROM DischargeMonitoring dm WHERE dm.monitoringStation.id = :monitoringStationId")
    List<DischargeMonitoring> findByMonitoringStationId(@Param("monitoringStationId") Long monitoringStationId);
    
    /**
     * Busca monitoreos con valores de OD en un rango específico.
     * @param minOd valor mínimo de OD
     * @param maxOd valor máximo de OD
     * @return lista de monitoreos con OD en el rango
     */
    @Query("SELECT dm FROM DischargeMonitoring dm WHERE dm.od BETWEEN :minOd AND :maxOd")
    List<DischargeMonitoring> findByOdBetween(@Param("minOd") BigDecimal minOd, @Param("maxOd") BigDecimal maxOd);
    
    /**
     * Busca monitoreos con valores de SST en un rango específico.
     * @param minSst valor mínimo de SST
     * @param maxSst valor máximo de SST
     * @return lista de monitoreos con SST en el rango
     */
    @Query("SELECT dm FROM DischargeMonitoring dm WHERE dm.sst BETWEEN :minSst AND :maxSst")
    List<DischargeMonitoring> findBySstBetween(@Param("minSst") BigDecimal minSst, @Param("maxSst") BigDecimal maxSst);
    
    /**
     * Busca monitoreos con valores de DQO en un rango específico.
     * @param minDqo valor mínimo de DQO
     * @param maxDqo valor máximo de DQO
     * @return lista de monitoreos con DQO en el rango
     */
    @Query("SELECT dm FROM DischargeMonitoring dm WHERE dm.dqo BETWEEN :minDqo AND :maxDqo")
    List<DischargeMonitoring> findByDqoBetween(@Param("minDqo") BigDecimal minDqo, @Param("maxDqo") BigDecimal maxDqo);
    
    /**
     * Busca monitoreos con valores de PH en un rango específico.
     * @param minPh valor mínimo de PH
     * @param maxPh valor máximo de PH
     * @return lista de monitoreos con PH en el rango
     */
    @Query("SELECT dm FROM DischargeMonitoring dm WHERE dm.ph BETWEEN :minPh AND :maxPh")
    List<DischargeMonitoring> findByPhBetween(@Param("minPh") BigDecimal minPh, @Param("maxPh") BigDecimal maxPh);
    
    /**
     * Cuenta monitoreos por corporación.
     * @param corporation la corporación
     * @return número de monitoreos de la corporación
     */
    long countByCorporation(Corporation corporation);
    
    /**
     * Cuenta monitoreos por ID de estación de monitoreo.
     * @param monitoringStationId el ID de la estación de monitoreo
     * @return número de monitoreos de la estación
     */
    @Query("SELECT COUNT(dm) FROM DischargeMonitoring dm WHERE dm.monitoringStation.id = :monitoringStationId")
    long countByMonitoringStationId(@Param("monitoringStationId") Long monitoringStationId);
    
    /**
     * Busca un monitoreo por ID y corporación.
     * @param id el ID del monitoreo
     * @param corporationId el ID de la corporación
     * @return el monitoreo si existe y pertenece a la corporación
     */
    @Query("SELECT dm FROM DischargeMonitoring dm WHERE dm.id = :id AND dm.corporation.id = :corporationId")
    Optional<DischargeMonitoring> findByIdAndCorporationId(@Param("id") Long id, @Param("corporationId") Long corporationId);
}

package com.univercloud.hydro.repository;

import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.Monitoring;
import com.univercloud.hydro.entity.MonitoringStation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository para la entidad Monitoring.
 * Proporciona métodos de consulta para gestionar monitoreos de estaciones.
 */
@Repository
public interface MonitoringRepository extends JpaRepository<Monitoring, Long> {
    
    /**
     * Busca monitoreos por estación de monitoreo.
     * @param monitoringStation la estación de monitoreo
     * @return lista de monitoreos de la estación
     */
    List<Monitoring> findByMonitoringStation(MonitoringStation monitoringStation);
    
    /**
     * Busca monitoreos por fecha de monitoreo.
     * @param monitoringDate la fecha de monitoreo
     * @return lista de monitoreos de la fecha
     */
    List<Monitoring> findByMonitoringDate(LocalDate monitoringDate);
    
    /**
     * Busca monitoreos por corporación.
     * @param corporation la corporación
     * @return lista de monitoreos de la corporación
     */
    List<Monitoring> findByCorporation(Corporation corporation);

    /**
     * Busca monitoreos por corporación con paginación.
     * @param corporation la corporación
     * @param pageable parámetros de paginación
     * @return página de monitoreos de la corporación
     */
    Page<Monitoring> findByCorporation(Corporation corporation, Pageable pageable);
    
    /**
     * Busca un monitoreo por estación y fecha.
     * @param monitoringStation la estación de monitoreo
     * @param monitoringDate la fecha de monitoreo
     * @return el monitoreo si existe
     */
    Optional<Monitoring> findByMonitoringStationAndMonitoringDate(MonitoringStation monitoringStation, LocalDate monitoringDate);
    
    /**
     * Verifica si existe un monitoreo para la estación y fecha especificados.
     * @param monitoringStation la estación de monitoreo
     * @param monitoringDate la fecha de monitoreo
     * @return true si existe, false en caso contrario
     */
    boolean existsByMonitoringStationAndMonitoringDate(MonitoringStation monitoringStation, LocalDate monitoringDate);
    
    /**
     * Busca monitoreos en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de monitoreos en el rango de fechas
     */
    List<Monitoring> findByMonitoringDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Busca monitoreos por estación en un rango de fechas.
     * @param monitoringStation la estación de monitoreo
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de monitoreos de la estación en el rango
     */
    List<Monitoring> findByMonitoringStationAndMonitoringDateBetween(MonitoringStation monitoringStation, LocalDate startDate, LocalDate endDate);
    
    /**
     * Busca monitoreos por quien los realizó.
     * @param performedBy quien realizó el monitoreo
     * @return lista de monitoreos realizados por la persona
     */
    List<Monitoring> findByPerformedBy(String performedBy);
    
    /**
     * Busca monitoreos creados en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de monitoreos creados en el rango
     */
    List<Monitoring> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Cuenta monitoreos por estación.
     * @param monitoringStationId el ID de la estación
     * @return número de monitoreos de la estación
     */
    @Query("SELECT COUNT(m) FROM Monitoring m WHERE m.monitoringStation.id = :monitoringStationId")
    long countByMonitoringStationId(@Param("monitoringStationId") Long monitoringStationId);
    
    /**
     * Cuenta monitoreos por corporación.
     * @param corporationId el ID de la corporación
     * @return número de monitoreos de la corporación
     */
    @Query("SELECT COUNT(m) FROM Monitoring m WHERE m.corporation.id = :corporationId")
    long countByCorporationId(@Param("corporationId") Long corporationId);
    
    /**
     * Busca el último monitoreo de una estación.
     * @param monitoringStation la estación de monitoreo
     * @return el último monitoreo de la estación
     */
    @Query("SELECT m FROM Monitoring m WHERE m.monitoringStation = :monitoringStation ORDER BY m.monitoringDate DESC")
    Optional<Monitoring> findLatestByMonitoringStation(@Param("monitoringStation") MonitoringStation monitoringStation);
    
    /**
     * Busca monitoreos ordenados por fecha de monitoreo descendente.
     * @return lista de monitoreos ordenados por fecha descendente
     */
    @Query("SELECT m FROM Monitoring m ORDER BY m.monitoringDate DESC, m.createdAt DESC")
    List<Monitoring> findAllOrderByMonitoringDateDesc();
    
    /**
     * Busca monitoreos por corporación ordenados por fecha de monitoreo descendente.
     * @param corporation la corporación
     * @return lista de monitoreos ordenados por fecha descendente
     */
    @Query("SELECT m FROM Monitoring m WHERE m.corporation = :corporation ORDER BY m.monitoringDate DESC")
    List<Monitoring> findByCorporationOrderByMonitoringDateDesc(@Param("corporation") Corporation corporation);
    
    /**
     * Busca monitoreos por año.
     * @param year el año
     * @return lista de monitoreos del año
     */
    @Query("SELECT m FROM Monitoring m WHERE YEAR(m.monitoringDate) = :year")
    List<Monitoring> findByYear(@Param("year") int year);
    
    /**
     * Busca monitoreos por estación y año.
     * @param monitoringStation la estación de monitoreo
     * @param year el año
     * @return lista de monitoreos de la estación y año
     */
    @Query("SELECT m FROM Monitoring m WHERE m.monitoringStation = :monitoringStation AND YEAR(m.monitoringDate) = :year")
    List<Monitoring> findByMonitoringStationAndYear(@Param("monitoringStation") MonitoringStation monitoringStation, @Param("year") int year);
    
    /**
     * Busca un monitoreo por ID y corporación.
     * @param id el ID del monitoreo
     * @param corporationId el ID de la corporación
     * @return el monitoreo si existe y pertenece a la corporación
     */
    @Query("SELECT m FROM Monitoring m WHERE m.id = :id AND m.corporation.id = :corporationId")
    Optional<Monitoring> findByIdAndCorporationId(@Param("id") Long id, @Param("corporationId") Long corporationId);
}

package com.univercloud.hydro.repository;

import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.Discharge;
import com.univercloud.hydro.entity.DischargeParameter;
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
 * Repository para la entidad DischargeParameter.
 * Proporciona métodos de consulta para gestionar parámetros de descarga.
 */
@Repository
public interface DischargeParameterRepository extends JpaRepository<DischargeParameter, Long> {
    
    /**
     * Busca parámetros por descarga.
     * @param discharge la descarga
     * @return lista de parámetros de la descarga
     */
    List<DischargeParameter> findByDischarge(Discharge discharge);
    
    /**
     * Busca parámetros por corporación.
     * @param corporation la corporación
     * @return lista de parámetros de la corporación
     */
    List<DischargeParameter> findByCorporation(Corporation corporation);
    
    /**
     * Busca parámetros por mes.
     * @param month el mes
     * @return lista de parámetros del mes
     */
    List<DischargeParameter> findByMonth(DischargeParameter.Month month);
    
    /**
     * Busca parámetros por origen.
     * @param origin el origen
     * @return lista de parámetros del origen
     */
    List<DischargeParameter> findByOrigin(DischargeParameter.Origin origin);
    
    /**
     * Busca un parámetro por descarga y mes.
     * @param discharge la descarga
     * @param month el mes
     * @return el parámetro si existe
     */
    Optional<DischargeParameter> findByDischargeAndMonth(Discharge discharge, DischargeParameter.Month month);
    
    /**
     * Verifica si existe un parámetro para la descarga y mes especificados.
     * @param discharge la descarga
     * @param month el mes
     * @return true si existe, false en caso contrario
     */
    boolean existsByDischargeAndMonth(Discharge discharge, DischargeParameter.Month month);
    
    /**
     * Verifica si existe un parámetro para la descarga, mes y origen especificados.
     * @param discharge la descarga
     * @param month el mes
     * @param origin el origen
     * @return true si existe, false en caso contrario
     */
    boolean existsByDischargeAndMonthAndOrigin(Discharge discharge, DischargeParameter.Month month, DischargeParameter.Origin origin);
    
    /**
     * Verifica si existe un parámetro para la descarga, mes y origen especificados, excluyendo un ID.
     * @param discharge la descarga
     * @param month el mes
     * @param origin el origen
     * @param excludeId el ID a excluir de la búsqueda
     * @return true si existe, false en caso contrario
     */
    @Query("SELECT COUNT(dp) > 0 FROM DischargeParameter dp WHERE dp.discharge = :discharge AND dp.month = :month AND dp.origin = :origin AND dp.id != :excludeId")
    boolean existsByDischargeAndMonthAndOriginExcludingId(@Param("discharge") Discharge discharge, @Param("month") DischargeParameter.Month month, @Param("origin") DischargeParameter.Origin origin, @Param("excludeId") Long excludeId);
    
    /**
     * Busca parámetros por descarga y origen.
     * @param discharge la descarga
     * @param origin el origen
     * @return lista de parámetros de la descarga y origen
     */
    List<DischargeParameter> findByDischargeAndOrigin(Discharge discharge, DischargeParameter.Origin origin);
    
    /**
     * Busca parámetros creados en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de parámetros creados en el rango
     */
    List<DischargeParameter> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Cuenta parámetros por descarga.
     * @param dischargeId el ID de la descarga
     * @return número de parámetros de la descarga
     */
    @Query("SELECT COUNT(dp) FROM DischargeParameter dp WHERE dp.discharge.id = :dischargeId")
    long countByDischargeId(@Param("dischargeId") Long dischargeId);
    
    /**
     * Cuenta parámetros por corporación.
     * @param corporationId el ID de la corporación
     * @return número de parámetros de la corporación
     */
    @Query("SELECT COUNT(dp) FROM DischargeParameter dp WHERE dp.corporation.id = :corporationId")
    long countByCorporationId(@Param("corporationId") Long corporationId);
    
    /**
     * Cuenta parámetros por mes.
     * @param month el mes
     * @return número de parámetros del mes
     */
    @Query("SELECT COUNT(dp) FROM DischargeParameter dp WHERE dp.month = :month")
    long countByMonth(@Param("month") DischargeParameter.Month month);
    
    /**
     * Cuenta parámetros por origen.
     * @param origin el origen
     * @return número de parámetros del origen
     */
    @Query("SELECT COUNT(dp) FROM DischargeParameter dp WHERE dp.origin = :origin")
    long countByOrigin(@Param("origin") DischargeParameter.Origin origin);
    
    /**
     * Busca parámetros ordenados por mes.
     * @return lista de parámetros ordenados por mes
     */
    @Query("SELECT dp FROM DischargeParameter dp ORDER BY dp.month ASC")
    List<DischargeParameter> findAllOrderByMonth();
    
    /**
     * Busca parámetros por descarga ordenados por mes.
     * @param discharge la descarga
     * @return lista de parámetros de la descarga ordenados por mes
     */
    @Query("SELECT dp FROM DischargeParameter dp WHERE dp.discharge = :discharge ORDER BY dp.month ASC")
    List<DischargeParameter> findByDischargeOrderByMonth(@Param("discharge") Discharge discharge);
    
    /**
     * Busca parámetros por corporación ordenados por mes.
     * @param corporation la corporación
     * @return lista de parámetros de la corporación ordenados por mes
     */
    @Query("SELECT dp FROM DischargeParameter dp WHERE dp.corporation = :corporation ORDER BY dp.month ASC")
    List<DischargeParameter> findByCorporationOrderByMonth(@Param("corporation") Corporation corporation);
    
    /**
     * Busca parámetros ordenados por fecha de creación (más recientes primero).
     * @return lista de parámetros ordenados por fecha de creación descendente
     */
    @Query("SELECT dp FROM DischargeParameter dp ORDER BY dp.createdAt DESC")
    List<DischargeParameter> findAllOrderByCreatedAtDesc();
    
    /**
     * Busca parámetros por año (basado en la fecha de creación).
     * @param year el año
     * @return lista de parámetros del año
     */
    @Query("SELECT dp FROM DischargeParameter dp WHERE YEAR(dp.createdAt) = :year")
    List<DischargeParameter> findByYear(@Param("year") int year);
    
    /**
     * Busca parámetros por descarga y año.
     * @param discharge la descarga
     * @param year el año
     * @return lista de parámetros de la descarga y año
     */
    @Query("SELECT dp FROM DischargeParameter dp WHERE dp.discharge = :discharge AND YEAR(dp.createdAt) = :year")
    List<DischargeParameter> findByDischargeAndYear(@Param("discharge") Discharge discharge, @Param("year") int year);
    
    // Métodos adicionales requeridos por los servicios
    
    /**
     * Busca parámetros por corporación con paginación.
     * @param corporation la corporación
     * @param pageable parámetros de paginación
     * @return página de parámetros de la corporación
     */
    Page<DischargeParameter> findByCorporation(Corporation corporation, Pageable pageable);
    
    /**
     * Busca parámetros por ID de descarga.
     * @param dischargeId el ID de la descarga
     * @return lista de parámetros de la descarga
     */
    @Query("SELECT dp FROM DischargeParameter dp WHERE dp.discharge.id = :dischargeId")
    List<DischargeParameter> findByDischargeId(@Param("dischargeId") Long dischargeId);
    
    /**
     * Busca parámetros por ID de descarga y mes.
     * @param dischargeId el ID de la descarga
     * @param month el mes
     * @return lista de parámetros de la descarga y mes
     */
    @Query("SELECT dp FROM DischargeParameter dp WHERE dp.discharge.id = :dischargeId AND dp.month = :month")
    List<DischargeParameter> findByDischargeIdAndMonth(@Param("dischargeId") Long dischargeId, @Param("month") DischargeParameter.Month month);
    
    /**
     * Busca parámetros por ID de descarga y origen.
     * @param dischargeId el ID de la descarga
     * @param origin el origen
     * @return lista de parámetros de la descarga y origen
     */
    @Query("SELECT dp FROM DischargeParameter dp WHERE dp.discharge.id = :dischargeId AND dp.origin = :origin")
    List<DischargeParameter> findByDischargeIdAndOrigin(@Param("dischargeId") Long dischargeId, @Param("origin") DischargeParameter.Origin origin);
    
    /**
     * Busca parámetros con caudal volumen en un rango específico.
     * @param minCaudalVolumen valor mínimo de caudal volumen
     * @param maxCaudalVolumen valor máximo de caudal volumen
     * @return lista de parámetros con caudal volumen en el rango
     */
    @Query("SELECT dp FROM DischargeParameter dp WHERE dp.caudalVolumen BETWEEN :minCaudalVolumen AND :maxCaudalVolumen")
    List<DischargeParameter> findByCaudalVolumenBetween(@Param("minCaudalVolumen") BigDecimal minCaudalVolumen, @Param("maxCaudalVolumen") BigDecimal maxCaudalVolumen);
    
    /**
     * Busca parámetros con frecuencia en un rango específico.
     * @param minFrequency frecuencia mínima
     * @param maxFrequency frecuencia máxima
     * @return lista de parámetros con frecuencia en el rango
     */
    @Query("SELECT dp FROM DischargeParameter dp WHERE dp.frequency BETWEEN :minFrequency AND :maxFrequency")
    List<DischargeParameter> findByFrequencyBetween(@Param("minFrequency") Short minFrequency, @Param("maxFrequency") Short maxFrequency);
    
    /**
     * Busca parámetros con duración en un rango específico.
     * @param minDuration duración mínima
     * @param maxDuration duración máxima
     * @return lista de parámetros con duración en el rango
     */
    @Query("SELECT dp FROM DischargeParameter dp WHERE dp.duration BETWEEN :minDuration AND :maxDuration")
    List<DischargeParameter> findByDurationBetween(@Param("minDuration") Short minDuration, @Param("maxDuration") Short maxDuration);
    
    /**
     * Cuenta parámetros por corporación.
     * @param corporation la corporación
     * @return número de parámetros de la corporación
     */
    long countByCorporation(Corporation corporation);
    
    /**
     * Busca parámetros por corporación ordenados por fecha de creación descendente.
     * @param corporation la corporación
     * @return lista de parámetros de la corporación ordenados por fecha descendente
     */
    @Query("SELECT dp FROM DischargeParameter dp WHERE dp.corporation = :corporation ORDER BY dp.createdAt DESC")
    List<DischargeParameter> findByCorporationOrderByCreatedAtDesc(@Param("corporation") Corporation corporation);
    
    /**
     * Busca un parámetro por ID y corporación.
     * @param id el ID del parámetro
     * @param corporationId el ID de la corporación
     * @return el parámetro si existe y pertenece a la corporación
     */
    @Query("SELECT dp FROM DischargeParameter dp WHERE dp.id = :id AND dp.corporation.id = :corporationId")
    Optional<DischargeParameter> findByIdAndCorporationId(@Param("id") Long id, @Param("corporationId") Long corporationId);
}

package com.univercloud.hydro.repository;

import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.ProjectProgress;
import com.univercloud.hydro.entity.DischargeUser;
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
 * Repository para la entidad ProjectProgress.
 * Proporciona métodos de consulta para gestionar progreso de proyectos.
 */
@Repository
public interface ProjectProgressRepository extends JpaRepository<ProjectProgress, Long> {
    
    /**
     * Busca progresos por corporación.
     * @param corporation la corporación
     * @return lista de progresos de la corporación
     */
    List<ProjectProgress> findByCorporation(Corporation corporation);
    
   /**
     * Busca progresos creados en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de progresos creados en el rango
     */
    List<ProjectProgress> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Cuenta progresos por corporación.
     * @param corporationId el ID de la corporación
     * @return número de progresos de la corporación
     */
    @Query("SELECT COUNT(pp) FROM ProjectProgress pp WHERE pp.corporation.id = :corporationId")
    long countByCorporationId(@Param("corporationId") Long corporationId);
    
    /**
     * Busca progresos ordenados por fecha de creación (más recientes primero).
     * @return lista de progresos ordenados por fecha de creación descendente
     */
    @Query("SELECT pp FROM ProjectProgress pp ORDER BY pp.createdAt DESC")
    List<ProjectProgress> findAllOrderByCreatedAtDesc();
    
    // Métodos adicionales requeridos por los servicios
    
    /**
     * Busca progresos por corporación con paginación.
     * @param corporation la corporación
     * @param pageable parámetros de paginación
     * @return página de progresos de la corporación
     */
    Page<ProjectProgress> findByCorporation(Corporation corporation, Pageable pageable);
    
    /**
     * Busca progresos por ID de usuario de descarga.
     * @param dischargeUserId el ID del usuario de descarga
     * @return lista de progresos del usuario de descarga
     */
    @Query("SELECT pp FROM ProjectProgress pp WHERE pp.dischargeUser.id = :dischargeUserId")
    List<ProjectProgress> findByDischargeUserId(@Param("dischargeUserId") Long dischargeUserId);
    
    /**
     * Busca progresos por año.
     * @param year el año
     * @return lista de progresos del año
     */
    List<ProjectProgress> findByYear(Integer year);
    
    /**
     * Busca progresos por ID de usuario de descarga y año.
     * @param dischargeUserId el ID del usuario de descarga
     * @param year el año
     * @return lista de progresos del usuario de descarga y año
     */
    @Query("SELECT pp FROM ProjectProgress pp WHERE pp.dischargeUser.id = :dischargeUserId AND pp.year = :year")
    List<ProjectProgress> findByDischargeUserIdAndYear(@Param("dischargeUserId") Long dischargeUserId, @Param("year") Integer year);
    
    /**
     * Busca progresos con porcentaje CCI en un rango específico.
     * @param minCciPercentage porcentaje mínimo de CCI
     * @param maxCciPercentage porcentaje máximo de CCI
     * @return lista de progresos con CCI en el rango
     */
    @Query("SELECT pp FROM ProjectProgress pp WHERE pp.cciPercentage BETWEEN :minCciPercentage AND :maxCciPercentage")
    List<ProjectProgress> findByCciPercentageBetween(@Param("minCciPercentage") BigDecimal minCciPercentage, @Param("maxCciPercentage") BigDecimal maxCciPercentage);
    
    /**
     * Busca progresos con porcentaje CEV en un rango específico.
     * @param minCevPercentage porcentaje mínimo de CEV
     * @param maxCevPercentage porcentaje máximo de CEV
     * @return lista de progresos con CEV en el rango
     */
    @Query("SELECT pp FROM ProjectProgress pp WHERE pp.cevPercentage BETWEEN :minCevPercentage AND :maxCevPercentage")
    List<ProjectProgress> findByCevPercentageBetween(@Param("minCevPercentage") BigDecimal minCevPercentage, @Param("maxCevPercentage") BigDecimal maxCevPercentage);
    
    /**
     * Busca progresos con porcentaje CDS en un rango específico.
     * @param minCdsPercentage porcentaje mínimo de CDS
     * @param maxCdsPercentage porcentaje máximo de CDS
     * @return lista de progresos con CDS en el rango
     */
    @Query("SELECT pp FROM ProjectProgress pp WHERE pp.cdsPercentage BETWEEN :minCdsPercentage AND :maxCdsPercentage")
    List<ProjectProgress> findByCdsPercentageBetween(@Param("minCdsPercentage") BigDecimal minCdsPercentage, @Param("maxCdsPercentage") BigDecimal maxCdsPercentage);
    
    /**
     * Busca progresos con porcentaje CCS en un rango específico.
     * @param minCcsPercentage porcentaje mínimo de CCS
     * @param maxCcsPercentage porcentaje máximo de CCS
     * @return lista de progresos con CCS en el rango
     */
    @Query("SELECT pp FROM ProjectProgress pp WHERE pp.ccsPercentage BETWEEN :minCcsPercentage AND :maxCcsPercentage")
    List<ProjectProgress> findByCcsPercentageBetween(@Param("minCcsPercentage") BigDecimal minCcsPercentage, @Param("maxCcsPercentage") BigDecimal maxCcsPercentage);
    
    /**
     * Cuenta progresos por corporación.
     * @param corporation la corporación
     * @return número de progresos de la corporación
     */
    long countByCorporation(Corporation corporation);
    
    /**
     * Cuenta progresos por ID de usuario de descarga.
     * @param dischargeUserId el ID del usuario de descarga
     * @return número de progresos del usuario de descarga
     */
    @Query("SELECT COUNT(pp) FROM ProjectProgress pp WHERE pp.dischargeUser.id = :dischargeUserId")
    long countByDischargeUserId(@Param("dischargeUserId") Long dischargeUserId);
    
    /**
     * Cuenta progresos por año.
     * @param year el año
     * @return número de progresos del año
     */
    long countByYear(Integer year);
    
    /**
     * Verifica si existe un progreso para el usuario de descarga y año especificados en la corporación.
     * @param corporation la corporación
     * @param dischargeUser el usuario de descarga
     * @param year el año
     * @return true si existe, false en caso contrario
     */
    boolean existsByCorporationAndDischargeUserAndYear(Corporation corporation, DischargeUser dischargeUser, Integer year);
    
    /**
     * Verifica si existe un progreso para el usuario de descarga y año especificados en la corporación.
     * @param corporation la corporación
     * @param dischargeUserId el ID del usuario de descarga
     * @param year el año
     * @return true si existe, false en caso contrario
     */
    @Query("SELECT COUNT(pp) > 0 FROM ProjectProgress pp WHERE pp.corporation = :corporation AND pp.dischargeUser.id = :dischargeUserId AND pp.year = :year")
    boolean existsByCorporationAndDischargeUserAndYear(@Param("corporation") Corporation corporation, @Param("dischargeUserId") Long dischargeUserId, @Param("year") Integer year);
    
    /**
     * Busca progresos ordenados por año (más recientes primero).
     * @return lista de progresos ordenados por año descendente
     */
    @Query("SELECT pp FROM ProjectProgress pp ORDER BY pp.year DESC")
    List<ProjectProgress> findAllOrderByYearDesc();
    
    /**
     * Busca progresos por corporación ordenados por año (más recientes primero).
     * @param corporation la corporación
     * @return lista de progresos de la corporación ordenados por año descendente
     */
    @Query("SELECT pp FROM ProjectProgress pp WHERE pp.corporation = :corporation ORDER BY pp.year DESC")
    List<ProjectProgress> findByCorporationOrderByYearDesc(@Param("corporation") Corporation corporation);
    
    /**
     * Busca un progreso por ID y corporación.
     * @param id el ID del progreso
     * @param corporationId el ID de la corporación
     * @return el progreso si existe y pertenece a la corporación
     */
    @Query("SELECT pp FROM ProjectProgress pp WHERE pp.id = :id AND pp.corporation.id = :corporationId")
    Optional<ProjectProgress> findByIdAndCorporationId(@Param("id") Long id, @Param("corporationId") Long corporationId);
}

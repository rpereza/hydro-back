package com.univercloud.hydro.repository;

import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.MinimumTariff;
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
 * Repository para la entidad MinimumTariff.
 * Proporciona métodos de consulta para gestionar tarifas mínimas.
 */
@Repository
public interface MinimumTariffRepository extends JpaRepository<MinimumTariff, Long> {
    
    /**
     * Busca tarifas por corporación.
     * @param corporation la corporación
     * @return lista de tarifas de la corporación
     */
    List<MinimumTariff> findByCorporation(Corporation corporation);
    
    /**
     * Busca tarifas activas.
     * @return lista de tarifas activas
     */
    List<MinimumTariff> findByIsActiveTrue();
    
    /**
     * Busca tarifas inactivas.
     * @return lista de tarifas inactivas
     */
    List<MinimumTariff> findByIsActiveFalse();
    
    /**
     * Busca tarifas activas por corporación.
     * @param corporation la corporación
     * @return lista de tarifas activas de la corporación
     */
    List<MinimumTariff> findByCorporationAndIsActiveTrue(Corporation corporation);
    
    /**
     * Busca tarifas creadas en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de tarifas creadas en el rango
     */
    List<MinimumTariff> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Cuenta tarifas por corporación.
     * @param corporationId el ID de la corporación
     * @return número de tarifas de la corporación
     */
    @Query("SELECT COUNT(mt) FROM MinimumTariff mt WHERE mt.corporation.id = :corporationId")
    long countByCorporationId(@Param("corporationId") Long corporationId);
    
    /**
     * Cuenta tarifas activas por corporación.
     * @param corporationId el ID de la corporación
     * @return número de tarifas activas de la corporación
     */
    @Query("SELECT COUNT(mt) FROM MinimumTariff mt WHERE mt.corporation.id = :corporationId AND mt.isActive = true")
    long countActiveByCorporationId(@Param("corporationId") Long corporationId);
    
    /**
     * Busca tarifas ordenadas por fecha de creación (más recientes primero).
     * @return lista de tarifas ordenadas por fecha de creación descendente
     */
    @Query("SELECT mt FROM MinimumTariff mt ORDER BY mt.createdAt DESC")
    List<MinimumTariff> findAllOrderByCreatedAtDesc();
    
    // Métodos adicionales requeridos por los servicios
    
    /**
     * Busca tarifas por corporación con paginación.
     * @param corporation la corporación
     * @param pageable parámetros de paginación
     * @return página de tarifas de la corporación
     */
    Page<MinimumTariff> findByCorporation(Corporation corporation, Pageable pageable);
    
    /**
     * Busca tarifas por año.
     * @param year el año
     * @return lista de tarifas del año
     */
    List<MinimumTariff> findByYear(Integer year);
    
    /**
     * Busca tarifas activas por año.
     * @param year el año
     * @return lista de tarifas activas del año
     */
    List<MinimumTariff> findByIsActiveTrueAndYear(Integer year);
    
    /**
     * Busca tarifas por corporación y año.
     * @param corporation la corporación
     * @param year el año
     * @return lista de tarifas de la corporación y año
     */
    List<MinimumTariff> findByCorporationAndYear(Corporation corporation, Integer year);
    
    /**
     * Busca tarifas activas por corporación y año.
     * @param corporation la corporación
     * @param year el año
     * @return lista de tarifas activas de la corporación y año
     */
    List<MinimumTariff> findByCorporationAndIsActiveTrueAndYear(Corporation corporation, Integer year);
    
    /**
     * Busca tarifas con valores de DBO en un rango específico.
     * @param minDboValue valor mínimo de DBO
     * @param maxDboValue valor máximo de DBO
     * @return lista de tarifas con DBO en el rango
     */
    @Query("SELECT mt FROM MinimumTariff mt WHERE mt.dboValue BETWEEN :minDboValue AND :maxDboValue")
    List<MinimumTariff> findByDboValueBetween(@Param("minDboValue") BigDecimal minDboValue, @Param("maxDboValue") BigDecimal maxDboValue);
    
    /**
     * Busca tarifas con valores de SST en un rango específico.
     * @param minSstValue valor mínimo de SST
     * @param maxSstValue valor máximo de SST
     * @return lista de tarifas con SST en el rango
     */
    @Query("SELECT mt FROM MinimumTariff mt WHERE mt.sstValue BETWEEN :minSstValue AND :maxSstValue")
    List<MinimumTariff> findBySstValueBetween(@Param("minSstValue") BigDecimal minSstValue, @Param("maxSstValue") BigDecimal maxSstValue);
    
    /**
     * Busca tarifas con valores de IPC en un rango específico.
     * @param minIpcValue valor mínimo de IPC
     * @param maxIpcValue valor máximo de IPC
     * @return lista de tarifas con IPC en el rango
     */
    @Query("SELECT mt FROM MinimumTariff mt WHERE mt.ipcValue BETWEEN :minIpcValue AND :maxIpcValue")
    List<MinimumTariff> findByIpcValueBetween(@Param("minIpcValue") BigDecimal minIpcValue, @Param("maxIpcValue") BigDecimal maxIpcValue);
    
    /**
     * Cuenta tarifas por corporación.
     * @param corporation la corporación
     * @return número de tarifas de la corporación
     */
    long countByCorporation(Corporation corporation);
    
    /**
     * Cuenta tarifas activas por corporación.
     * @param corporation la corporación
     * @return número de tarifas activas de la corporación
     */
    long countByCorporationAndIsActiveTrue(Corporation corporation);
    
    /**
     * Cuenta tarifas por año.
     * @param year el año
     * @return número de tarifas del año
     */
    long countByYear(Integer year);
    
    /**
     * Verifica si existe una tarifa para el año especificado en la corporación.
     * @param corporation la corporación
     * @param year el año
     * @return true si existe, false en caso contrario
     */
    boolean existsByCorporationAndYear(Corporation corporation, Integer year);
    
    /**
     * Busca tarifas ordenadas por año (más recientes primero).
     * @return lista de tarifas ordenadas por año descendente
     */
    @Query("SELECT mt FROM MinimumTariff mt ORDER BY mt.year DESC")
    List<MinimumTariff> findAllOrderByYearDesc();
    
    /**
     * Busca tarifas activas ordenadas por año (más recientes primero).
     * @return lista de tarifas activas ordenadas por año descendente
     */
    @Query("SELECT mt FROM MinimumTariff mt WHERE mt.isActive = true ORDER BY mt.year DESC")
    List<MinimumTariff> findByIsActiveTrueOrderByYearDesc();
    
    /**
     * Busca tarifas por corporación ordenadas por año (más recientes primero).
     * @param corporation la corporación
     * @return lista de tarifas de la corporación ordenadas por año descendente
     */
    @Query("SELECT mt FROM MinimumTariff mt WHERE mt.corporation = :corporation ORDER BY mt.year DESC")
    List<MinimumTariff> findByCorporationOrderByYearDesc(@Param("corporation") Corporation corporation);
    
    /**
     * Busca tarifas activas por corporación ordenadas por año (más recientes primero).
     * @param corporation la corporación
     * @return lista de tarifas activas de la corporación ordenadas por año descendente
     */
    @Query("SELECT mt FROM MinimumTariff mt WHERE mt.corporation = :corporation AND mt.isActive = true ORDER BY mt.year DESC")
    List<MinimumTariff> findByCorporationAndIsActiveTrueOrderByYearDesc(@Param("corporation") Corporation corporation);
    
    /**
     * Busca una tarifa por ID y corporación.
     * @param id el ID de la tarifa
     * @param corporationId el ID de la corporación
     * @return la tarifa si existe y pertenece a la corporación
     */
    @Query("SELECT mt FROM MinimumTariff mt WHERE mt.id = :id AND mt.corporation.id = :corporationId")
    Optional<MinimumTariff> findByIdAndCorporationId(@Param("id") Long id, @Param("corporationId") Long corporationId);
}

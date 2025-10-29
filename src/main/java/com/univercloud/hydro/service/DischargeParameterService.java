package com.univercloud.hydro.service;

import com.univercloud.hydro.entity.DischargeParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de Parámetros de Descarga.
 * Proporciona operaciones CRUD y lógica de negocio para parámetros de descarga.
 */
public interface DischargeParameterService {
    
    /**
     * Crea un nuevo parámetro de descarga.
     * @param dischargeParameter el parámetro de descarga a crear
     * @return el parámetro de descarga creado
     * @throws IllegalArgumentException si los datos son inválidos
     */
    DischargeParameter createDischargeParameter(DischargeParameter dischargeParameter);
    
    /**
     * Actualiza un parámetro de descarga existente.
     * @param dischargeParameter el parámetro de descarga a actualizar
     * @return el parámetro de descarga actualizado
     * @throws IllegalArgumentException si el parámetro de descarga no existe
     */
    DischargeParameter updateDischargeParameter(DischargeParameter dischargeParameter);
    
    /**
     * Obtiene un parámetro de descarga por su ID.
     * @param id el ID del parámetro de descarga
     * @return el parámetro de descarga, si existe
     */
    Optional<DischargeParameter> getDischargeParameterById(Long id);
    
    /**
     * Obtiene todos los parámetros de descarga de la corporación del usuario autenticado.
     * @param pageable parámetros de paginación
     * @return página de parámetros de descarga
     */
    Page<DischargeParameter> getMyCorporationDischargeParameters(Pageable pageable);
    
    /**
     * Obtiene todos los parámetros de descarga de la corporación del usuario autenticado.
     * @return lista de parámetros de descarga
     */
    List<DischargeParameter> getAllMyCorporationDischargeParameters();
    
    /**
     * Obtiene parámetros de descarga por descarga.
     * @param dischargeId el ID de la descarga
     * @return lista de parámetros de la descarga
     */
    List<DischargeParameter> getDischargeParametersByDischarge(Long dischargeId);
    
    /**
     * Obtiene parámetros de descarga por mes.
     * @param month el mes
     * @return lista de parámetros del mes
     */
    List<DischargeParameter> getDischargeParametersByMonth(DischargeParameter.Month month);
    
    /**
     * Obtiene parámetros de descarga por origen.
     * @param origin el origen
     * @return lista de parámetros del origen
     */
    List<DischargeParameter> getDischargeParametersByOrigin(DischargeParameter.Origin origin);
    
    /**
     * Obtiene parámetros de descarga por descarga y mes.
     * @param dischargeId el ID de la descarga
     * @param month el mes
     * @return lista de parámetros de la descarga y mes
     */
    List<DischargeParameter> getDischargeParametersByDischargeAndMonth(Long dischargeId, DischargeParameter.Month month);
    
    /**
     * Obtiene parámetros de descarga por descarga y origen.
     * @param dischargeId el ID de la descarga
     * @param origin el origen
     * @return lista de parámetros de la descarga y origen
     */
    List<DischargeParameter> getDischargeParametersByDischargeAndOrigin(Long dischargeId, DischargeParameter.Origin origin);
    
    /**
     * Obtiene parámetros de descarga creados en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de parámetros creados en el rango
     */
    List<DischargeParameter> getDischargeParametersByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Obtiene parámetros de descarga con caudal volumen en un rango específico.
     * @param minCaudalVolumen valor mínimo de caudal volumen
     * @param maxCaudalVolumen valor máximo de caudal volumen
     * @return lista de parámetros con caudal volumen en el rango
     */
    List<DischargeParameter> getDischargeParametersByCaudalVolumenRange(BigDecimal minCaudalVolumen, BigDecimal maxCaudalVolumen);
    
    /**
     * Obtiene parámetros de descarga con frecuencia en un rango específico.
     * @param minFrequency frecuencia mínima
     * @param maxFrequency frecuencia máxima
     * @return lista de parámetros con frecuencia en el rango
     */
    List<DischargeParameter> getDischargeParametersByFrequencyRange(Short minFrequency, Short maxFrequency);
    
    /**
     * Obtiene parámetros de descarga con duración en un rango específico.
     * @param minDuration duración mínima
     * @param maxDuration duración máxima
     * @return lista de parámetros con duración en el rango
     */
    List<DischargeParameter> getDischargeParametersByDurationRange(Short minDuration, Short maxDuration);
    
    /**
     * Cuenta el número de parámetros de descarga de la corporación del usuario autenticado.
     * @return número de parámetros de descarga
     */
    long countMyCorporationDischargeParameters();
    
    /**
     * Cuenta parámetros de descarga por descarga.
     * @param dischargeId el ID de la descarga
     * @return número de parámetros de la descarga
     */
    long countDischargeParametersByDischarge(Long dischargeId);
    
    /**
     * Cuenta parámetros de descarga por mes.
     * @param month el mes
     * @return número de parámetros del mes
     */
    long countDischargeParametersByMonth(DischargeParameter.Month month);
    
    /**
     * Cuenta parámetros de descarga por origen.
     * @param origin el origen
     * @return número de parámetros del origen
     */
    long countDischargeParametersByOrigin(DischargeParameter.Origin origin);
    
    /**
     * Elimina un parámetro de descarga.
     * @param id el ID del parámetro de descarga a eliminar
     * @return true si se eliminó correctamente
     * @throws IllegalArgumentException si el parámetro de descarga no existe
     */
    boolean deleteDischargeParameter(Long id);
    
    /**
     * Obtiene parámetros de descarga ordenados por fecha de creación (más recientes primero).
     * @return lista de parámetros de descarga ordenados por fecha de creación descendente
     */
    List<DischargeParameter> getDischargeParametersOrderByCreatedAtDesc();
    
    /**
     * Obtiene parámetros de descarga de la corporación ordenados por fecha de creación (más recientes primero).
     * @return lista de parámetros de descarga de la corporación ordenados por fecha de creación descendente
     */
    List<DischargeParameter> getMyCorporationDischargeParametersOrderByCreatedAtDesc();
    
    /**
     * Obtiene estadísticas de parámetros de descarga de la corporación del usuario autenticado.
     * @return estadísticas de parámetros de descarga
     */
    DischargeParameterStats getMyCorporationDischargeParameterStats();
    
    /**
     * Clase interna para estadísticas de parámetros de descarga
     */
    class DischargeParameterStats {
        private long totalDischargeParameters;
        private long totalDischarges;
        private long totalMonths;
        private long totalOrigins;
        private BigDecimal averageCaudalVolumen;
        private BigDecimal averageFrequency;
        private BigDecimal averageDuration;
        private BigDecimal averageConcDbo;
        private BigDecimal averageConcSst;
        private BigDecimal averageCcDbo;
        private BigDecimal averageCcSst;
        
        // Constructors
        public DischargeParameterStats() {}
        
        // Getters and Setters
        public long getTotalDischargeParameters() { return totalDischargeParameters; }
        public void setTotalDischargeParameters(long totalDischargeParameters) { this.totalDischargeParameters = totalDischargeParameters; }
        
        public long getTotalDischarges() { return totalDischarges; }
        public void setTotalDischarges(long totalDischarges) { this.totalDischarges = totalDischarges; }
        
        public long getTotalMonths() { return totalMonths; }
        public void setTotalMonths(long totalMonths) { this.totalMonths = totalMonths; }
        
        public long getTotalOrigins() { return totalOrigins; }
        public void setTotalOrigins(long totalOrigins) { this.totalOrigins = totalOrigins; }
        
        public BigDecimal getAverageCaudalVolumen() { return averageCaudalVolumen; }
        public void setAverageCaudalVolumen(BigDecimal averageCaudalVolumen) { this.averageCaudalVolumen = averageCaudalVolumen; }
        
        public BigDecimal getAverageFrequency() { return averageFrequency; }
        public void setAverageFrequency(BigDecimal averageFrequency) { this.averageFrequency = averageFrequency; }
        
        public BigDecimal getAverageDuration() { return averageDuration; }
        public void setAverageDuration(BigDecimal averageDuration) { this.averageDuration = averageDuration; }
        
        public BigDecimal getAverageConcDbo() { return averageConcDbo; }
        public void setAverageConcDbo(BigDecimal averageConcDbo) { this.averageConcDbo = averageConcDbo; }
        
        public BigDecimal getAverageConcSst() { return averageConcSst; }
        public void setAverageConcSst(BigDecimal averageConcSst) { this.averageConcSst = averageConcSst; }
        
        public BigDecimal getAverageCcDbo() { return averageCcDbo; }
        public void setAverageCcDbo(BigDecimal averageCcDbo) { this.averageCcDbo = averageCcDbo; }
        
        public BigDecimal getAverageCcSst() { return averageCcSst; }
        public void setAverageCcSst(BigDecimal averageCcSst) { this.averageCcSst = averageCcSst; }
    }
}

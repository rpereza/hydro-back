package com.univercloud.hydro.service;

import com.univercloud.hydro.entity.DischargeMonitoring;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de Monitoreos de Descarga.
 * Proporciona operaciones CRUD y lógica de negocio para monitoreos de descarga.
 */
public interface DischargeMonitoringService {
    
    /**
     * Crea un nuevo monitoreo de descarga.
     * @param dischargeMonitoring el monitoreo de descarga a crear
     * @return el monitoreo de descarga creado
     * @throws IllegalArgumentException si los datos son inválidos
     */
    DischargeMonitoring createDischargeMonitoring(DischargeMonitoring dischargeMonitoring);
    
    /**
     * Actualiza un monitoreo de descarga existente.
     * @param dischargeMonitoring el monitoreo de descarga a actualizar
     * @return el monitoreo de descarga actualizado
     * @throws IllegalArgumentException si el monitoreo de descarga no existe
     */
    DischargeMonitoring updateDischargeMonitoring(DischargeMonitoring dischargeMonitoring);
    
    /**
     * Obtiene un monitoreo de descarga por su ID.
     * @param id el ID del monitoreo de descarga
     * @return el monitoreo de descarga, si existe
     */
    Optional<DischargeMonitoring> getDischargeMonitoringById(Long id);
    
    /**
     * Obtiene todos los monitoreos de descarga de la corporación del usuario autenticado.
     * @param pageable parámetros de paginación
     * @return página de monitoreos de descarga
     */
    Page<DischargeMonitoring> getMyCorporationDischargeMonitorings(Pageable pageable);
    
    /**
     * Obtiene todos los monitoreos de descarga de la corporación del usuario autenticado.
     * @return lista de monitoreos de descarga
     */
    List<DischargeMonitoring> getAllMyCorporationDischargeMonitorings();
    
    /**
     * Obtiene monitoreos de descarga por descarga.
     * @param dischargeId el ID de la descarga
     * @return lista de monitoreos de la descarga
     */
    List<DischargeMonitoring> getDischargeMonitoringsByDischarge(Long dischargeId);
    
    /**
     * Obtiene monitoreos de descarga por estación de monitoreo.
     * @param monitoringStationId el ID de la estación de monitoreo
     * @return lista de monitoreos de la estación
     */
    List<DischargeMonitoring> getDischargeMonitoringsByMonitoringStation(Long monitoringStationId);
    
    /**
     * Obtiene monitoreos de descarga creados en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de monitoreos creados en el rango
     */
    List<DischargeMonitoring> getDischargeMonitoringsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Obtiene monitoreos de descarga con valores de OD en un rango específico.
     * @param minOd valor mínimo de OD
     * @param maxOd valor máximo de OD
     * @return lista de monitoreos con OD en el rango
     */
    List<DischargeMonitoring> getDischargeMonitoringsByOdRange(BigDecimal minOd, BigDecimal maxOd);
    
    /**
     * Obtiene monitoreos de descarga con valores de SST en un rango específico.
     * @param minSst valor mínimo de SST
     * @param maxSst valor máximo de SST
     * @return lista de monitoreos con SST en el rango
     */
    List<DischargeMonitoring> getDischargeMonitoringsBySstRange(BigDecimal minSst, BigDecimal maxSst);
    
    /**
     * Obtiene monitoreos de descarga con valores de DQO en un rango específico.
     * @param minDqo valor mínimo de DQO
     * @param maxDqo valor máximo de DQO
     * @return lista de monitoreos con DQO en el rango
     */
    List<DischargeMonitoring> getDischargeMonitoringsByDqoRange(BigDecimal minDqo, BigDecimal maxDqo);
    
    /**
     * Obtiene monitoreos de descarga con valores de PH en un rango específico.
     * @param minPh valor mínimo de PH
     * @param maxPh valor máximo de PH
     * @return lista de monitoreos con PH en el rango
     */
    List<DischargeMonitoring> getDischargeMonitoringsByPhRange(BigDecimal minPh, BigDecimal maxPh);
    
    /**
     * Cuenta el número de monitoreos de descarga de la corporación del usuario autenticado.
     * @return número de monitoreos de descarga
     */
    long countMyCorporationDischargeMonitorings();
    
    /**
     * Cuenta monitoreos de descarga por descarga.
     * @param dischargeId el ID de la descarga
     * @return número de monitoreos de la descarga
     */
    long countDischargeMonitoringsByDischarge(Long dischargeId);
    
    /**
     * Cuenta monitoreos de descarga por estación de monitoreo.
     * @param monitoringStationId el ID de la estación de monitoreo
     * @return número de monitoreos de la estación
     */
    long countDischargeMonitoringsByMonitoringStation(Long monitoringStationId);
    
    /**
     * Elimina un monitoreo de descarga.
     * @param id el ID del monitoreo de descarga a eliminar
     * @return true si se eliminó correctamente
     * @throws IllegalArgumentException si el monitoreo de descarga no existe
     */
    boolean deleteDischargeMonitoring(Long id);
    
    /**
     * Obtiene monitoreos de descarga ordenados por fecha de creación (más recientes primero).
     * @return lista de monitoreos de descarga ordenados por fecha de creación descendente
     */
    List<DischargeMonitoring> getDischargeMonitoringsOrderByCreatedAtDesc();
    
    /**
     * Obtiene monitoreos de descarga de la corporación ordenados por fecha de creación (más recientes primero).
     * @return lista de monitoreos de descarga de la corporación ordenados por fecha de creación descendente
     */
    List<DischargeMonitoring> getMyCorporationDischargeMonitoringsOrderByCreatedAtDesc();
    
    /**
     * Obtiene estadísticas de monitoreos de descarga de la corporación del usuario autenticado.
     * @return estadísticas de monitoreos de descarga
     */
    DischargeMonitoringStats getMyCorporationDischargeMonitoringStats();
    
    /**
     * Clase interna para estadísticas de monitoreos de descarga
     */
    class DischargeMonitoringStats {
        private long totalDischargeMonitorings;
        private long totalDischarges;
        private long totalMonitoringStations;
        private BigDecimal averageOd;
        private BigDecimal averageSst;
        private BigDecimal averageDqo;
        private BigDecimal averagePh;
        private BigDecimal averageCaudalVolumen;
        
        // Constructors
        public DischargeMonitoringStats() {}
        
        // Getters and Setters
        public long getTotalDischargeMonitorings() { return totalDischargeMonitorings; }
        public void setTotalDischargeMonitorings(long totalDischargeMonitorings) { this.totalDischargeMonitorings = totalDischargeMonitorings; }
        
        public long getTotalDischarges() { return totalDischarges; }
        public void setTotalDischarges(long totalDischarges) { this.totalDischarges = totalDischarges; }
        
        public long getTotalMonitoringStations() { return totalMonitoringStations; }
        public void setTotalMonitoringStations(long totalMonitoringStations) { this.totalMonitoringStations = totalMonitoringStations; }
        
        public BigDecimal getAverageOd() { return averageOd; }
        public void setAverageOd(BigDecimal averageOd) { this.averageOd = averageOd; }
        
        public BigDecimal getAverageSst() { return averageSst; }
        public void setAverageSst(BigDecimal averageSst) { this.averageSst = averageSst; }
        
        public BigDecimal getAverageDqo() { return averageDqo; }
        public void setAverageDqo(BigDecimal averageDqo) { this.averageDqo = averageDqo; }
        
        public BigDecimal getAveragePh() { return averagePh; }
        public void setAveragePh(BigDecimal averagePh) { this.averagePh = averagePh; }
        
        public BigDecimal getAverageCaudalVolumen() { return averageCaudalVolumen; }
        public void setAverageCaudalVolumen(BigDecimal averageCaudalVolumen) { this.averageCaudalVolumen = averageCaudalVolumen; }
    }
}

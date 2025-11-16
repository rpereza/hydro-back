package com.univercloud.hydro.service;

import com.univercloud.hydro.entity.Monitoring;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de Monitoreos.
 * Proporciona operaciones CRUD y lógica de negocio para monitoreos de estaciones.
 */
public interface MonitoringService {
    
    /**
     * Crea un nuevo monitoreo.
     * @param monitoring el monitoreo a crear
     * @return el monitoreo creado
     * @throws IllegalArgumentException si los datos son inválidos
     */
    Monitoring createMonitoring(Monitoring monitoring);
    
    /**
     * Actualiza un monitoreo existente.
     * @param monitoring el monitoreo a actualizar
     * @return el monitoreo actualizado
     * @throws IllegalArgumentException si el monitoreo no existe
     */
    Monitoring updateMonitoring(Monitoring monitoring);
    
    /**
     * Obtiene un monitoreo por su ID.
     * @param id el ID del monitoreo
     * @return el monitoreo, si existe
     */
    Optional<Monitoring> getMonitoringById(Long id);
    
    /**
     * Obtiene todos los monitoreos de la corporación del usuario autenticado.
     * @param pageable parámetros de paginación
     * @return página de monitoreos
     */
    Page<Monitoring> getMyCorporationMonitorings(Pageable pageable);
        
    /**
     * Obtiene monitoreos por estación de monitoreo con paginación.
     * @param monitoringStationId el ID de la estación de monitoreo
     * @param pageable parámetros de paginación
     * @return página de monitoreos de la estación
     */
    Page<Monitoring> getMonitoringsByStation(Long monitoringStationId, Pageable pageable);
    
    /**
     * Obtiene monitoreos por estación de monitoreo.
     * @param monitoringStationId el ID de la estación de monitoreo
     * @return lista de monitoreos de la estación
     */
    List<Monitoring> getMonitoringsByStation(Long monitoringStationId);
    
    /**
     * Obtiene monitoreos por fecha de monitoreo.
     * @param monitoringDate la fecha de monitoreo
     * @return lista de monitoreos de la fecha
     */
    List<Monitoring> getMonitoringsByDate(LocalDate monitoringDate);
    
    /**
     * Obtiene un monitoreo por estación y fecha.
     * @param monitoringStationId el ID de la estación de monitoreo
     * @param monitoringDate la fecha de monitoreo
     * @return el monitoreo si existe
     */
    Optional<Monitoring> getMonitoringByStationAndDate(Long monitoringStationId, LocalDate monitoringDate);
    
    /**
     * Obtiene monitoreos en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de monitoreos en el rango de fechas
     */
    List<Monitoring> getMonitoringsByDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * Obtiene monitoreos por estación en un rango de fechas.
     * @param monitoringStationId el ID de la estación de monitoreo
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de monitoreos de la estación en el rango
     */
    List<Monitoring> getMonitoringsByStationAndDateRange(Long monitoringStationId, LocalDate startDate, LocalDate endDate);
    
    /**
     * Obtiene el último monitoreo de una estación.
     * @param monitoringStationId el ID de la estación de monitoreo
     * @return el último monitoreo de la estación
     */
    Optional<Monitoring> getLatestMonitoringByStation(Long monitoringStationId);
        
    /**
     * Elimina un monitoreo.
     * @param id el ID del monitoreo a eliminar
     * @return true si se eliminó correctamente
     * @throws IllegalArgumentException si el monitoreo no existe
     */
    boolean deleteMonitoring(Long id);
    
    /**
     * Verifica si existe un monitoreo para la estación y fecha especificados.
     * @param monitoringStationId el ID de la estación de monitoreo
     * @param monitoringDate la fecha de monitoreo
     * @return true si existe, false en caso contrario
     */
    boolean existsByStationAndDate(Long monitoringStationId, LocalDate monitoringDate);
    
    /**
     * Obtiene estadísticas de monitoreos de la corporación del usuario autenticado.
     * @return estadísticas de monitoreos
     */
    MonitoringStats getMyCorporationMonitoringStats();
    
    /**
     * Clase interna para estadísticas de monitoreos
     */
    class MonitoringStats {
        private long totalMonitorings;
        private long monitoringsThisYear;
        private long monitoringsThisMonth;
        private long activeStations;
        private double averageWaterTemperature;
        private double averageAirTemperature;
        private double averagePh;
        private double averageOd;
        private double averageSst;
        private double averageDqo;
        
        // Constructors
        public MonitoringStats() {}
        
        // Getters and Setters
        public long getTotalMonitorings() { return totalMonitorings; }
        public void setTotalMonitorings(long totalMonitorings) { this.totalMonitorings = totalMonitorings; }
        
        public long getMonitoringsThisYear() { return monitoringsThisYear; }
        public void setMonitoringsThisYear(long monitoringsThisYear) { this.monitoringsThisYear = monitoringsThisYear; }
        
        public long getMonitoringsThisMonth() { return monitoringsThisMonth; }
        public void setMonitoringsThisMonth(long monitoringsThisMonth) { this.monitoringsThisMonth = monitoringsThisMonth; }
        
        public long getActiveStations() { return activeStations; }
        public void setActiveStations(long activeStations) { this.activeStations = activeStations; }
        
        public double getAverageWaterTemperature() { return averageWaterTemperature; }
        public void setAverageWaterTemperature(double averageWaterTemperature) { this.averageWaterTemperature = averageWaterTemperature; }
        
        public double getAverageAirTemperature() { return averageAirTemperature; }
        public void setAverageAirTemperature(double averageAirTemperature) { this.averageAirTemperature = averageAirTemperature; }
        
        public double getAveragePh() { return averagePh; }
        public void setAveragePh(double averagePh) { this.averagePh = averagePh; }
        
        public double getAverageOd() { return averageOd; }
        public void setAverageOd(double averageOd) { this.averageOd = averageOd; }
        
        public double getAverageSst() { return averageSst; }
        public void setAverageSst(double averageSst) { this.averageSst = averageSst; }
        
        public double getAverageDqo() { return averageDqo; }
        public void setAverageDqo(double averageDqo) { this.averageDqo = averageDqo; }
    }
}

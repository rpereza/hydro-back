package com.univercloud.hydro.service;

import com.univercloud.hydro.entity.MonitoringStation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de Estaciones de Monitoreo.
 * Proporciona operaciones CRUD y lógica de negocio para estaciones de monitoreo.
 */
public interface MonitoringStationService {
    
    /**
     * Crea una nueva estación de monitoreo.
     * @param monitoringStation la estación de monitoreo a crear
     * @return la estación de monitoreo creada
     * @throws IllegalArgumentException si los datos son inválidos
     */
    MonitoringStation createMonitoringStation(MonitoringStation monitoringStation);
    
    /**
     * Actualiza una estación de monitoreo existente.
     * @param monitoringStation la estación de monitoreo a actualizar
     * @return la estación de monitoreo actualizada
     * @throws IllegalArgumentException si la estación de monitoreo no existe
     */
    MonitoringStation updateMonitoringStation(MonitoringStation monitoringStation);
    
    /**
     * Obtiene una estación de monitoreo por su ID.
     * @param id el ID de la estación de monitoreo
     * @return la estación de monitoreo, si existe
     */
    Optional<MonitoringStation> getMonitoringStationById(Long id);
    
    /**
     * Obtiene todas las estaciones de monitoreo de la corporación del usuario autenticado.
     * @param pageable parámetros de paginación
     * @return página de estaciones de monitoreo
     */
    Page<MonitoringStation> getMyCorporationMonitoringStations(Pageable pageable);
    
    /**
     * Obtiene todas las estaciones de monitoreo de la corporación del usuario autenticado.
     * @return lista de estaciones de monitoreo
     */
    List<MonitoringStation> getAllMyCorporationMonitoringStations();
    
    /**
     * Obtiene estaciones de monitoreo activas de la corporación del usuario autenticado.
     * @return lista de estaciones de monitoreo activas
     */
    List<MonitoringStation> getActiveMyCorporationMonitoringStations();
    
    /**
     * Obtiene todas las estaciones de monitoreo activas.
     * @return lista de estaciones de monitoreo activas
     */
    List<MonitoringStation> getAllActiveMonitoringStations();
    
    /**
     * Obtiene todas las estaciones de monitoreo inactivas.
     * @return lista de estaciones de monitoreo inactivas
     */
    List<MonitoringStation> getAllInactiveMonitoringStations();
    
    /**
     * Busca una estación de monitoreo por nombre.
     * @param name el nombre de la estación de monitoreo
     * @return la estación de monitoreo si existe
     */
    Optional<MonitoringStation> getMonitoringStationByName(String name);
    
    /**
     * Busca estaciones de monitoreo por nombre (búsqueda parcial).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de estaciones de monitoreo que coinciden
     */
    List<MonitoringStation> searchMonitoringStationsByName(String name);
    
    /**
     * Busca estaciones de monitoreo activas por nombre (búsqueda parcial).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de estaciones de monitoreo activas que coinciden
     */
    List<MonitoringStation> searchActiveMonitoringStationsByName(String name);
    
    /**
     * Busca estaciones de monitoreo por corporación y nombre (búsqueda parcial).
     * @param corporationId el ID de la corporación
     * @param name el nombre o parte del nombre a buscar
     * @return lista de estaciones de la corporación que coinciden
     */
    List<MonitoringStation> searchMonitoringStationsByCorporationAndName(Long corporationId, String name);
    
    /**
     * Obtiene estaciones de monitoreo creadas en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de estaciones de monitoreo creadas en el rango
     */
    List<MonitoringStation> getMonitoringStationsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Cuenta el número de estaciones de monitoreo de la corporación del usuario autenticado.
     * @return número de estaciones de monitoreo
     */
    long countMyCorporationMonitoringStations();
    
    /**
     * Cuenta estaciones de monitoreo activas de la corporación del usuario autenticado.
     * @return número de estaciones de monitoreo activas
     */
    long countActiveMyCorporationMonitoringStations();
    
    /**
     * Activa una estación de monitoreo.
     * @param id el ID de la estación de monitoreo a activar
     * @return true si se activó correctamente
     * @throws IllegalArgumentException si la estación de monitoreo no existe
     */
    boolean activateMonitoringStation(Long id);
    
    /**
     * Desactiva una estación de monitoreo.
     * @param id el ID de la estación de monitoreo a desactivar
     * @return true si se desactivó correctamente
     * @throws IllegalArgumentException si la estación de monitoreo no existe
     */
    boolean deactivateMonitoringStation(Long id);
    
    /**
     * Elimina una estación de monitoreo.
     * @param id el ID de la estación de monitoreo a eliminar
     * @return true si se eliminó correctamente
     * @throws IllegalArgumentException si la estación de monitoreo no existe
     */
    boolean deleteMonitoringStation(Long id);
    
    /**
     * Verifica si existe una estación de monitoreo con el nombre especificado.
     * @param name el nombre de la estación de monitoreo
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);
    
    /**
     * Obtiene estaciones de monitoreo ordenadas por nombre.
     * @return lista de estaciones de monitoreo ordenadas por nombre
     */
    List<MonitoringStation> getMonitoringStationsOrderByName();
    
    /**
     * Obtiene estaciones de monitoreo activas ordenadas por nombre.
     * @return lista de estaciones de monitoreo activas ordenadas por nombre
     */
    List<MonitoringStation> getActiveMonitoringStationsOrderByName();
    
    /**
     * Obtiene estaciones de monitoreo de la corporación ordenadas por nombre.
     * @return lista de estaciones de monitoreo de la corporación ordenadas por nombre
     */
    List<MonitoringStation> getMyCorporationMonitoringStationsOrderByName();
    
    /**
     * Obtiene estaciones de monitoreo activas de la corporación ordenadas por nombre.
     * @return lista de estaciones de monitoreo activas de la corporación ordenadas por nombre
     */
    List<MonitoringStation> getActiveMyCorporationMonitoringStationsOrderByName();
    
    /**
     * Obtiene estaciones de monitoreo ordenadas por fecha de creación (más recientes primero).
     * @return lista de estaciones de monitoreo ordenadas por fecha de creación descendente
     */
    List<MonitoringStation> getMonitoringStationsOrderByCreatedAtDesc();
    
    /**
     * Obtiene estadísticas de estaciones de monitoreo de la corporación del usuario autenticado.
     * @return estadísticas de estaciones de monitoreo
     */
    MonitoringStationStats getMyCorporationMonitoringStationStats();
    
    /**
     * Clase interna para estadísticas de estaciones de monitoreo
     */
    class MonitoringStationStats {
        private long totalMonitoringStations;
        private long activeMonitoringStations;
        private long inactiveMonitoringStations;
        private long totalMonitorings;
        private long monitoringsThisYear;
        private long monitoringsThisMonth;
        private double averageMonitoringsPerStation;
        private long stationsWithRecentMonitorings;
        
        // Constructors
        public MonitoringStationStats() {}
        
        // Getters and Setters
        public long getTotalMonitoringStations() { return totalMonitoringStations; }
        public void setTotalMonitoringStations(long totalMonitoringStations) { this.totalMonitoringStations = totalMonitoringStations; }
        
        public long getActiveMonitoringStations() { return activeMonitoringStations; }
        public void setActiveMonitoringStations(long activeMonitoringStations) { this.activeMonitoringStations = activeMonitoringStations; }
        
        public long getInactiveMonitoringStations() { return inactiveMonitoringStations; }
        public void setInactiveMonitoringStations(long inactiveMonitoringStations) { this.inactiveMonitoringStations = inactiveMonitoringStations; }
        
        public long getTotalMonitorings() { return totalMonitorings; }
        public void setTotalMonitorings(long totalMonitorings) { this.totalMonitorings = totalMonitorings; }
        
        public long getMonitoringsThisYear() { return monitoringsThisYear; }
        public void setMonitoringsThisYear(long monitoringsThisYear) { this.monitoringsThisYear = monitoringsThisYear; }
        
        public long getMonitoringsThisMonth() { return monitoringsThisMonth; }
        public void setMonitoringsThisMonth(long monitoringsThisMonth) { this.monitoringsThisMonth = monitoringsThisMonth; }
        
        public double getAverageMonitoringsPerStation() { return averageMonitoringsPerStation; }
        public void setAverageMonitoringsPerStation(double averageMonitoringsPerStation) { this.averageMonitoringsPerStation = averageMonitoringsPerStation; }
        
        public long getStationsWithRecentMonitorings() { return stationsWithRecentMonitorings; }
        public void setStationsWithRecentMonitorings(long stationsWithRecentMonitorings) { this.stationsWithRecentMonitorings = stationsWithRecentMonitorings; }
    }
}

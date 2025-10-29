package com.univercloud.hydro.service;

import com.univercloud.hydro.entity.WaterBasin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de Cuencas Hidrográficas.
 * Proporciona operaciones CRUD y lógica de negocio para cuencas hidrográficas.
 */
public interface WaterBasinService {
    
    /**
     * Crea una nueva cuenca hidrográfica.
     * @param waterBasin la cuenca hidrográfica a crear
     * @return la cuenca hidrográfica creada
     * @throws IllegalArgumentException si los datos son inválidos
     */
    WaterBasin createWaterBasin(WaterBasin waterBasin);
    
    /**
     * Actualiza una cuenca hidrográfica existente.
     * @param waterBasin la cuenca hidrográfica a actualizar
     * @return la cuenca hidrográfica actualizada
     * @throws IllegalArgumentException si la cuenca hidrográfica no existe
     */
    WaterBasin updateWaterBasin(WaterBasin waterBasin);
    
    /**
     * Obtiene una cuenca hidrográfica por su ID.
     * @param id el ID de la cuenca hidrográfica
     * @return la cuenca hidrográfica, si existe
     */
    Optional<WaterBasin> getWaterBasinById(Long id);
    
    /**
     * Obtiene todas las cuencas hidrográficas de la corporación del usuario autenticado.
     * @param pageable parámetros de paginación
     * @return página de cuencas hidrográficas
     */
    Page<WaterBasin> getMyCorporationWaterBasins(Pageable pageable);
    
    /**
     * Obtiene todas las cuencas hidrográficas de la corporación del usuario autenticado.
     * @return lista de cuencas hidrográficas
     */
    List<WaterBasin> getAllMyCorporationWaterBasins();
    
    /**
     * Obtiene todas las cuencas hidrográficas activas de la corporación del usuario autenticado.
     * @return lista de cuencas hidrográficas activas
     */
    List<WaterBasin> getActiveMyCorporationWaterBasins();
    
    /**
     * Obtiene todas las cuencas hidrográficas activas.
     * @return lista de cuencas hidrográficas activas
     */
    List<WaterBasin> getAllActiveWaterBasins();
    
    /**
     * Obtiene todas las cuencas hidrográficas inactivas.
     * @return lista de cuencas hidrográficas inactivas
     */
    List<WaterBasin> getAllInactiveWaterBasins();
    
    /**
     * Busca una cuenca hidrográfica por nombre.
     * @param name el nombre de la cuenca hidrográfica
     * @return la cuenca hidrográfica si existe
     */
    Optional<WaterBasin> getWaterBasinByName(String name);
    
    /**
     * Busca cuencas hidrográficas por nombre (búsqueda parcial).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de cuencas hidrográficas que coinciden
     */
    List<WaterBasin> searchWaterBasinsByName(String name);
    
    /**
     * Busca cuencas hidrográficas activas por nombre (búsqueda parcial).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de cuencas hidrográficas activas que coinciden
     */
    List<WaterBasin> searchActiveWaterBasinsByName(String name);
    
    /**
     * Obtiene cuencas hidrográficas creadas en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de cuencas hidrográficas creadas en el rango
     */
    List<WaterBasin> getWaterBasinsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Cuenta el número de cuencas hidrográficas de la corporación del usuario autenticado.
     * @return número de cuencas hidrográficas
     */
    long countMyCorporationWaterBasins();
    
    /**
     * Cuenta cuencas hidrográficas activas de la corporación del usuario autenticado.
     * @return número de cuencas hidrográficas activas
     */
    long countActiveMyCorporationWaterBasins();
    
    /**
     * Activa una cuenca hidrográfica.
     * @param id el ID de la cuenca hidrográfica a activar
     * @return true si se activó correctamente
     * @throws IllegalArgumentException si la cuenca hidrográfica no existe
     */
    boolean activateWaterBasin(Long id);
    
    /**
     * Desactiva una cuenca hidrográfica.
     * @param id el ID de la cuenca hidrográfica a desactivar
     * @return true si se desactivó correctamente
     * @throws IllegalArgumentException si la cuenca hidrográfica no existe
     */
    boolean deactivateWaterBasin(Long id);
    
    /**
     * Elimina una cuenca hidrográfica.
     * @param id el ID de la cuenca hidrográfica a eliminar
     * @return true si se eliminó correctamente
     * @throws IllegalArgumentException si la cuenca hidrográfica no existe
     */
    boolean deleteWaterBasin(Long id);
    
    /**
     * Verifica si existe una cuenca hidrográfica con el nombre especificado.
     * @param name el nombre de la cuenca hidrográfica
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);
    
    /**
     * Obtiene cuencas hidrográficas ordenadas por fecha de creación (más recientes primero).
     * @return lista de cuencas hidrográficas ordenadas por fecha de creación descendente
     */
    List<WaterBasin> getWaterBasinsOrderByCreatedAtDesc();
    
    /**
     * Obtiene cuencas hidrográficas activas ordenadas por nombre.
     * @return lista de cuencas hidrográficas activas ordenadas por nombre
     */
    List<WaterBasin> getActiveWaterBasinsOrderByName();
    
    /**
     * Obtiene cuencas hidrográficas de la corporación ordenadas por nombre.
     * @return lista de cuencas hidrográficas de la corporación ordenadas por nombre
     */
    List<WaterBasin> getMyCorporationWaterBasinsOrderByName();
    
    /**
     * Obtiene cuencas hidrográficas activas de la corporación ordenadas por nombre.
     * @return lista de cuencas hidrográficas activas de la corporación ordenadas por nombre
     */
    List<WaterBasin> getActiveMyCorporationWaterBasinsOrderByName();
    
    /**
     * Obtiene estadísticas de cuencas hidrográficas de la corporación del usuario autenticado.
     * @return estadísticas de cuencas hidrográficas
     */
    WaterBasinStats getMyCorporationWaterBasinStats();
    
    /**
     * Clase interna para estadísticas de cuencas hidrográficas
     */
    class WaterBasinStats {
        private long totalWaterBasins;
        private long activeWaterBasins;
        private long inactiveWaterBasins;
        private long totalBasinSections;
        private long activeBasinSections;
        private long totalDischarges;
        private long totalMonitorings;
        
        // Constructors
        public WaterBasinStats() {}
        
        // Getters and Setters
        public long getTotalWaterBasins() { return totalWaterBasins; }
        public void setTotalWaterBasins(long totalWaterBasins) { this.totalWaterBasins = totalWaterBasins; }
        
        public long getActiveWaterBasins() { return activeWaterBasins; }
        public void setActiveWaterBasins(long activeWaterBasins) { this.activeWaterBasins = activeWaterBasins; }
        
        public long getInactiveWaterBasins() { return inactiveWaterBasins; }
        public void setInactiveWaterBasins(long inactiveWaterBasins) { this.inactiveWaterBasins = inactiveWaterBasins; }
        
        public long getTotalBasinSections() { return totalBasinSections; }
        public void setTotalBasinSections(long totalBasinSections) { this.totalBasinSections = totalBasinSections; }
        
        public long getActiveBasinSections() { return activeBasinSections; }
        public void setActiveBasinSections(long activeBasinSections) { this.activeBasinSections = activeBasinSections; }
        
        public long getTotalDischarges() { return totalDischarges; }
        public void setTotalDischarges(long totalDischarges) { this.totalDischarges = totalDischarges; }
        
        public long getTotalMonitorings() { return totalMonitorings; }
        public void setTotalMonitorings(long totalMonitorings) { this.totalMonitorings = totalMonitorings; }
    }
}

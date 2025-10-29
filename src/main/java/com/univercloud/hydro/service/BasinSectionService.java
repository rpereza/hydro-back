package com.univercloud.hydro.service;

import com.univercloud.hydro.entity.BasinSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de Secciones de Cuenca.
 * Proporciona operaciones CRUD y lógica de negocio para secciones de cuenca.
 */
public interface BasinSectionService {
    
    /**
     * Crea una nueva sección de cuenca.
     * @param basinSection la sección de cuenca a crear
     * @return la sección de cuenca creada
     * @throws IllegalArgumentException si los datos son inválidos
     */
    BasinSection createBasinSection(BasinSection basinSection);
    
    /**
     * Actualiza una sección de cuenca existente.
     * @param basinSection la sección de cuenca a actualizar
     * @return la sección de cuenca actualizada
     * @throws IllegalArgumentException si la sección de cuenca no existe
     */
    BasinSection updateBasinSection(BasinSection basinSection);
    
    /**
     * Obtiene una sección de cuenca por su ID.
     * @param id el ID de la sección de cuenca
     * @return la sección de cuenca, si existe
     */
    Optional<BasinSection> getBasinSectionById(Long id);
    
    /**
     * Obtiene todas las secciones de cuenca de la corporación del usuario autenticado.
     * @param pageable parámetros de paginación
     * @return página de secciones de cuenca
     */
    Page<BasinSection> getMyCorporationBasinSections(Pageable pageable);
    
    /**
     * Obtiene todas las secciones de cuenca de la corporación del usuario autenticado.
     * @return lista de secciones de cuenca
     */
    List<BasinSection> getAllMyCorporationBasinSections();
    
    /**
     * Obtiene secciones de cuenca por cuenca hidrográfica.
     * @param waterBasinId el ID de la cuenca hidrográfica
     * @return lista de secciones de la cuenca
     */
    List<BasinSection> getBasinSectionsByWaterBasin(Long waterBasinId);
    
    /**
     * Obtiene secciones de cuenca activas por cuenca hidrográfica.
     * @param waterBasinId el ID de la cuenca hidrográfica
     * @return lista de secciones activas de la cuenca
     */
    List<BasinSection> getActiveBasinSectionsByWaterBasin(Long waterBasinId);
    
    /**
     * Obtiene todas las secciones de cuenca activas de la corporación del usuario autenticado.
     * @return lista de secciones de cuenca activas
     */
    List<BasinSection> getActiveMyCorporationBasinSections();
    
    /**
     * Obtiene todas las secciones de cuenca activas.
     * @return lista de secciones de cuenca activas
     */
    List<BasinSection> getAllActiveBasinSections();
    
    /**
     * Obtiene todas las secciones de cuenca inactivas.
     * @return lista de secciones de cuenca inactivas
     */
    List<BasinSection> getAllInactiveBasinSections();
    
    /**
     * Busca una sección de cuenca por nombre.
     * @param name el nombre de la sección de cuenca
     * @return la sección de cuenca si existe
     */
    Optional<BasinSection> getBasinSectionByName(String name);
    
    /**
     * Busca secciones de cuenca por nombre (búsqueda parcial).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de secciones de cuenca que coinciden
     */
    List<BasinSection> searchBasinSectionsByName(String name);
    
    /**
     * Busca secciones de cuenca por cuenca y nombre (búsqueda parcial).
     * @param waterBasinId el ID de la cuenca hidrográfica
     * @param name el nombre o parte del nombre a buscar
     * @return lista de secciones de la cuenca que coinciden
     */
    List<BasinSection> searchBasinSectionsByWaterBasinAndName(Long waterBasinId, String name);
    
    /**
     * Busca secciones de cuenca activas por nombre (búsqueda parcial).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de secciones de cuenca activas que coinciden
     */
    List<BasinSection> searchActiveBasinSectionsByName(String name);
    
    /**
     * Obtiene secciones de cuenca creadas en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de secciones de cuenca creadas en el rango
     */
    List<BasinSection> getBasinSectionsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Cuenta secciones de cuenca por cuenca hidrográfica.
     * @param waterBasinId el ID de la cuenca hidrográfica
     * @return número de secciones de la cuenca
     */
    long countBasinSectionsByWaterBasin(Long waterBasinId);
    
    /**
     * Cuenta secciones de cuenca activas por cuenca hidrográfica.
     * @param waterBasinId el ID de la cuenca hidrográfica
     * @return número de secciones activas de la cuenca
     */
    long countActiveBasinSectionsByWaterBasin(Long waterBasinId);
    
    /**
     * Cuenta el número de secciones de cuenca de la corporación del usuario autenticado.
     * @return número de secciones de cuenca
     */
    long countMyCorporationBasinSections();
    
    /**
     * Activa una sección de cuenca.
     * @param id el ID de la sección de cuenca a activar
     * @return true si se activó correctamente
     * @throws IllegalArgumentException si la sección de cuenca no existe
     */
    boolean activateBasinSection(Long id);
    
    /**
     * Desactiva una sección de cuenca.
     * @param id el ID de la sección de cuenca a desactivar
     * @return true si se desactivó correctamente
     * @throws IllegalArgumentException si la sección de cuenca no existe
     */
    boolean deactivateBasinSection(Long id);
    
    /**
     * Elimina una sección de cuenca.
     * @param id el ID de la sección de cuenca a eliminar
     * @return true si se eliminó correctamente
     * @throws IllegalArgumentException si la sección de cuenca no existe
     */
    boolean deleteBasinSection(Long id);
    
    /**
     * Verifica si existe una sección de cuenca con el nombre especificado.
     * @param name el nombre de la sección de cuenca
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);
    
    /**
     * Obtiene secciones de cuenca ordenadas por nombre.
     * @return lista de secciones de cuenca ordenadas por nombre
     */
    List<BasinSection> getBasinSectionsOrderByName();
    
    /**
     * Obtiene secciones de cuenca por cuenca hidrográfica ordenadas por nombre.
     * @param waterBasinId el ID de la cuenca hidrográfica
     * @return lista de secciones de la cuenca ordenadas por nombre
     */
    List<BasinSection> getBasinSectionsByWaterBasinOrderByName(Long waterBasinId);
    
    /**
     * Obtiene secciones de cuenca activas ordenadas por nombre.
     * @return lista de secciones de cuenca activas ordenadas por nombre
     */
    List<BasinSection> getActiveBasinSectionsOrderByName();
    
    /**
     * Obtiene secciones de cuenca de la corporación ordenadas por nombre.
     * @return lista de secciones de cuenca de la corporación ordenadas por nombre
     */
    List<BasinSection> getMyCorporationBasinSectionsOrderByName();
    
    /**
     * Obtiene secciones de cuenca ordenadas por fecha de creación (más recientes primero).
     * @return lista de secciones de cuenca ordenadas por fecha de creación descendente
     */
    List<BasinSection> getBasinSectionsOrderByCreatedAtDesc();
    
    /**
     * Obtiene estadísticas de secciones de cuenca de la corporación del usuario autenticado.
     * @return estadísticas de secciones de cuenca
     */
    BasinSectionStats getMyCorporationBasinSectionStats();
    
    /**
     * Clase interna para estadísticas de secciones de cuenca
     */
    class BasinSectionStats {
        private long totalBasinSections;
        private long activeBasinSections;
        private long inactiveBasinSections;
        private long totalWaterBasins;
        private long totalDischarges;
        private long totalMonitorings;
        private long averageSectionsPerBasin;
        
        // Constructors
        public BasinSectionStats() {}
        
        // Getters and Setters
        public long getTotalBasinSections() { return totalBasinSections; }
        public void setTotalBasinSections(long totalBasinSections) { this.totalBasinSections = totalBasinSections; }
        
        public long getActiveBasinSections() { return activeBasinSections; }
        public void setActiveBasinSections(long activeBasinSections) { this.activeBasinSections = activeBasinSections; }
        
        public long getInactiveBasinSections() { return inactiveBasinSections; }
        public void setInactiveBasinSections(long inactiveBasinSections) { this.inactiveBasinSections = inactiveBasinSections; }
        
        public long getTotalWaterBasins() { return totalWaterBasins; }
        public void setTotalWaterBasins(long totalWaterBasins) { this.totalWaterBasins = totalWaterBasins; }
        
        public long getTotalDischarges() { return totalDischarges; }
        public void setTotalDischarges(long totalDischarges) { this.totalDischarges = totalDischarges; }
        
        public long getTotalMonitorings() { return totalMonitorings; }
        public void setTotalMonitorings(long totalMonitorings) { this.totalMonitorings = totalMonitorings; }
        
        public long getAverageSectionsPerBasin() { return averageSectionsPerBasin; }
        public void setAverageSectionsPerBasin(long averageSectionsPerBasin) { this.averageSectionsPerBasin = averageSectionsPerBasin; }
    }
}

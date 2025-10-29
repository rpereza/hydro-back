package com.univercloud.hydro.service;

import com.univercloud.hydro.entity.Discharge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de Descargas.
 * Proporciona operaciones CRUD y lógica de negocio para descargas de agua.
 */
public interface DischargeService {
    
    /**
     * Crea una nueva descarga.
     * @param discharge la descarga a crear
     * @return la descarga creada
     * @throws IllegalArgumentException si los datos son inválidos
     */
    Discharge createDischarge(Discharge discharge);
    
    /**
     * Actualiza una descarga existente.
     * @param discharge la descarga a actualizar
     * @return la descarga actualizada
     * @throws IllegalArgumentException si la descarga no existe
     */
    Discharge updateDischarge(Discharge discharge);
    
    /**
     * Obtiene una descarga por su ID.
     * @param id el ID de la descarga
     * @return la descarga, si existe
     */
    Optional<Discharge> getDischargeById(Long id);
    
    /**
     * Obtiene todas las descargas de la corporación del usuario autenticado.
     * @param pageable parámetros de paginación
     * @return página de descargas
     */
    Page<Discharge> getMyCorporationDischarges(Pageable pageable);
    
    /**
     * Obtiene todas las descargas de la corporación del usuario autenticado.
     * @return lista de descargas
     */
    List<Discharge> getAllMyCorporationDischarges();
    
    /**
     * Obtiene descargas por usuario de descarga.
     * @param dischargeUserId el ID del usuario de descarga
     * @return lista de descargas del usuario
     */
    List<Discharge> getDischargesByDischargeUser(Long dischargeUserId);
    
    /**
     * Obtiene descargas por municipio.
     * @param municipalityId el ID del municipio
     * @return lista de descargas del municipio
     */
    List<Discharge> getDischargesByMunicipality(Long municipalityId);
    
    /**
     * Obtiene descargas por sección de cuenca.
     * @param basinSectionId el ID de la sección de cuenca
     * @return lista de descargas de la sección
     */
    List<Discharge> getDischargesByBasinSection(Long basinSectionId);
    
    /**
     * Obtiene descargas por año.
     * @param year el año
     * @return lista de descargas del año
     */
    List<Discharge> getDischargesByYear(Integer year);
    
    /**
     * Obtiene descargas por tipo de descarga.
     * @param dischargeType el tipo de descarga
     * @return lista de descargas del tipo especificado
     */
    List<Discharge> getDischargesByType(Discharge.DischargeType dischargeType);
    
    /**
     * Obtiene descargas por tipo de recurso hídrico.
     * @param waterResourceType el tipo de recurso hídrico
     * @return lista de descargas del tipo de recurso
     */
    List<Discharge> getDischargesByWaterResourceType(Discharge.WaterResourceType waterResourceType);
    
    /**
     * Busca una descarga por número y año.
     * @param number el número de descarga
     * @param year el año
     * @return la descarga si existe
     */
    Optional<Discharge> getDischargeByNumberAndYear(Integer number, Integer year);
    
    /**
     * Busca descargas por nombre (búsqueda parcial).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de descargas que coinciden
     */
    List<Discharge> searchDischargesByName(String name);
    
    /**
     * Obtiene descargas que están siendo monitoreadas.
     * @return lista de descargas monitoreadas
     */
    List<Discharge> getMonitoredDischarges();
    
    /**
     * Obtiene descargas que no están siendo monitoreadas.
     * @return lista de descargas no monitoreadas
     */
    List<Discharge> getUnmonitoredDischarges();
    
    /**
     * Obtiene descargas con reúso de cuenca.
     * @return lista de descargas con reúso de cuenca
     */
    List<Discharge> getDischargesWithBasinReuse();
    
    /**
     * Obtiene descargas sin reúso de cuenca.
     * @return lista de descargas sin reúso de cuenca
     */
    List<Discharge> getDischargesWithoutBasinReuse();
    
    /**
     * Obtiene descargas creadas en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de descargas creadas en el rango
     */
    List<Discharge> getDischargesByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Cuenta el número de descargas de la corporación del usuario autenticado.
     * @return número de descargas
     */
    long countMyCorporationDischarges();
    
    /**
     * Cuenta descargas por municipio.
     * @param municipalityId el ID del municipio
     * @return número de descargas del municipio
     */
    long countDischargesByMunicipality(Long municipalityId);
    
    /**
     * Cuenta descargas por sección de cuenca.
     * @param basinSectionId el ID de la sección de cuenca
     * @return número de descargas de la sección
     */
    long countDischargesByBasinSection(Long basinSectionId);
    
    /**
     * Cuenta descargas por año.
     * @param year el año
     * @return número de descargas del año
     */
    long countDischargesByYear(Integer year);
    
    /**
     * Elimina una descarga.
     * @param id el ID de la descarga a eliminar
     * @return true si se eliminó correctamente
     * @throws IllegalArgumentException si la descarga no existe
     */
    boolean deleteDischarge(Long id);
    
    /**
     * Verifica si existe una descarga con el número y año especificados.
     * @param number el número de descarga
     * @param year el año
     * @return true si existe, false en caso contrario
     */
    boolean existsByNumberAndYear(Integer number, Integer year);
    
    /**
     * Obtiene descargas ordenadas por fecha de creación (más recientes primero).
     * @return lista de descargas ordenadas por fecha de creación descendente
     */
    List<Discharge> getDischargesOrderByCreatedAtDesc();
    
    /**
     * Obtiene descargas por municipio y año.
     * @param municipalityId el ID del municipio
     * @param year el año
     * @return lista de descargas del municipio y año
     */
    List<Discharge> getDischargesByMunicipalityAndYear(Long municipalityId, Integer year);
    
    /**
     * Obtiene descargas por sección de cuenca y año.
     * @param basinSectionId el ID de la sección de cuenca
     * @param year el año
     * @return lista de descargas de la sección y año
     */
    List<Discharge> getDischargesByBasinSectionAndYear(Long basinSectionId, Integer year);
    
    /**
     * Obtiene estadísticas de descargas de la corporación del usuario autenticado.
     * @return estadísticas de descargas
     */
    DischargeStats getMyCorporationDischargeStats();
    
    /**
     * Clase interna para estadísticas de descargas
     */
    class DischargeStats {
        private long totalDischarges;
        private long monitoredDischarges;
        private long unmonitoredDischarges;
        private long dischargesWithBasinReuse;
        private long dischargesWithoutBasinReuse;
        private long domesticDischarges;
        private long nonDomesticDischarges;
        private long riverDischarges;
        private long lakeDischarges;
        
        // Constructors
        public DischargeStats() {}
        
        // Getters and Setters
        public long getTotalDischarges() { return totalDischarges; }
        public void setTotalDischarges(long totalDischarges) { this.totalDischarges = totalDischarges; }
        
        public long getMonitoredDischarges() { return monitoredDischarges; }
        public void setMonitoredDischarges(long monitoredDischarges) { this.monitoredDischarges = monitoredDischarges; }
        
        public long getUnmonitoredDischarges() { return unmonitoredDischarges; }
        public void setUnmonitoredDischarges(long unmonitoredDischarges) { this.unmonitoredDischarges = unmonitoredDischarges; }
        
        public long getDischargesWithBasinReuse() { return dischargesWithBasinReuse; }
        public void setDischargesWithBasinReuse(long dischargesWithBasinReuse) { this.dischargesWithBasinReuse = dischargesWithBasinReuse; }
        
        public long getDischargesWithoutBasinReuse() { return dischargesWithoutBasinReuse; }
        public void setDischargesWithoutBasinReuse(long dischargesWithoutBasinReuse) { this.dischargesWithoutBasinReuse = dischargesWithoutBasinReuse; }
        
        public long getDomesticDischarges() { return domesticDischarges; }
        public void setDomesticDischarges(long domesticDischarges) { this.domesticDischarges = domesticDischarges; }
        
        public long getNonDomesticDischarges() { return nonDomesticDischarges; }
        public void setNonDomesticDischarges(long nonDomesticDischarges) { this.nonDomesticDischarges = nonDomesticDischarges; }
        
        public long getRiverDischarges() { return riverDischarges; }
        public void setRiverDischarges(long riverDischarges) { this.riverDischarges = riverDischarges; }
        
        public long getLakeDischarges() { return lakeDischarges; }
        public void setLakeDischarges(long lakeDischarges) { this.lakeDischarges = lakeDischarges; }
    }
}

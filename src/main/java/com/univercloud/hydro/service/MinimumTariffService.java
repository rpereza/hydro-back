package com.univercloud.hydro.service;

import com.univercloud.hydro.entity.MinimumTariff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de Tarifas Mínimas.
 * Proporciona operaciones CRUD y lógica de negocio para tarifas mínimas.
 */
public interface MinimumTariffService {
    
    /**
     * Crea una nueva tarifa mínima.
     * @param minimumTariff la tarifa mínima a crear
     * @return la tarifa mínima creada
     * @throws IllegalArgumentException si los datos son inválidos
     */
    MinimumTariff createMinimumTariff(MinimumTariff minimumTariff);
    
    /**
     * Actualiza una tarifa mínima existente.
     * @param minimumTariff la tarifa mínima a actualizar
     * @return la tarifa mínima actualizada
     * @throws IllegalArgumentException si la tarifa mínima no existe
     */
    MinimumTariff updateMinimumTariff(MinimumTariff minimumTariff);
    
    /**
     * Obtiene una tarifa mínima por su ID.
     * @param id el ID de la tarifa mínima
     * @return la tarifa mínima, si existe
     */
    Optional<MinimumTariff> getMinimumTariffById(Long id);
    
    /**
     * Obtiene todas las tarifas mínimas de la corporación del usuario autenticado.
     * @param pageable parámetros de paginación
     * @return página de tarifas mínimas
     */
    Page<MinimumTariff> getMyCorporationMinimumTariffs(Pageable pageable);
    
    /**
     * Obtiene todas las tarifas mínimas de la corporación del usuario autenticado.
     * @return lista de tarifas mínimas
     */
    List<MinimumTariff> getAllMyCorporationMinimumTariffs();
    
    /**
     * Obtiene todas las tarifas mínimas activas de la corporación del usuario autenticado.
     * @return lista de tarifas mínimas activas
     */
    List<MinimumTariff> getActiveMyCorporationMinimumTariffs();
    
    /**
     * Obtiene todas las tarifas mínimas activas.
     * @return lista de tarifas mínimas activas
     */
    List<MinimumTariff> getAllActiveMinimumTariffs();
    
    /**
     * Obtiene todas las tarifas mínimas inactivas.
     * @return lista de tarifas mínimas inactivas
     */
    List<MinimumTariff> getAllInactiveMinimumTariffs();
    
    /**
     * Obtiene tarifas mínimas por año.
     * @param year el año
     * @return lista de tarifas mínimas del año
     */
    List<MinimumTariff> getMinimumTariffsByYear(Integer year);
    
    /**
     * Obtiene tarifas mínimas activas por año.
     * @param year el año
     * @return lista de tarifas mínimas activas del año
     */
    List<MinimumTariff> getActiveMinimumTariffsByYear(Integer year);
    
    /**
     * Obtiene tarifas mínimas de la corporación por año.
     * @param year el año
     * @return lista de tarifas mínimas de la corporación del año
     */
    List<MinimumTariff> getMyCorporationMinimumTariffsByYear(Integer year);
    
    /**
     * Obtiene tarifas mínimas activas de la corporación por año.
     * @param year el año
     * @return lista de tarifas mínimas activas de la corporación del año
     */
    List<MinimumTariff> getActiveMyCorporationMinimumTariffsByYear(Integer year);
    
    /**
     * Obtiene tarifas mínimas creadas en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de tarifas mínimas creadas en el rango
     */
    List<MinimumTariff> getMinimumTariffsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Obtiene tarifas mínimas con valores de DBO en un rango específico.
     * @param minDboValue valor mínimo de DBO
     * @param maxDboValue valor máximo de DBO
     * @return lista de tarifas mínimas con DBO en el rango
     */
    List<MinimumTariff> getMinimumTariffsByDboValueRange(BigDecimal minDboValue, BigDecimal maxDboValue);
    
    /**
     * Obtiene tarifas mínimas con valores de SST en un rango específico.
     * @param minSstValue valor mínimo de SST
     * @param maxSstValue valor máximo de SST
     * @return lista de tarifas mínimas con SST en el rango
     */
    List<MinimumTariff> getMinimumTariffsBySstValueRange(BigDecimal minSstValue, BigDecimal maxSstValue);
    
    /**
     * Obtiene tarifas mínimas con valores de IPC en un rango específico.
     * @param minIpcValue valor mínimo de IPC
     * @param maxIpcValue valor máximo de IPC
     * @return lista de tarifas mínimas con IPC en el rango
     */
    List<MinimumTariff> getMinimumTariffsByIpcValueRange(BigDecimal minIpcValue, BigDecimal maxIpcValue);
    
    /**
     * Cuenta el número de tarifas mínimas de la corporación del usuario autenticado.
     * @return número de tarifas mínimas
     */
    long countMyCorporationMinimumTariffs();
    
    /**
     * Cuenta tarifas mínimas activas de la corporación del usuario autenticado.
     * @return número de tarifas mínimas activas
     */
    long countActiveMyCorporationMinimumTariffs();
    
    /**
     * Cuenta tarifas mínimas por año.
     * @param year el año
     * @return número de tarifas mínimas del año
     */
    long countMinimumTariffsByYear(Integer year);
    
    /**
     * Activa una tarifa mínima.
     * @param id el ID de la tarifa mínima a activar
     * @return true si se activó correctamente
     * @throws IllegalArgumentException si la tarifa mínima no existe
     */
    boolean activateMinimumTariff(Long id);
    
    /**
     * Desactiva una tarifa mínima.
     * @param id el ID de la tarifa mínima a desactivar
     * @return true si se desactivó correctamente
     * @throws IllegalArgumentException si la tarifa mínima no existe
     */
    boolean deactivateMinimumTariff(Long id);
    
    /**
     * Elimina una tarifa mínima.
     * @param id el ID de la tarifa mínima a eliminar
     * @return true si se eliminó correctamente
     * @throws IllegalArgumentException si la tarifa mínima no existe
     */
    boolean deleteMinimumTariff(Long id);
    
    /**
     * Verifica si existe una tarifa mínima para el año especificado en la corporación del usuario.
     * @param year el año
     * @return true si existe, false en caso contrario
     */
    boolean existsByYear(Integer year);
    
    /**
     * Obtiene tarifas mínimas ordenadas por fecha de creación (más recientes primero).
     * @return lista de tarifas mínimas ordenadas por fecha de creación descendente
     */
    List<MinimumTariff> getMinimumTariffsOrderByCreatedAtDesc();
    
    /**
     * Obtiene tarifas mínimas ordenadas por año (más recientes primero).
     * @return lista de tarifas mínimas ordenadas por año descendente
     */
    List<MinimumTariff> getMinimumTariffsOrderByYearDesc();
    
    /**
     * Obtiene tarifas mínimas activas ordenadas por año (más recientes primero).
     * @return lista de tarifas mínimas activas ordenadas por año descendente
     */
    List<MinimumTariff> getActiveMinimumTariffsOrderByYearDesc();
    
    /**
     * Obtiene tarifas mínimas de la corporación ordenadas por año (más recientes primero).
     * @return lista de tarifas mínimas de la corporación ordenadas por año descendente
     */
    List<MinimumTariff> getMyCorporationMinimumTariffsOrderByYearDesc();
    
    /**
     * Obtiene tarifas mínimas activas de la corporación ordenadas por año (más recientes primero).
     * @return lista de tarifas mínimas activas de la corporación ordenadas por año descendente
     */
    List<MinimumTariff> getActiveMyCorporationMinimumTariffsOrderByYearDesc();
    
    /**
     * Obtiene estadísticas de tarifas mínimas de la corporación del usuario autenticado.
     * @return estadísticas de tarifas mínimas
     */
    MinimumTariffStats getMyCorporationMinimumTariffStats();
    
    /**
     * Clase interna para estadísticas de tarifas mínimas
     */
    class MinimumTariffStats {
        private long totalMinimumTariffs;
        private long activeMinimumTariffs;
        private long inactiveMinimumTariffs;
        private long totalYears;
        private BigDecimal averageDboValue;
        private BigDecimal averageSstValue;
        private BigDecimal averageIpcValue;
        private BigDecimal minDboValue;
        private BigDecimal maxDboValue;
        private BigDecimal minSstValue;
        private BigDecimal maxSstValue;
        private BigDecimal minIpcValue;
        private BigDecimal maxIpcValue;
        
        // Constructors
        public MinimumTariffStats() {}
        
        // Getters and Setters
        public long getTotalMinimumTariffs() { return totalMinimumTariffs; }
        public void setTotalMinimumTariffs(long totalMinimumTariffs) { this.totalMinimumTariffs = totalMinimumTariffs; }
        
        public long getActiveMinimumTariffs() { return activeMinimumTariffs; }
        public void setActiveMinimumTariffs(long activeMinimumTariffs) { this.activeMinimumTariffs = activeMinimumTariffs; }
        
        public long getInactiveMinimumTariffs() { return inactiveMinimumTariffs; }
        public void setInactiveMinimumTariffs(long inactiveMinimumTariffs) { this.inactiveMinimumTariffs = inactiveMinimumTariffs; }
        
        public long getTotalYears() { return totalYears; }
        public void setTotalYears(long totalYears) { this.totalYears = totalYears; }
        
        public BigDecimal getAverageDboValue() { return averageDboValue; }
        public void setAverageDboValue(BigDecimal averageDboValue) { this.averageDboValue = averageDboValue; }
        
        public BigDecimal getAverageSstValue() { return averageSstValue; }
        public void setAverageSstValue(BigDecimal averageSstValue) { this.averageSstValue = averageSstValue; }
        
        public BigDecimal getAverageIpcValue() { return averageIpcValue; }
        public void setAverageIpcValue(BigDecimal averageIpcValue) { this.averageIpcValue = averageIpcValue; }
        
        public BigDecimal getMinDboValue() { return minDboValue; }
        public void setMinDboValue(BigDecimal minDboValue) { this.minDboValue = minDboValue; }
        
        public BigDecimal getMaxDboValue() { return maxDboValue; }
        public void setMaxDboValue(BigDecimal maxDboValue) { this.maxDboValue = maxDboValue; }
        
        public BigDecimal getMinSstValue() { return minSstValue; }
        public void setMinSstValue(BigDecimal minSstValue) { this.minSstValue = minSstValue; }
        
        public BigDecimal getMaxSstValue() { return maxSstValue; }
        public void setMaxSstValue(BigDecimal maxSstValue) { this.maxSstValue = maxSstValue; }
        
        public BigDecimal getMinIpcValue() { return minIpcValue; }
        public void setMinIpcValue(BigDecimal minIpcValue) { this.minIpcValue = minIpcValue; }
        
        public BigDecimal getMaxIpcValue() { return maxIpcValue; }
        public void setMaxIpcValue(BigDecimal maxIpcValue) { this.maxIpcValue = maxIpcValue; }
    }
}

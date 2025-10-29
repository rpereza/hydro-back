package com.univercloud.hydro.service;

import com.univercloud.hydro.entity.ProjectProgress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de Progreso de Proyectos.
 * Proporciona operaciones CRUD y lógica de negocio para progreso de proyectos.
 */
public interface ProjectProgressService {
    
    /**
     * Crea un nuevo progreso de proyecto.
     * @param projectProgress el progreso de proyecto a crear
     * @return el progreso de proyecto creado
     * @throws IllegalArgumentException si los datos son inválidos
     */
    ProjectProgress createProjectProgress(ProjectProgress projectProgress);
    
    /**
     * Actualiza un progreso de proyecto existente.
     * @param projectProgress el progreso de proyecto a actualizar
     * @return el progreso de proyecto actualizado
     * @throws IllegalArgumentException si el progreso de proyecto no existe
     */
    ProjectProgress updateProjectProgress(ProjectProgress projectProgress);
    
    /**
     * Obtiene un progreso de proyecto por su ID.
     * @param id el ID del progreso de proyecto
     * @return el progreso de proyecto, si existe
     */
    Optional<ProjectProgress> getProjectProgressById(Long id);
    
    /**
     * Obtiene todos los progresos de proyecto de la corporación del usuario autenticado.
     * @param pageable parámetros de paginación
     * @return página de progresos de proyecto
     */
    Page<ProjectProgress> getMyCorporationProjectProgresses(Pageable pageable);
    
    /**
     * Obtiene todos los progresos de proyecto de la corporación del usuario autenticado.
     * @return lista de progresos de proyecto
     */
    List<ProjectProgress> getAllMyCorporationProjectProgresses();
    
    /**
     * Obtiene progresos de proyecto por usuario de descarga.
     * @param dischargeUserId el ID del usuario de descarga
     * @return lista de progresos del usuario de descarga
     */
    List<ProjectProgress> getProjectProgressesByDischargeUser(Long dischargeUserId);
    
    /**
     * Obtiene progresos de proyecto por año.
     * @param year el año
     * @return lista de progresos del año
     */
    List<ProjectProgress> getProjectProgressesByYear(Integer year);
    
    /**
     * Obtiene progresos de proyecto por usuario de descarga y año.
     * @param dischargeUserId el ID del usuario de descarga
     * @param year el año
     * @return lista de progresos del usuario de descarga y año
     */
    List<ProjectProgress> getProjectProgressesByDischargeUserAndYear(Long dischargeUserId, Integer year);
    
    /**
     * Obtiene progresos de proyecto creados en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de progresos creados en el rango
     */
    List<ProjectProgress> getProjectProgressesByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Obtiene progresos de proyecto con porcentaje CCI en un rango específico.
     * @param minCciPercentage porcentaje mínimo de CCI
     * @param maxCciPercentage porcentaje máximo de CCI
     * @return lista de progresos con CCI en el rango
     */
    List<ProjectProgress> getProjectProgressesByCciPercentageRange(BigDecimal minCciPercentage, BigDecimal maxCciPercentage);
    
    /**
     * Obtiene progresos de proyecto con porcentaje CEV en un rango específico.
     * @param minCevPercentage porcentaje mínimo de CEV
     * @param maxCevPercentage porcentaje máximo de CEV
     * @return lista de progresos con CEV en el rango
     */
    List<ProjectProgress> getProjectProgressesByCevPercentageRange(BigDecimal minCevPercentage, BigDecimal maxCevPercentage);
    
    /**
     * Obtiene progresos de proyecto con porcentaje CDS en un rango específico.
     * @param minCdsPercentage porcentaje mínimo de CDS
     * @param maxCdsPercentage porcentaje máximo de CDS
     * @return lista de progresos con CDS en el rango
     */
    List<ProjectProgress> getProjectProgressesByCdsPercentageRange(BigDecimal minCdsPercentage, BigDecimal maxCdsPercentage);
    
    /**
     * Obtiene progresos de proyecto con porcentaje CCS en un rango específico.
     * @param minCcsPercentage porcentaje mínimo de CCS
     * @param maxCcsPercentage porcentaje máximo de CCS
     * @return lista de progresos con CCS en el rango
     */
    List<ProjectProgress> getProjectProgressesByCcsPercentageRange(BigDecimal minCcsPercentage, BigDecimal maxCcsPercentage);
    
    /**
     * Cuenta el número de progresos de proyecto de la corporación del usuario autenticado.
     * @return número de progresos de proyecto
     */
    long countMyCorporationProjectProgresses();
    
    /**
     * Cuenta progresos de proyecto por usuario de descarga.
     * @param dischargeUserId el ID del usuario de descarga
     * @return número de progresos del usuario de descarga
     */
    long countProjectProgressesByDischargeUser(Long dischargeUserId);
    
    /**
     * Cuenta progresos de proyecto por año.
     * @param year el año
     * @return número de progresos del año
     */
    long countProjectProgressesByYear(Integer year);
    
    /**
     * Elimina un progreso de proyecto.
     * @param id el ID del progreso de proyecto a eliminar
     * @return true si se eliminó correctamente
     * @throws IllegalArgumentException si el progreso de proyecto no existe
     */
    boolean deleteProjectProgress(Long id);
    
    /**
     * Verifica si existe un progreso de proyecto para el usuario de descarga y año especificados en la corporación del usuario.
     * @param dischargeUserId el ID del usuario de descarga
     * @param year el año
     * @return true si existe, false en caso contrario
     */
    boolean existsByDischargeUserAndYear(Long dischargeUserId, Integer year);
    
    /**
     * Obtiene progresos de proyecto ordenados por fecha de creación (más recientes primero).
     * @return lista de progresos de proyecto ordenados por fecha de creación descendente
     */
    List<ProjectProgress> getProjectProgressesOrderByCreatedAtDesc();
    
    /**
     * Obtiene progresos de proyecto ordenados por año (más recientes primero).
     * @return lista de progresos de proyecto ordenados por año descendente
     */
    List<ProjectProgress> getProjectProgressesOrderByYearDesc();
    
    /**
     * Obtiene progresos de proyecto de la corporación ordenados por año (más recientes primero).
     * @return lista de progresos de proyecto de la corporación ordenados por año descendente
     */
    List<ProjectProgress> getMyCorporationProjectProgressesOrderByYearDesc();
    
    /**
     * Obtiene estadísticas de progresos de proyecto de la corporación del usuario autenticado.
     * @return estadísticas de progresos de proyecto
     */
    ProjectProgressStats getMyCorporationProjectProgressStats();
    
    /**
     * Clase interna para estadísticas de progresos de proyecto
     */
    class ProjectProgressStats {
        private long totalProjectProgresses;
        private long totalDischargeUsers;
        private long totalYears;
        private BigDecimal averageCciPercentage;
        private BigDecimal averageCevPercentage;
        private BigDecimal averageCdsPercentage;
        private BigDecimal averageCcsPercentage;
        private BigDecimal minCciPercentage;
        private BigDecimal maxCciPercentage;
        private BigDecimal minCevPercentage;
        private BigDecimal maxCevPercentage;
        private BigDecimal minCdsPercentage;
        private BigDecimal maxCdsPercentage;
        private BigDecimal minCcsPercentage;
        private BigDecimal maxCcsPercentage;
        
        // Constructors
        public ProjectProgressStats() {}
        
        // Getters and Setters
        public long getTotalProjectProgresses() { return totalProjectProgresses; }
        public void setTotalProjectProgresses(long totalProjectProgresses) { this.totalProjectProgresses = totalProjectProgresses; }
        
        public long getTotalDischargeUsers() { return totalDischargeUsers; }
        public void setTotalDischargeUsers(long totalDischargeUsers) { this.totalDischargeUsers = totalDischargeUsers; }
        
        public long getTotalYears() { return totalYears; }
        public void setTotalYears(long totalYears) { this.totalYears = totalYears; }
        
        public BigDecimal getAverageCciPercentage() { return averageCciPercentage; }
        public void setAverageCciPercentage(BigDecimal averageCciPercentage) { this.averageCciPercentage = averageCciPercentage; }
        
        public BigDecimal getAverageCevPercentage() { return averageCevPercentage; }
        public void setAverageCevPercentage(BigDecimal averageCevPercentage) { this.averageCevPercentage = averageCevPercentage; }
        
        public BigDecimal getAverageCdsPercentage() { return averageCdsPercentage; }
        public void setAverageCdsPercentage(BigDecimal averageCdsPercentage) { this.averageCdsPercentage = averageCdsPercentage; }
        
        public BigDecimal getAverageCcsPercentage() { return averageCcsPercentage; }
        public void setAverageCcsPercentage(BigDecimal averageCcsPercentage) { this.averageCcsPercentage = averageCcsPercentage; }
        
        public BigDecimal getMinCciPercentage() { return minCciPercentage; }
        public void setMinCciPercentage(BigDecimal minCciPercentage) { this.minCciPercentage = minCciPercentage; }
        
        public BigDecimal getMaxCciPercentage() { return maxCciPercentage; }
        public void setMaxCciPercentage(BigDecimal maxCciPercentage) { this.maxCciPercentage = maxCciPercentage; }
        
        public BigDecimal getMinCevPercentage() { return minCevPercentage; }
        public void setMinCevPercentage(BigDecimal minCevPercentage) { this.minCevPercentage = minCevPercentage; }
        
        public BigDecimal getMaxCevPercentage() { return maxCevPercentage; }
        public void setMaxCevPercentage(BigDecimal maxCevPercentage) { this.maxCevPercentage = maxCevPercentage; }
        
        public BigDecimal getMinCdsPercentage() { return minCdsPercentage; }
        public void setMinCdsPercentage(BigDecimal minCdsPercentage) { this.minCdsPercentage = minCdsPercentage; }
        
        public BigDecimal getMaxCdsPercentage() { return maxCdsPercentage; }
        public void setMaxCdsPercentage(BigDecimal maxCdsPercentage) { this.maxCdsPercentage = maxCdsPercentage; }
        
        public BigDecimal getMinCcsPercentage() { return minCcsPercentage; }
        public void setMinCcsPercentage(BigDecimal minCcsPercentage) { this.minCcsPercentage = minCcsPercentage; }
        
        public BigDecimal getMaxCcsPercentage() { return maxCcsPercentage; }
        public void setMaxCcsPercentage(BigDecimal maxCcsPercentage) { this.maxCcsPercentage = maxCcsPercentage; }
    }
}

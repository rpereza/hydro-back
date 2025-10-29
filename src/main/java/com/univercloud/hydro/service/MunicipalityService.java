package com.univercloud.hydro.service;

import com.univercloud.hydro.entity.Municipality;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de Municipios.
 * Proporciona operaciones CRUD y lógica de negocio para municipios.
 */
public interface MunicipalityService {
    
    /**
     * Crea un nuevo municipio.
     * @param municipality el municipio a crear
     * @return el municipio creado
     * @throws IllegalArgumentException si los datos son inválidos
     */
    Municipality createMunicipality(Municipality municipality);
    
    /**
     * Actualiza un municipio existente.
     * @param municipality el municipio a actualizar
     * @return el municipio actualizado
     * @throws IllegalArgumentException si el municipio no existe
     */
    Municipality updateMunicipality(Municipality municipality);
    
    /**
     * Obtiene un municipio por su ID.
     * @param id el ID del municipio
     * @return el municipio, si existe
     */
    Optional<Municipality> getMunicipalityById(Long id);
    
    /**
     * Obtiene todos los municipios.
     * @param pageable parámetros de paginación
     * @return página de municipios
     */
    Page<Municipality> getAllMunicipalities(Pageable pageable);
    
    /**
     * Obtiene todos los municipios.
     * @return lista de municipios
     */
    List<Municipality> getAllMunicipalities();
    
    /**
     * Obtiene municipios por departamento.
     * @param departmentId el ID del departamento
     * @return lista de municipios del departamento
     */
    List<Municipality> getMunicipalitiesByDepartment(Long departmentId);
    
    /**
     * Obtiene municipios por categoría.
     * @param categoryId el ID de la categoría
     * @return lista de municipios de la categoría
     */
    List<Municipality> getMunicipalitiesByCategory(Long categoryId);
    
    /**
     * Busca un municipio por código.
     * @param code el código del municipio
     * @return el municipio si existe
     */
    Optional<Municipality> getMunicipalityByCode(String code);
    
    /**
     * Busca municipios por nombre (búsqueda parcial).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de municipios que coinciden
     */
    List<Municipality> searchMunicipalitiesByName(String name);
    
    /**
     * Busca municipios por departamento y nombre (búsqueda parcial).
     * @param departmentId el ID del departamento
     * @param name el nombre o parte del nombre a buscar
     * @return lista de municipios del departamento que coinciden
     */
    List<Municipality> searchMunicipalitiesByDepartmentAndName(Long departmentId, String name);
    
    /**
     * Busca municipios por código (búsqueda parcial).
     * @param code el código o parte del código a buscar
     * @return lista de municipios que coinciden con el código
     */
    List<Municipality> searchMunicipalitiesByCode(String code);
    
    /**
     * Obtiene municipios creados en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de municipios creados en el rango
     */
    List<Municipality> getMunicipalitiesByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Cuenta municipios por departamento.
     * @param departmentId el ID del departamento
     * @return número de municipios del departamento
     */
    long countMunicipalitiesByDepartment(Long departmentId);
    
    /**
     * Cuenta municipios por categoría.
     * @param categoryId el ID de la categoría
     * @return número de municipios de la categoría
     */
    long countMunicipalitiesByCategory(Long categoryId);
    
    /**
     * Obtiene municipios con NBI (Necesidades Básicas Insatisfechas) mayor a un valor.
     * @param nbi el valor mínimo de NBI
     * @return lista de municipios con NBI mayor al valor especificado
     */
    List<Municipality> getMunicipalitiesWithNbiGreaterThan(BigDecimal nbi);
    
    /**
     * Obtiene municipios con NBI menor a un valor.
     * @param nbi el valor máximo de NBI
     * @return lista de municipios con NBI menor al valor especificado
     */
    List<Municipality> getMunicipalitiesWithNbiLessThan(BigDecimal nbi);
    
    /**
     * Obtiene municipios con NBI en un rango.
     * @param minNbi el valor mínimo de NBI
     * @param maxNbi el valor máximo de NBI
     * @return lista de municipios con NBI en el rango especificado
     */
    List<Municipality> getMunicipalitiesWithNbiBetween(BigDecimal minNbi, BigDecimal maxNbi);
    
    /**
     * Elimina un municipio.
     * @param id el ID del municipio a eliminar
     * @return true si se eliminó correctamente
     * @throws IllegalArgumentException si el municipio no existe
     */
    boolean deleteMunicipality(Long id);
    
    /**
     * Verifica si existe un municipio con el código especificado.
     * @param code el código del municipio
     * @return true si existe, false en caso contrario
     */
    boolean existsByCode(String code);
    
    /**
     * Obtiene municipios ordenados por nombre.
     * @return lista de municipios ordenados por nombre
     */
    List<Municipality> getMunicipalitiesOrderByName();
    
    /**
     * Obtiene municipios por departamento ordenados por nombre.
     * @param departmentId el ID del departamento
     * @return lista de municipios del departamento ordenados por nombre
     */
    List<Municipality> getMunicipalitiesByDepartmentOrderByName(Long departmentId);
    
    /**
     * Obtiene municipios por categoría ordenados por nombre.
     * @param categoryId el ID de la categoría
     * @return lista de municipios de la categoría ordenados por nombre
     */
    List<Municipality> getMunicipalitiesByCategoryOrderByName(Long categoryId);
    
    /**
     * Obtiene municipios ordenados por fecha de creación (más recientes primero).
     * @return lista de municipios ordenados por fecha de creación descendente
     */
    List<Municipality> getMunicipalitiesOrderByCreatedAtDesc();
    
    /**
     * Obtiene estadísticas de municipios.
     * @return estadísticas de municipios
     */
    MunicipalityStats getMunicipalityStats();    
   
    /**
     * Obtiene todos los municipios activos.
     * @return lista de municipios activos
     */
    List<Municipality> getAllActiveMunicipalities();
    
    /**
     * Obtiene todos los municipios inactivos.
     * @return lista de municipios inactivos
     */
    List<Municipality> getAllInactiveMunicipalities();
    
    /**
     * Busca un municipio por nombre.
     * @param name el nombre del municipio
     * @return el municipio si existe
     */
    Optional<Municipality> getMunicipalityByName(String name);
    
    /**
     * Busca municipios activos por nombre (búsqueda parcial).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de municipios activos que coinciden
     * @throws IllegalStateException si el usuario no está autenticado o no tiene corporación
     */
    List<Municipality> searchActiveMunicipalitiesByName(String name);
    
    /**
     * Activa un municipio.
     * @param id el ID del municipio a activar
     * @return true si se activó correctamente
     * @throws IllegalArgumentException si el municipio no existe
     * @throws IllegalStateException si el usuario no tiene permisos para activar el municipio
     */
    boolean activateMunicipality(Long id);
    
    /**
     * Desactiva un municipio.
     * @param id el ID del municipio a desactivar
     * @return true si se desactivó correctamente
     * @throws IllegalArgumentException si el municipio no existe
     * @throws IllegalStateException si el usuario no tiene permisos para desactivar el municipio
     */
    boolean deactivateMunicipality(Long id);
    
    /**
     * Verifica si existe un municipio con el nombre especificado.
     * @param name el nombre del municipio
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);
    
    /**
     * Obtiene municipios activos ordenados por nombre.
     * @return lista de municipios activos ordenados por nombre
     */
    List<Municipality> getActiveMunicipalitiesOrderByName();
    
    /**
     * Clase interna para estadísticas de municipios
     */
    class MunicipalityStats {
        private long totalMunicipalities;
        private long municipalitiesByDepartment;
        private long municipalitiesByCategory;
        private double averageNbi;
        private double minNbi;
        private double maxNbi;
        private long municipalitiesWithNbiData;
        private long municipalitiesWithoutNbiData;
        
        // Constructors
        public MunicipalityStats() {}
        
        // Getters and Setters
        public long getTotalMunicipalities() { return totalMunicipalities; }
        public void setTotalMunicipalities(long totalMunicipalities) { this.totalMunicipalities = totalMunicipalities; }
        
        public long getMunicipalitiesByDepartment() { return municipalitiesByDepartment; }
        public void setMunicipalitiesByDepartment(long municipalitiesByDepartment) { this.municipalitiesByDepartment = municipalitiesByDepartment; }
        
        public long getMunicipalitiesByCategory() { return municipalitiesByCategory; }
        public void setMunicipalitiesByCategory(long municipalitiesByCategory) { this.municipalitiesByCategory = municipalitiesByCategory; }
        
        public double getAverageNbi() { return averageNbi; }
        public void setAverageNbi(double averageNbi) { this.averageNbi = averageNbi; }
        
        public double getMinNbi() { return minNbi; }
        public void setMinNbi(double minNbi) { this.minNbi = minNbi; }
        
        public double getMaxNbi() { return maxNbi; }
        public void setMaxNbi(double maxNbi) { this.maxNbi = maxNbi; }
        
        public long getMunicipalitiesWithNbiData() { return municipalitiesWithNbiData; }
        public void setMunicipalitiesWithNbiData(long municipalitiesWithNbiData) { this.municipalitiesWithNbiData = municipalitiesWithNbiData; }
        
        public long getMunicipalitiesWithoutNbiData() { return municipalitiesWithoutNbiData; }
        public void setMunicipalitiesWithoutNbiData(long municipalitiesWithoutNbiData) { this.municipalitiesWithoutNbiData = municipalitiesWithoutNbiData; }
    }
}

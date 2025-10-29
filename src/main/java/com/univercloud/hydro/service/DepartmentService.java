package com.univercloud.hydro.service;

import com.univercloud.hydro.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de Departamentos.
 * Proporciona operaciones CRUD y lógica de negocio para departamentos.
 */
public interface DepartmentService {
    
    /**
     * Crea un nuevo departamento.
     * @param department el departamento a crear
     * @return el departamento creado
     * @throws IllegalArgumentException si los datos son inválidos
     */
    Department createDepartment(Department department);
    
    /**
     * Actualiza un departamento existente.
     * @param department el departamento a actualizar
     * @return el departamento actualizado
     * @throws IllegalArgumentException si el departamento no existe
     */
    Department updateDepartment(Department department);
    
    /**
     * Obtiene un departamento por su ID.
     * @param id el ID del departamento
     * @return el departamento, si existe
     */
    Optional<Department> getDepartmentById(Long id);
    
    /**
     * Obtiene todos los departamentos.
     * @param pageable parámetros de paginación
     * @return página de departamentos
     */
    Page<Department> getAllDepartments(Pageable pageable);
    
    /**
     * Obtiene todos los departamentos.
     * @return lista de departamentos
     */
    List<Department> getAllDepartments();
    
    /**
     * Busca un departamento por nombre.
     * @param name el nombre del departamento
     * @return el departamento si existe
     */
    Optional<Department> getDepartmentByName(String name);
    
    /**
     * Busca departamentos por nombre (búsqueda parcial).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de departamentos que coinciden
     */
    List<Department> searchDepartmentsByName(String name);
    
    /**
     * Obtiene departamentos creados en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de departamentos creados en el rango
     */
    List<Department> getDepartmentsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Elimina un departamento.
     * @param id el ID del departamento a eliminar
     * @return true si se eliminó correctamente
     * @throws IllegalArgumentException si el departamento no existe
     */
    boolean deleteDepartment(Long id);
    
    /**
     * Verifica si existe un departamento con el nombre especificado.
     * @param name el nombre del departamento
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);
    
    /**
     * Obtiene departamentos ordenados por nombre.
     * @return lista de departamentos ordenados por nombre
     */
    List<Department> getDepartmentsOrderByName();
    
    /**
     * Obtiene departamentos ordenados por fecha de creación (más recientes primero).
     * @return lista de departamentos ordenados por fecha de creación descendente
     */
    List<Department> getDepartmentsOrderByCreatedAtDesc();
    
    /**
     * Obtiene estadísticas de departamentos.
     * @return estadísticas de departamentos
     */
    DepartmentStats getDepartmentStats();
    
    /**
     * Clase interna para estadísticas de departamentos
     */
    class DepartmentStats {
        private long totalDepartments;
        private long totalMunicipalities;
        private double averageMunicipalitiesPerDepartment;
        private long departmentsWithMunicipalities;
        private long departmentsWithoutMunicipalities;
        
        // Constructors
        public DepartmentStats() {}
        
        // Getters and Setters
        public long getTotalDepartments() { return totalDepartments; }
        public void setTotalDepartments(long totalDepartments) { this.totalDepartments = totalDepartments; }
        
        public long getTotalMunicipalities() { return totalMunicipalities; }
        public void setTotalMunicipalities(long totalMunicipalities) { this.totalMunicipalities = totalMunicipalities; }
        
        public double getAverageMunicipalitiesPerDepartment() { return averageMunicipalitiesPerDepartment; }
        public void setAverageMunicipalitiesPerDepartment(double averageMunicipalitiesPerDepartment) { this.averageMunicipalitiesPerDepartment = averageMunicipalitiesPerDepartment; }
        
        public long getDepartmentsWithMunicipalities() { return departmentsWithMunicipalities; }
        public void setDepartmentsWithMunicipalities(long departmentsWithMunicipalities) { this.departmentsWithMunicipalities = departmentsWithMunicipalities; }
        
        public long getDepartmentsWithoutMunicipalities() { return departmentsWithoutMunicipalities; }
        public void setDepartmentsWithoutMunicipalities(long departmentsWithoutMunicipalities) { this.departmentsWithoutMunicipalities = departmentsWithoutMunicipalities; }
    }
}

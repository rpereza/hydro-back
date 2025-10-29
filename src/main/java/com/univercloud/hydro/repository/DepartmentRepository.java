package com.univercloud.hydro.repository;

import com.univercloud.hydro.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository para la entidad Department.
 * Proporciona métodos de consulta para gestionar departamentos.
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    /**
     * Busca un departamento por nombre.
     * @param name el nombre del departamento
     * @return el departamento si existe
     */
    Optional<Department> findByName(String name);
    
    /**
     * Verifica si existe un departamento con el nombre especificado.
     * @param name el nombre del departamento
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);
    
    /**
     * Busca departamentos por nombre (búsqueda parcial, case-insensitive).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de departamentos que coinciden con el nombre
     */
    @Query("SELECT d FROM Department d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Department> findByNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * Busca departamentos creados en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de departamentos creados en el rango
     */
    List<Department> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Busca departamentos ordenados por nombre.
     * @return lista de departamentos ordenados por nombre
     */
    @Query("SELECT d FROM Department d ORDER BY d.name ASC")
    List<Department> findAllOrderByName();
    
    /**
     * Busca departamentos ordenados por fecha de creación (más recientes primero).
     * @return lista de departamentos ordenados por fecha de creación descendente
     */
    @Query("SELECT d FROM Department d ORDER BY d.createdAt DESC")
    List<Department> findAllOrderByCreatedAtDesc();
}

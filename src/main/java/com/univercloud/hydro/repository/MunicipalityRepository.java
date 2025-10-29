package com.univercloud.hydro.repository;

import com.univercloud.hydro.entity.Category;
import com.univercloud.hydro.entity.Department;
import com.univercloud.hydro.entity.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository para la entidad Municipality.
 * Proporciona métodos de consulta para gestionar municipios.
 */
@Repository
public interface MunicipalityRepository extends JpaRepository<Municipality, Long> {
    
    /**
     * Busca municipios por departamento.
     * @param department el departamento
     * @return lista de municipios del departamento
     */
    List<Municipality> findByDepartment(Department department);
    
    /**
     * Busca municipios por categoría.
     * @param category la categoría
     * @return lista de municipios de la categoría
     */
    List<Municipality> findByCategory(Category category);
    
    /**
     * Busca un municipio por código.
     * @param code el código del municipio
     * @return el municipio si existe
     */
    Optional<Municipality> findByCode(String code);
    
    /**
     * Verifica si existe un municipio con el código especificado.
     * @param code el código del municipio
     * @return true si existe, false en caso contrario
     */
    boolean existsByCode(String code);
    
    /**
     * Busca municipios por nombre (búsqueda parcial, case-insensitive).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de municipios que coinciden con el nombre
     */
    @Query("SELECT m FROM Municipality m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Municipality> findByNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * Busca municipios por departamento y nombre (búsqueda parcial, case-insensitive).
     * @param department el departamento
     * @param name el nombre o parte del nombre a buscar
     * @return lista de municipios del departamento que coinciden con el nombre
     */
    @Query("SELECT m FROM Municipality m WHERE m.department = :department AND LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Municipality> findByDepartmentAndNameContainingIgnoreCase(@Param("department") Department department, @Param("name") String name);
    
    /**
     * Busca municipios por código (búsqueda parcial, case-insensitive).
     * @param code el código o parte del código a buscar
     * @return lista de municipios que coinciden con el código
     */
    @Query("SELECT m FROM Municipality m WHERE LOWER(m.code) LIKE LOWER(CONCAT('%', :code, '%'))")
    List<Municipality> findByCodeContainingIgnoreCase(@Param("code") String code);
    
    /**
     * Busca municipios creados en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de municipios creados en el rango
     */
    List<Municipality> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Cuenta municipios por departamento.
     * @param departmentId el ID del departamento
     * @return número de municipios del departamento
     */
    @Query("SELECT COUNT(m) FROM Municipality m WHERE m.department.id = :departmentId")
    long countByDepartmentId(@Param("departmentId") Long departmentId);
    
    /**
     * Cuenta municipios por categoría.
     * @param categoryId el ID de la categoría
     * @return número de municipios de la categoría
     */
    @Query("SELECT COUNT(m) FROM Municipality m WHERE m.category.id = :categoryId")
    long countByCategoryId(@Param("categoryId") Long categoryId);
    
    /**
     * Busca municipios ordenados por nombre.
     * @return lista de municipios ordenados por nombre
     */
    @Query("SELECT m FROM Municipality m ORDER BY m.name ASC")
    List<Municipality> findAllOrderByName();
    
    /**
     * Busca municipios por departamento ordenados por nombre.
     * @param department el departamento
     * @return lista de municipios del departamento ordenados por nombre
     */
    @Query("SELECT m FROM Municipality m WHERE m.department = :department ORDER BY m.name ASC")
    List<Municipality> findByDepartmentOrderByName(@Param("department") Department department);
    
    /**
     * Busca municipios por categoría ordenados por nombre.
     * @param category la categoría
     * @return lista de municipios de la categoría ordenados por nombre
     */
    @Query("SELECT m FROM Municipality m WHERE m.category = :category ORDER BY m.name ASC")
    List<Municipality> findByCategoryOrderByName(@Param("category") Category category);
    
    /**
     * Busca municipios ordenados por fecha de creación (más recientes primero).
     * @return lista de municipios ordenados por fecha de creación descendente
     */
    @Query("SELECT m FROM Municipality m ORDER BY m.createdAt DESC")
    List<Municipality> findAllOrderByCreatedAtDesc();
    
    /**
     * Busca municipios con NBI (Necesidades Básicas Insatisfechas) mayor a un valor.
     * @param nbi el valor mínimo de NBI
     * @return lista de municipios con NBI mayor al valor especificado
     */
    @Query("SELECT m FROM Municipality m WHERE m.nbi > :nbi ORDER BY m.nbi DESC")
    List<Municipality> findByNbiGreaterThan(@Param("nbi") java.math.BigDecimal nbi);
    
    /**
     * Busca municipios con NBI menor a un valor.
     * @param nbi el valor máximo de NBI
     * @return lista de municipios con NBI menor al valor especificado
     */
    @Query("SELECT m FROM Municipality m WHERE m.nbi < :nbi ORDER BY m.nbi ASC")
    List<Municipality> findByNbiLessThan(@Param("nbi") java.math.BigDecimal nbi);
    
    /**
     * Busca municipios con NBI en un rango.
     * @param minNbi el valor mínimo de NBI
     * @param maxNbi el valor máximo de NBI
     * @return lista de municipios con NBI en el rango especificado
     */
    @Query("SELECT m FROM Municipality m WHERE m.nbi BETWEEN :minNbi AND :maxNbi ORDER BY m.nbi ASC")
    List<Municipality> findByNbiBetween(@Param("minNbi") java.math.BigDecimal minNbi, @Param("maxNbi") java.math.BigDecimal maxNbi);
    
    /**
     * Busca municipios activos.
     * @return lista de municipios activos
     */
    List<Municipality> findByIsActiveTrue();
    
    /**
     * Busca municipios inactivos.
     * @return lista de municipios inactivos
     */
    List<Municipality> findByIsActiveFalse();
    
    /**
     * Busca un municipio por nombre.
     * @param name el nombre del municipio
     * @return el municipio si existe
     */
    Optional<Municipality> findByName(String name);
    
    /**
     * Verifica si existe un municipio con el nombre especificado.
     * @param name el nombre del municipio
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);
    
    /**
     * Busca municipios activos ordenados por nombre.
     * @return lista de municipios activos ordenados por nombre
     */
    @Query("SELECT m FROM Municipality m WHERE m.isActive = true ORDER BY m.name ASC")
    List<Municipality> findByIsActiveTrueOrderByName();

    /**
     * Busca municipios activos por nombre (búsqueda parcial, case-insensitive).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de municipios activos que coinciden con el nombre
     */
    @Query("SELECT m FROM Municipality m WHERE m.isActive = true AND LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Municipality> findByIsActiveTrueAndNameContainingIgnoreCase(@Param("name") String name);
}

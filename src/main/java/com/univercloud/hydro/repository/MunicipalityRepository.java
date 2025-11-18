package com.univercloud.hydro.repository;

import com.univercloud.hydro.entity.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para la entidad Municipality.
 * Proporciona métodos de consulta para gestionar municipios.
 */
@Repository
public interface MunicipalityRepository extends JpaRepository<Municipality, Long> {
    
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
     * Busca municipios activos por ID de departamento.
     * @param departmentId el ID del departamento
     * @return lista de municipios activos del departamento
     */
    List<Municipality> findByDepartmentIdAndIsActiveTrue(Long departmentId);
    
    /**
     * Cuenta usuarios de descarga por municipio.
     * @param municipalityId el ID del municipio
     * @return número de usuarios de descarga asociados al municipio
     */
    @Query("SELECT COUNT(du) FROM DischargeUser du WHERE du.municipality.id = :municipalityId")
    long countDischargeUsersByMunicipalityId(@Param("municipalityId") Long municipalityId);
}

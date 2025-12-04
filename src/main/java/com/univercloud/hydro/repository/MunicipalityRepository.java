package com.univercloud.hydro.repository;

import com.univercloud.hydro.entity.Municipality;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
     * Carga las relaciones department y category para evitar problemas de lazy loading.
     * @param name el nombre o parte del nombre a buscar
     * @return lista de municipios que coinciden con el nombre
     */
    @EntityGraph(attributePaths = {"department", "category"})
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
     * Carga las relaciones department y category para evitar problemas de lazy loading.
     * @param departmentId el ID del departamento
     * @return lista de municipios activos del departamento
     */
    @EntityGraph(attributePaths = {"department", "category"})
    List<Municipality> findByDepartmentIdAndIsActiveTrue(Long departmentId);
    
    /**
     * Obtiene todos los municipios con paginación.
     * Carga las relaciones department y category para evitar problemas de lazy loading.
     * @param pageable parámetros de paginación
     * @return página de municipios
     */
    @Override
    @EntityGraph(attributePaths = {"department", "category"})
    @NonNull
    Page<Municipality> findAll(@NonNull Pageable pageable);
    
    /**
     * Obtiene todos los municipios.
     * Carga las relaciones department y category para evitar problemas de lazy loading.
     * @return lista de municipios
     */
    @Override
    @EntityGraph(attributePaths = {"department", "category"})
    @NonNull
    List<Municipality> findAll();
    
    /**
     * Busca un municipio por ID.
     * Carga las relaciones department y category para evitar problemas de lazy loading.
     * @param id el ID del municipio
     * @return el municipio si existe
     */
    @Override
    @EntityGraph(attributePaths = {"department", "category"})
    @NonNull
    Optional<Municipality> findById(@NonNull Long id);
    
    /**
     * Cuenta usuarios de descarga por municipio.
     * @param municipalityId el ID del municipio
     * @return número de usuarios de descarga asociados al municipio
     */
    @Query("SELECT COUNT(du) FROM DischargeUser du WHERE du.municipality.id = :municipalityId")
    long countDischargeUsersByMunicipalityId(@Param("municipalityId") Long municipalityId);
}

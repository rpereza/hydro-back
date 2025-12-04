package com.univercloud.hydro.repository;

import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.Discharge;
import com.univercloud.hydro.entity.DischargeUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para la entidad Discharge.
 * Proporciona métodos de consulta para gestionar descargas de agua.
 */
@Repository
public interface DischargeRepository extends JpaRepository<Discharge, Long> {
    
    /**
     * Busca descargas por usuario de descarga.
     * @param dischargeUser el usuario de descarga
     * @return lista de descargas del usuario
     */
    List<Discharge> findByDischargeUser(DischargeUser dischargeUser);
    
    /**
     * Busca una descarga por número y año.
     * @param number el número de descarga
     * @param year el año
     * @return la descarga si existe
     */
    Optional<Discharge> findByNumberAndYear(Integer number, Integer year);
    
    /**
     * Verifica si existe una descarga con el número y año especificados en la corporación.
     * @param corporation la corporación
     * @param number el número de descarga
     * @param year el año
     * @return true si existe, false en caso contrario
     */
    @Query("SELECT COUNT(d) > 0 FROM Discharge d WHERE d.corporation = :corporation AND d.number = :number AND d.year = :year")
    boolean existsByCorporationAndNumberAndYear(@Param("corporation") Corporation corporation, @Param("number") Integer number, @Param("year") Integer year);
    
    /**
     * Verifica si existe una descarga con el número y año especificados en la corporación, excluyendo un ID específico.
     * @param corporation la corporación
     * @param number el número de descarga
     * @param year el año
     * @param excludeId el ID a excluir de la búsqueda
     * @return true si existe, false en caso contrario
     */
    @Query("SELECT COUNT(d) > 0 FROM Discharge d WHERE d.corporation = :corporation AND d.number = :number AND d.year = :year AND d.id != :excludeId")
    boolean existsByCorporationAndNumberAndYearExcludingId(@Param("corporation") Corporation corporation, @Param("number") Integer number, @Param("year") Integer year, @Param("excludeId") Long excludeId);
    
    /**
     * Busca descargas por corporación y nombre (búsqueda parcial, case-insensitive).
     * @param corporation la corporación
     * @param name el nombre o parte del nombre a buscar
     * @return lista de descargas de la corporación que coinciden con el nombre
     */
    @Query("SELECT d FROM Discharge d WHERE d.corporation = :corporation AND LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Discharge> findByCorporationAndNameContainingIgnoreCase(@Param("corporation") Corporation corporation, @Param("name") String name);
    
    /**
     * Busca descargas por corporación con paginación.
     * Carga la relación dischargeUser con las relaciones municipality.department, economicActivity y authorizationType para evitar problemas de lazy loading.
     * @param corporation la corporación
     * @param pageable parámetros de paginación
     * @return página de descargas de la corporación
     */
    @EntityGraph(attributePaths = {"dischargeUser.municipality.department", "dischargeUser.economicActivity", "dischargeUser.authorizationType"})
    Page<Discharge> findByCorporation(Corporation corporation, Pageable pageable);
    
    /**
     * Busca descargas por corporación y año.
     * @param corporation la corporación
     * @param year el año
     * @return lista de descargas de la corporación y año
     */
    List<Discharge> findByCorporationAndYear(Corporation corporation, Integer year);
    
    /**
     * Busca una descarga por ID y corporación.
     * @param id el ID de la descarga
     * @param corporationId el ID de la corporación
     * @return la descarga si existe y pertenece a la corporación
     */
    @Query("SELECT d FROM Discharge d WHERE d.id = :id AND d.corporation.id = :corporationId")
    Optional<Discharge> findByIdAndCorporationId(@Param("id") Long id, @Param("corporationId") Long corporationId);
}


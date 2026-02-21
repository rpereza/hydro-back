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
     * Carga las relaciones necesarias incluyendo dischargeUser para evitar problemas de lazy loading.
     * @param dischargeUser el usuario de descarga
     * @return lista de descargas del usuario
     */
    @EntityGraph(attributePaths = {"dischargeUser"})
    List<Discharge> findByDischargeUser(DischargeUser dischargeUser);

    /**
     * Busca descargas por corporación y nombre (búsqueda parcial, case-insensitive).
     * Carga las relaciones necesarias. Solo carga dischargeParameters para evitar MultipleBagFetchException.
     * @param corporation la corporación
     * @param name el nombre o parte del nombre a buscar
     * @return lista de descargas de la corporación que coinciden con el nombre
     */
    @EntityGraph(attributePaths = {"dischargeUser.municipality.department", "dischargeUser.municipality.category", "dischargeUser.economicActivity", "dischargeUser.authorizationType", "basinSection.waterBasin", "dischargeParameters"})
    @Query("SELECT d FROM Discharge d WHERE d.corporation = :corporation AND LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Discharge> findByCorporationAndNameContainingIgnoreCase(@Param("corporation") Corporation corporation, @Param("name") String name);
    
    /**
     * Busca descargas por corporación con paginación.
     * Carga las relaciones necesarias incluyendo dischargeUser para evitar problemas de lazy loading.
     * @param corporation la corporación
     * @param pageable parámetros de paginación
     * @return página de descargas de la corporación
     */
    @EntityGraph(attributePaths = {"dischargeUser"})
    Page<Discharge> findByCorporation(Corporation corporation, Pageable pageable);
    
    /**
     * Busca una descarga por ID y corporación.
     * Carga dischargeMonitorings y monitoringStation de cada uno en el EntityGraph.
     * dischargeParameters se carga manualmente en el servicio para evitar MultipleBagFetchException.
     * @param id el ID de la descarga
     * @param corporationId el ID de la corporación
     * @return la descarga si existe y pertenece a la corporación
     */
    @EntityGraph(attributePaths = {"dischargeUser.municipality.department", "dischargeUser.municipality.category", "dischargeUser.economicActivity", "dischargeUser.authorizationType", 
    "basinSection.waterBasin", "dischargeMonitorings", "dischargeMonitorings.monitoringStation", "discharge.municipality.department"})
    @Query("SELECT d FROM Discharge d WHERE d.id = :id AND d.corporation.id = :corporationId")
    Optional<Discharge> findByIdAndCorporationId(@Param("id") Long id, @Param("corporationId") Long corporationId);
}


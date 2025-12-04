package com.univercloud.hydro.repository;

import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.ProjectProgress;
import com.univercloud.hydro.entity.DischargeUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository para la entidad ProjectProgress.
 * Proporciona métodos de consulta para gestionar progreso de proyectos.
 */
@Repository
public interface ProjectProgressRepository extends JpaRepository<ProjectProgress, Long> {
    
    /**
     * Busca progresos por corporación con paginación.
     * Carga la relación dischargeUser con las relaciones municipality.department, municipality.category, economicActivity y authorizationType para evitar problemas de lazy loading.
     * @param corporation la corporación
     * @param pageable parámetros de paginación
     * @return página de progresos de la corporación
     */
    @EntityGraph(attributePaths = {"dischargeUser.municipality.department", "dischargeUser.municipality.category", "dischargeUser.economicActivity", "dischargeUser.authorizationType"})
    Page<ProjectProgress> findByCorporation(Corporation corporation, Pageable pageable);
    
    /**
     * Verifica si existe un progreso para el usuario de descarga y año especificados en la corporación.
     * @param corporation la corporación
     * @param dischargeUser el usuario de descarga
     * @param year el año
     * @return true si existe, false en caso contrario
     */
    boolean existsByCorporationAndDischargeUserAndYear(Corporation corporation, DischargeUser dischargeUser, Integer year);
    
    /**
     * Cuenta progresos por ID de usuario de descarga.
     * @param dischargeUserId el ID del usuario de descarga
     * @return número de progresos del usuario de descarga
     */
    @Query("SELECT COUNT(pp) FROM ProjectProgress pp WHERE pp.dischargeUser.id = :dischargeUserId")
    long countByDischargeUserId(@Param("dischargeUserId") Long dischargeUserId);
    
    /**
     * Busca un progreso por ID y corporación.
     * @param id el ID del progreso
     * @param corporationId el ID de la corporación
     * @return el progreso si existe y pertenece a la corporación
     */
    @Query("SELECT pp FROM ProjectProgress pp WHERE pp.id = :id AND pp.corporation.id = :corporationId")
    Optional<ProjectProgress> findByIdAndCorporationId(@Param("id") Long id, @Param("corporationId") Long corporationId);
}

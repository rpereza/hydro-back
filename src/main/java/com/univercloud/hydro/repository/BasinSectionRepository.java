package com.univercloud.hydro.repository;

import com.univercloud.hydro.entity.BasinSection;
import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.WaterBasin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository para la entidad BasinSection.
 * Proporciona métodos de consulta para gestionar secciones de cuenca.
 */
@Repository
public interface BasinSectionRepository extends JpaRepository<BasinSection, Long> {
    
    /**
     * Busca secciones por cuenca hidrográfica.
     * @param waterBasin la cuenca hidrográfica
     * @return lista de secciones de la cuenca
     */
    List<BasinSection> findByWaterBasin(WaterBasin waterBasin);
    
    /**
     * Busca secciones por corporación.
     * @param corporation la corporación
     * @return lista de secciones de la corporación
     */
    List<BasinSection> findByCorporation(Corporation corporation);

    /**
     * Busca secciones de la corporación con paginación.
     * @param corporation la corporación
     * @param pageable parámetros de paginación
     * @return página de secciones de la corporación
     */
    Page<BasinSection> findByCorporation(Corporation corporation, Pageable pageable);
    
    /**
     * Busca secciones activas.
     * @return lista de secciones activas
     */
    List<BasinSection> findByIsActiveTrue();
    
    /**
     * Busca secciones inactivas.
     * @return lista de secciones inactivas
     */
    List<BasinSection> findByIsActiveFalse();
    
    /**
     * Busca secciones activas por cuenca.
     * @param waterBasin la cuenca hidrográfica
     * @return lista de secciones activas de la cuenca
     */
    List<BasinSection> findByWaterBasinAndIsActiveTrue(WaterBasin waterBasin);
    
    /**
     * Busca una sección por nombre.
     * @param name el nombre de la sección
     * @return la sección si existe
     */
    Optional<BasinSection> findByName(String name);
    
    /**
     * Verifica si existe una sección con el nombre especificado.
     * @param name el nombre de la sección
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);
    
    /**
     * Busca secciones por nombre (búsqueda parcial, case-insensitive).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de secciones que coinciden con el nombre
     */
    @Query("SELECT bs FROM BasinSection bs WHERE LOWER(bs.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<BasinSection> findByNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * Busca secciones por cuenca y nombre (búsqueda parcial, case-insensitive).
     * @param waterBasin la cuenca hidrográfica
     * @param name el nombre o parte del nombre a buscar
     * @return lista de secciones de la cuenca que coinciden con el nombre
     */
    @Query("SELECT bs FROM BasinSection bs WHERE bs.waterBasin = :waterBasin AND LOWER(bs.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<BasinSection> findByWaterBasinAndNameContainingIgnoreCase(@Param("waterBasin") WaterBasin waterBasin, @Param("name") String name);
    
    /**
     * Busca secciones activas por nombre (búsqueda parcial, case-insensitive).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de secciones activas que coinciden con el nombre
     */
    @Query("SELECT bs FROM BasinSection bs WHERE bs.isActive = true AND LOWER(bs.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<BasinSection> findActiveByNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * Busca secciones creadas en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de secciones creadas en el rango
     */
    List<BasinSection> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Cuenta secciones por cuenca.
     * @param waterBasinId el ID de la cuenca
     * @return número de secciones de la cuenca
     */
    @Query("SELECT COUNT(bs) FROM BasinSection bs WHERE bs.waterBasin.id = :waterBasinId")
    long countByWaterBasinId(@Param("waterBasinId") Long waterBasinId);
    
    /**
     * Cuenta secciones activas por cuenca.
     * @param waterBasinId el ID de la cuenca
     * @return número de secciones activas de la cuenca
     */
    @Query("SELECT COUNT(bs) FROM BasinSection bs WHERE bs.waterBasin.id = :waterBasinId AND bs.isActive = true")
    long countActiveByWaterBasinId(@Param("waterBasinId") Long waterBasinId);
    
    /**
     * Cuenta secciones por corporación.
     * @param corporationId el ID de la corporación
     * @return número de secciones de la corporación
     */
    @Query("SELECT COUNT(bs) FROM BasinSection bs WHERE bs.corporation.id = :corporationId")
    long countByCorporationId(@Param("corporationId") Long corporationId);
    
    /**
     * Busca secciones ordenadas por nombre.
     * @return lista de secciones ordenadas por nombre
     */
    @Query("SELECT bs FROM BasinSection bs ORDER BY bs.name ASC")
    List<BasinSection> findAllOrderByName();
    
    /**
     * Busca secciones por cuenca ordenadas por nombre.
     * @param waterBasin la cuenca hidrográfica
     * @return lista de secciones de la cuenca ordenadas por nombre
     */
    @Query("SELECT bs FROM BasinSection bs WHERE bs.waterBasin = :waterBasin ORDER BY bs.name ASC")
    List<BasinSection> findByWaterBasinOrderByName(@Param("waterBasin") WaterBasin waterBasin);
    
    /**
     * Busca secciones activas ordenadas por nombre.
     * @return lista de secciones activas ordenadas por nombre
     */
    @Query("SELECT bs FROM BasinSection bs WHERE bs.isActive = true ORDER BY bs.name ASC")
    List<BasinSection> findActiveOrderByName();
    
    /**
     * Busca secciones por corporación ordenadas por nombre.
     * @param corporation la corporación
     * @return lista de secciones de la corporación ordenadas por nombre
     */
    @Query("SELECT bs FROM BasinSection bs WHERE bs.corporation = :corporation ORDER BY bs.name ASC")
    List<BasinSection> findByCorporationOrderByName(@Param("corporation") Corporation corporation);
    
    /**
     * Busca secciones ordenadas por fecha de creación (más recientes primero).
     * @return lista de secciones ordenadas por fecha de creación descendente
     */
    @Query("SELECT bs FROM BasinSection bs ORDER BY bs.createdAt DESC")
    List<BasinSection> findAllOrderByCreatedAtDesc();

    /**
     * Busca secciones activos por corporación.
     * @param corporation la corporación
     * @return lista de secciones activos de la corporación
     */
    List<BasinSection> findByCorporationAndIsActiveTrue(Corporation corporation);
    
    /**
     * Busca una sección por ID y corporación.
     * @param id el ID de la sección
     * @param corporationId el ID de la corporación
     * @return la sección si existe y pertenece a la corporación
     */
    @Query("SELECT bs FROM BasinSection bs WHERE bs.id = :id AND bs.corporation.id = :corporationId")
    Optional<BasinSection> findByIdAndCorporationId(@Param("id") Long id, @Param("corporationId") Long corporationId);
}

package com.univercloud.hydro.repository;

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
 * Repository para la entidad WaterBasin.
 * Proporciona métodos de consulta para gestionar cuencas hidrográficas.
 */
@Repository
public interface WaterBasinRepository extends JpaRepository<WaterBasin, Long> {
    
    /**
     * Busca cuencas por corporación.
     * @param corporation la corporación
     * @return lista de cuencas de la corporación
     */
    List<WaterBasin> findByCorporation(Corporation corporation);
    
    /**
     * Busca cuencas activas.
     * @return lista de cuencas activas
     */
    List<WaterBasin> findByIsActiveTrue();
    
    /**
     * Busca cuencas inactivas.
     * @return lista de cuencas inactivas
     */
    List<WaterBasin> findByIsActiveFalse();
    
    /**
     * Busca cuencas activas por corporación.
     * @param corporation la corporación
     * @return lista de cuencas activas de la corporación
     */
    List<WaterBasin> findByCorporationAndIsActiveTrue(Corporation corporation);
    
    /**
     * Busca una cuenca por nombre.
     * @param name el nombre de la cuenca
     * @return la cuenca si existe
     */
    Optional<WaterBasin> findByName(String name);
    
    /**
     * Verifica si existe una cuenca con el nombre especificado.
     * @param name el nombre de la cuenca
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);
    
    /**
     * Busca cuencas por nombre (búsqueda parcial, case-insensitive).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de cuencas que coinciden con el nombre
     */
    @Query("SELECT wb FROM WaterBasin wb WHERE LOWER(wb.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<WaterBasin> findByNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * Busca cuencas activas por nombre (búsqueda parcial, case-insensitive).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de cuencas activas que coinciden con el nombre
     */
    @Query("SELECT wb FROM WaterBasin wb WHERE wb.isActive = true AND LOWER(wb.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<WaterBasin> findActiveByNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * Busca cuencas por corporación y nombre (búsqueda parcial, case-insensitive).
     * @param corporation la corporación
     * @param name el nombre o parte del nombre a buscar
     * @return lista de cuencas de la corporación que coinciden con el nombre
     */
    @Query("SELECT wb FROM WaterBasin wb WHERE wb.corporation = :corporation AND LOWER(wb.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<WaterBasin> findByCorporationAndNameContainingIgnoreCase(@Param("corporation") Corporation corporation, @Param("name") String name);
    
    /**
     * Busca cuencas creadas en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de cuencas creadas en el rango
     */
    List<WaterBasin> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Cuenta cuencas por corporación.
     * @param corporationId el ID de la corporación
     * @return número de cuencas de la corporación
     */
    @Query("SELECT COUNT(wb) FROM WaterBasin wb WHERE wb.corporation.id = :corporationId")
    long countByCorporationId(@Param("corporationId") Long corporationId);
    
    /**
     * Cuenta cuencas activas por corporación.
     * @param corporationId el ID de la corporación
     * @return número de cuencas activas de la corporación
     */
    @Query("SELECT COUNT(wb) FROM WaterBasin wb WHERE wb.corporation.id = :corporationId AND wb.isActive = true")
    long countActiveByCorporationId(@Param("corporationId") Long corporationId);
    
    /**
     * Busca cuencas ordenadas por fecha de creación (más recientes primero).
     * @return lista de cuencas ordenadas por fecha de creación descendente
     */
    @Query("SELECT wb FROM WaterBasin wb ORDER BY wb.createdAt DESC")
    List<WaterBasin> findAllOrderByCreatedAtDesc();
    
    /**
     * Busca cuencas activas ordenadas por nombre.
     * @return lista de cuencas activas ordenadas por nombre
     */
    @Query("SELECT wb FROM WaterBasin wb WHERE wb.isActive = true ORDER BY wb.name ASC")
    List<WaterBasin> findActiveOrderByName();
    
    /**
     * Busca cuencas por corporación ordenadas por nombre.
     * @param corporation la corporación
     * @return lista de cuencas de la corporación ordenadas por nombre
     */
    @Query("SELECT wb FROM WaterBasin wb WHERE wb.corporation = :corporation ORDER BY wb.name ASC")
    List<WaterBasin> findByCorporationOrderByName(@Param("corporation") Corporation corporation);
    
    /**
     * Busca cuencas activas por corporación ordenadas por nombre.
     * @param corporation la corporación
     * @return lista de cuencas activas de la corporación ordenadas por nombre
     */
    @Query("SELECT wb FROM WaterBasin wb WHERE wb.corporation = :corporation AND wb.isActive = true ORDER BY wb.name ASC")
    List<WaterBasin> findActiveByCorporationOrderByName(@Param("corporation") Corporation corporation);
    
    /**
     * Busca cuencas por corporación con paginación.
     * @param corporation la corporación
     * @param pageable parámetros de paginación
     * @return página de cuencas de la corporación
     */
    Page<WaterBasin> findByCorporation(Corporation corporation, Pageable pageable);
    
    /**
     * Busca cuencas por corporación creadas en un rango de fechas.
     * @param corporation la corporación
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de cuencas de la corporación creadas en el rango
     */
    List<WaterBasin> findByCorporationAndCreatedAtBetween(Corporation corporation, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Busca cuencas por corporación ordenadas por fecha de creación descendente.
     * @param corporation la corporación
     * @return lista de cuencas de la corporación ordenadas por fecha descendente
     */
    @Query("SELECT wb FROM WaterBasin wb WHERE wb.corporation = :corporation ORDER BY wb.createdAt DESC")
    List<WaterBasin> findByCorporationOrderByCreatedAtDesc(@Param("corporation") Corporation corporation);
    
    /**
     * Busca una cuenca por ID y corporación.
     * @param id el ID de la cuenca
     * @param corporationId el ID de la corporación
     * @return la cuenca si existe y pertenece a la corporación
     */
    @Query("SELECT wb FROM WaterBasin wb WHERE wb.id = :id AND wb.corporation.id = :corporationId")
    Optional<WaterBasin> findByIdAndCorporationId(@Param("id") Long id, @Param("corporationId") Long corporationId);
}

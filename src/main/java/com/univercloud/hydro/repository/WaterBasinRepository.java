package com.univercloud.hydro.repository;

import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.WaterBasin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
     * Verifica si existe una cuenca con el nombre especificado en la corporación (comparación case-insensitive).
     * @param corporation la corporación
     * @param name el nombre de la cuenca
     * @return true si existe, false en caso contrario
     */
    @Query("SELECT COUNT(wb) > 0 FROM WaterBasin wb WHERE wb.corporation = :corporation AND LOWER(wb.name) = LOWER(:name)")
    boolean existsByCorporationAndName(@Param("corporation") Corporation corporation, @Param("name") String name);
    
    /**
     * Verifica si existe una cuenca con el nombre especificado en la corporación, excluyendo un ID específico.
     * @param corporation la corporación
     * @param name el nombre de la cuenca
     * @param excludeId el ID a excluir de la búsqueda
     * @return true si existe, false en caso contrario
     */
    @Query("SELECT COUNT(wb) > 0 FROM WaterBasin wb WHERE wb.corporation = :corporation AND LOWER(wb.name) = LOWER(:name) AND wb.id != :excludeId")
    boolean existsByCorporationAndNameExcludingId(@Param("corporation") Corporation corporation, @Param("name") String name, @Param("excludeId") Long excludeId);
    
    /**
     * Busca cuencas por corporación y nombre (búsqueda parcial, case-insensitive).
     * @param corporation la corporación
     * @param name el nombre o parte del nombre a buscar
     * @return lista de cuencas de la corporación que coinciden con el nombre
     */
    @Query("SELECT wb FROM WaterBasin wb WHERE wb.corporation = :corporation AND LOWER(wb.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<WaterBasin> findByCorporationAndNameContainingIgnoreCase(@Param("corporation") Corporation corporation, @Param("name") String name);
    
    /**
     * Busca cuencas por corporación con paginación.
     * @param corporation la corporación
     * @param pageable parámetros de paginación
     * @return página de cuencas de la corporación
     */
    Page<WaterBasin> findByCorporation(Corporation corporation, Pageable pageable);
    
    /**
     * Busca una cuenca por ID y corporación.
     * @param id el ID de la cuenca
     * @param corporationId el ID de la corporación
     * @return la cuenca si existe y pertenece a la corporación
     */
    @Query("SELECT wb FROM WaterBasin wb WHERE wb.id = :id AND wb.corporation.id = :corporationId")
    Optional<WaterBasin> findByIdAndCorporationId(@Param("id") Long id, @Param("corporationId") Long corporationId);
    
    /**
     * Cuenta secciones de cuenca por cuenca hidrográfica.
     * @param waterBasinId el ID de la cuenca hidrográfica
     * @return número de secciones asociadas a la cuenca
     */
    @Query("SELECT COUNT(bs) FROM BasinSection bs WHERE bs.waterBasin.id = :waterBasinId")
    long countBasinSectionsByWaterBasinId(@Param("waterBasinId") Long waterBasinId);
}


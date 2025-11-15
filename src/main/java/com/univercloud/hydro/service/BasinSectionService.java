package com.univercloud.hydro.service;

import com.univercloud.hydro.entity.BasinSection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de Secciones de Cuenca.
 * Proporciona operaciones CRUD y lógica de negocio para secciones de cuenca.
 */
public interface BasinSectionService {
    
    /**
     * Crea una nueva sección de cuenca.
     * @param basinSection la sección de cuenca a crear
     * @return la sección de cuenca creada
     * @throws IllegalArgumentException si los datos son inválidos
     */
    BasinSection createBasinSection(BasinSection basinSection);
    
    /**
     * Actualiza una sección de cuenca existente.
     * @param basinSection la sección de cuenca a actualizar
     * @return la sección de cuenca actualizada
     * @throws IllegalArgumentException si la sección de cuenca no existe
     */
    BasinSection updateBasinSection(BasinSection basinSection);
    
    /**
     * Obtiene una sección de cuenca por su ID.
     * @param id el ID de la sección de cuenca
     * @return la sección de cuenca, si existe
     */
    Optional<BasinSection> getBasinSectionById(Long id);
    
    /**
     * Obtiene todas las secciones de cuenca de la corporación del usuario autenticado.
     * @param pageable parámetros de paginación
     * @return página de secciones de cuenca
     */
    Page<BasinSection> getMyCorporationBasinSections(Pageable pageable);
    
    /**
     * Obtiene todas las secciones de cuenca de la corporación del usuario autenticado.
     * @return lista de secciones de cuenca
     */
    List<BasinSection> getAllMyCorporationBasinSections();
    
    /**
     * Obtiene secciones de cuenca por cuenca hidrográfica.
     * @param waterBasinId el ID de la cuenca hidrográfica
     * @return lista de secciones de la cuenca
     */
    List<BasinSection> getBasinSectionsByWaterBasin(Long waterBasinId);
    
    /**
     * Busca secciones de cuenca por nombre (búsqueda parcial).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de secciones de cuenca que coinciden
     */
    List<BasinSection> searchBasinSectionsByName(String name);
    
    /**
     * Elimina una sección de cuenca.
     * @param id el ID de la sección de cuenca a eliminar
     * @return true si se eliminó correctamente
     * @throws IllegalArgumentException si la sección de cuenca no existe
     */
    boolean deleteBasinSection(Long id);
}

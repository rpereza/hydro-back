package com.univercloud.hydro.service;

import com.univercloud.hydro.entity.WaterBasin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de Cuencas Hidrográficas.
 * Proporciona operaciones CRUD y lógica de negocio para cuencas hidrográficas.
 */
public interface WaterBasinService {
    
    /**
     * Crea una nueva cuenca hidrográfica.
     * @param waterBasin la cuenca hidrográfica a crear
     * @return la cuenca hidrográfica creada
     * @throws IllegalArgumentException si los datos son inválidos
     */
    WaterBasin createWaterBasin(WaterBasin waterBasin);
    
    /**
     * Actualiza una cuenca hidrográfica existente.
     * @param waterBasin la cuenca hidrográfica a actualizar
     * @return la cuenca hidrográfica actualizada
     * @throws IllegalArgumentException si la cuenca hidrográfica no existe
     */
    WaterBasin updateWaterBasin(WaterBasin waterBasin);
    
    /**
     * Obtiene una cuenca hidrográfica por su ID.
     * @param id el ID de la cuenca hidrográfica
     * @return la cuenca hidrográfica, si existe
     */
    Optional<WaterBasin> getWaterBasinById(Long id);
    
    /**
     * Obtiene todas las cuencas hidrográficas de la corporación del usuario autenticado.
     * @param pageable parámetros de paginación
     * @return página de cuencas hidrográficas
     */
    Page<WaterBasin> getMyCorporationWaterBasins(Pageable pageable);
    
    /**
     * Obtiene todas las cuencas hidrográficas de la corporación del usuario autenticado.
     * @return lista de cuencas hidrográficas
     */
    List<WaterBasin> getAllMyCorporationWaterBasins();
    
    /**
     * Busca cuencas hidrográficas por nombre (búsqueda parcial).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de cuencas hidrográficas que coinciden
     */
    List<WaterBasin> searchWaterBasinsByName(String name);
    
    /**
     * Elimina una cuenca hidrográfica.
     * @param id el ID de la cuenca hidrográfica a eliminar
     * @return true si se eliminó correctamente
     * @throws IllegalArgumentException si la cuenca hidrográfica no existe
     */
    boolean deleteWaterBasin(Long id);
}

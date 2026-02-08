package com.univercloud.hydro.service;

import com.univercloud.hydro.dto.DischargeDto;
import com.univercloud.hydro.entity.Discharge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de Descargas.
 * Proporciona operaciones CRUD y lógica de negocio para descargas de agua.
 */
public interface DischargeService {
    
    /**
     * Crea una nueva descarga.
     * @param discharge la descarga a crear
     * @return la descarga creada
     * @throws IllegalArgumentException si los datos son inválidos
     */
    Discharge createDischarge(Discharge discharge);
    
    /**
     * Actualiza una descarga existente.
     * @param discharge la descarga a actualizar
     * @return la descarga actualizada
     * @throws IllegalArgumentException si la descarga no existe
     */
    Discharge updateDischarge(Discharge discharge);
    
    /**
     * Obtiene una descarga por su ID.
     * @param id el ID de la descarga
     * @return la descarga, si existe
     */
    Optional<Discharge> getDischargeById(Long id);
    
    /**
     * Obtiene todas las descargas de la corporación del usuario autenticado.
     * @param pageable parámetros de paginación
     * @return página de descargas como DTOs
     */
    Page<DischargeDto> getMyCorporationDischarges(Pageable pageable);
    
    /**
     * Obtiene descargas por usuario de descarga.
     * @param dischargeUserId el ID del usuario de descarga
     * @return lista de descargas del usuario como DTOs
     */
    List<DischargeDto> getDischargesByDischargeUser(Long dischargeUserId);
    
    /**
     * Busca descargas por nombre (búsqueda parcial).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de descargas que coinciden como DTOs
     */
    List<DischargeDto> searchDischargesByName(String name);
    
    /**
     * Elimina una descarga.
     * @param id el ID de la descarga a eliminar
     * @return true si se eliminó correctamente
     * @throws IllegalArgumentException si la descarga no existe
     */
    boolean deleteDischarge(Long id);
}

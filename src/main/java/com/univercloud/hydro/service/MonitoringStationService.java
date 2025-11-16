package com.univercloud.hydro.service;

import com.univercloud.hydro.entity.MonitoringStation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de Estaciones de Monitoreo.
 * Proporciona operaciones CRUD y lógica de negocio para estaciones de monitoreo.
 */
public interface MonitoringStationService {
    
    /**
     * Crea una nueva estación de monitoreo.
     * @param monitoringStation la estación de monitoreo a crear
     * @return la estación de monitoreo creada
     * @throws IllegalArgumentException si los datos son inválidos
     */
    MonitoringStation createMonitoringStation(MonitoringStation monitoringStation);
    
    /**
     * Actualiza una estación de monitoreo existente.
     * @param monitoringStation la estación de monitoreo a actualizar
     * @return la estación de monitoreo actualizada
     * @throws IllegalArgumentException si la estación de monitoreo no existe
     */
    MonitoringStation updateMonitoringStation(MonitoringStation monitoringStation);
    
    /**
     * Obtiene una estación de monitoreo por su ID.
     * @param id el ID de la estación de monitoreo
     * @return la estación de monitoreo, si existe
     */
    Optional<MonitoringStation> getMonitoringStationById(Long id);
    
    /**
     * Obtiene todas las estaciones de monitoreo de la corporación del usuario autenticado.
     * @param pageable parámetros de paginación
     * @return página de estaciones de monitoreo
     */
    Page<MonitoringStation> getMyCorporationMonitoringStations(Pageable pageable);
    
    /**
     * Obtiene todas las estaciones de monitoreo de la corporación del usuario autenticado.
     * @return lista de estaciones de monitoreo
     */
    List<MonitoringStation> getAllMyCorporationMonitoringStations();
    
    /**
     * Busca estaciones de monitoreo por nombre (búsqueda parcial).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de estaciones de monitoreo que coinciden
     */
    List<MonitoringStation> searchMonitoringStationsByName(String name);
    
    /**
     * Elimina una estación de monitoreo.
     * @param id el ID de la estación de monitoreo a eliminar
     * @return true si se eliminó correctamente
     * @throws IllegalArgumentException si la estación de monitoreo no existe
     */
    boolean deleteMonitoringStation(Long id);
}

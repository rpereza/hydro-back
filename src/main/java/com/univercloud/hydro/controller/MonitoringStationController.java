package com.univercloud.hydro.controller;

import com.univercloud.hydro.entity.MonitoringStation;
import com.univercloud.hydro.service.MonitoringStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de Estaciones de Monitoreo.
 * Proporciona endpoints para operaciones CRUD y consultas de estaciones de monitoreo.
 */
@RestController
@RequestMapping("/api/monitoring-stations")
@CrossOrigin(origins = "*")
public class MonitoringStationController {
    
    @Autowired
    private MonitoringStationService monitoringStationService;
    
    /**
     * Crea una nueva estación de monitoreo.
     * 
     * @param monitoringStation la estación de monitoreo a crear
     * @return la estación de monitoreo creada
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MonitoringStation> createMonitoringStation(@Valid @RequestBody MonitoringStation monitoringStation) {
        try {
            MonitoringStation createdMonitoringStation = monitoringStationService.createMonitoringStation(monitoringStation);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMonitoringStation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Actualiza una estación de monitoreo existente.
     * 
     * @param id el ID de la estación de monitoreo
     * @param monitoringStation la estación de monitoreo actualizada
     * @return la estación de monitoreo actualizada
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MonitoringStation> updateMonitoringStation(@PathVariable Long id, @Valid @RequestBody MonitoringStation monitoringStation) {
        try {
            monitoringStation.setId(id);
            MonitoringStation updatedMonitoringStation = monitoringStationService.updateMonitoringStation(monitoringStation);
            return ResponseEntity.ok(updatedMonitoringStation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene una estación de monitoreo por su ID.
     * 
     * @param id el ID de la estación de monitoreo
     * @return la estación de monitoreo si existe
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MonitoringStation> getMonitoringStationById(@PathVariable Long id) {
        Optional<MonitoringStation> monitoringStation = monitoringStationService.getMonitoringStationById(id);
        return monitoringStation.map(ResponseEntity::ok)
                               .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtiene todas las estaciones de monitoreo de la corporación con paginación.
     * 
     * @param pageable parámetros de paginación (page, size, sort)
     * @return página de estaciones de monitoreo
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<MonitoringStation>> getAllMonitoringStations(Pageable pageable) {
        try {
            Page<MonitoringStation> monitoringStations = monitoringStationService.getMyCorporationMonitoringStations(pageable);
            return ResponseEntity.ok(monitoringStations);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Busca estaciones de monitoreo por nombre (búsqueda parcial).
     * 
     * @param name el nombre o parte del nombre a buscar
     * @return lista de estaciones de monitoreo que coinciden
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<MonitoringStation>> searchMonitoringStationsByName(@RequestParam String name) {
        try {
            List<MonitoringStation> monitoringStations = monitoringStationService.searchMonitoringStationsByName(name);
            return ResponseEntity.ok(monitoringStations);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Elimina una estación de monitoreo.
     * 
     * @param id el ID de la estación de monitoreo a eliminar
     * @return true si se eliminó correctamente
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> deleteMonitoringStation(@PathVariable Long id) {
        try {
            boolean deleted = monitoringStationService.deleteMonitoringStation(id);
            return ResponseEntity.ok(deleted);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
}

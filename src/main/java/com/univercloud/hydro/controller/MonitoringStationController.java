package com.univercloud.hydro.controller;

import com.univercloud.hydro.entity.MonitoringStation;
import com.univercloud.hydro.service.MonitoringStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
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
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<MonitoringStation> getMonitoringStationById(@PathVariable Long id) {
        Optional<MonitoringStation> monitoringStation = monitoringStationService.getMonitoringStationById(id);
        return monitoringStation.map(ResponseEntity::ok)
                               .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtiene todas las estaciones de monitoreo de la corporación.
     * 
     * @return lista de estaciones de monitoreo
     */
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<MonitoringStation>> getAllMonitoringStations() {
        try {
            List<MonitoringStation> monitoringStations = monitoringStationService.getAllMyCorporationMonitoringStations();
            return ResponseEntity.ok(monitoringStations);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Busca una estación de monitoreo por nombre.
     * 
     * @param name el nombre de la estación de monitoreo
     * @return la estación de monitoreo si existe
     */
    @GetMapping("/by-name")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<MonitoringStation> getMonitoringStationByName(@RequestParam String name) {
        Optional<MonitoringStation> monitoringStation = monitoringStationService.getMonitoringStationByName(name);
        return monitoringStation.map(ResponseEntity::ok)
                               .orElse(ResponseEntity.notFound().build());
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
     * Obtiene estaciones de monitoreo creadas en un rango de fechas.
     * 
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de estaciones de monitoreo creadas en el rango
     */
    @GetMapping("/by-date-range")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<MonitoringStation>> getMonitoringStationsByDateRange(
            @RequestParam LocalDateTime startDate, 
            @RequestParam LocalDateTime endDate) {
        try {
            List<MonitoringStation> monitoringStations = monitoringStationService.getMonitoringStationsByDateRange(startDate, endDate);
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
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
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
    
    /**
     * Verifica si existe una estación de monitoreo con el nombre especificado.
     * 
     * @param name el nombre de la estación de monitoreo
     * @return true si existe, false en caso contrario
     */
    @GetMapping("/exists")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> existsByName(@RequestParam String name) {
        try {
            boolean exists = monitoringStationService.existsByName(name);
            return ResponseEntity.ok(exists);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene estaciones de monitoreo ordenadas por fecha de creación (más recientes primero).
     * 
     * @return lista de estaciones de monitoreo ordenadas por fecha de creación descendente
     */
    @GetMapping("/recent")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<MonitoringStation>> getMonitoringStationsOrderByCreatedAtDesc() {
        try {
            List<MonitoringStation> monitoringStations = monitoringStationService.getMonitoringStationsOrderByCreatedAtDesc();
            return ResponseEntity.ok(monitoringStations);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene estaciones de monitoreo ordenadas por nombre.
     * 
     * @return lista de estaciones de monitoreo ordenadas por nombre
     */
    @GetMapping("/ordered-by-name")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<MonitoringStation>> getMonitoringStationsOrderByName() {
        try {
            List<MonitoringStation> monitoringStations = monitoringStationService.getMonitoringStationsOrderByName();
            return ResponseEntity.ok(monitoringStations);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
}

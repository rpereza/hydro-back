package com.univercloud.hydro.controller;

import com.univercloud.hydro.entity.Monitoring;
import com.univercloud.hydro.service.MonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Optional;

/**
 * Controlador REST para la gestión de Monitoreos.
 * Proporciona endpoints para operaciones CRUD y consultas de monitoreos.
 */
@RestController
@RequestMapping("/api/monitoring")
@CrossOrigin(origins = "*")
public class MonitoringController {
    
    @Autowired
    private MonitoringService monitoringService;
    
    /**
     * Crea un nuevo monitoreo.
     * 
     * @param monitoring el monitoreo a crear
     * @return el monitoreo creado
     */
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Monitoring> createMonitoring(@Valid @RequestBody Monitoring monitoring) {
        try {
            Monitoring createdMonitoring = monitoringService.createMonitoring(monitoring);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMonitoring);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Actualiza un monitoreo existente.
     * 
     * @param id el ID del monitoreo
     * @param monitoring el monitoreo actualizado
     * @return el monitoreo actualizado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Monitoring> updateMonitoring(@PathVariable Long id, @Valid @RequestBody Monitoring monitoring) {
        try {
            monitoring.setId(id);
            Monitoring updatedMonitoring = monitoringService.updateMonitoring(monitoring);
            return ResponseEntity.ok(updatedMonitoring);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene un monitoreo por su ID.
     * 
     * @param id el ID del monitoreo
     * @return el monitoreo si existe
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Monitoring> getMonitoringById(@PathVariable Long id) {
        Optional<Monitoring> monitoring = monitoringService.getMonitoringById(id);
        return monitoring.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtiene todos los monitoreos de la corporación con paginación.
     * 
     * @param pageable parámetros de paginación (page, size, sort)
     * @return página de monitoreos
     */
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<Monitoring>> getMyCorporationMonitorings(Pageable pageable) {
        try {
            Page<Monitoring> monitorings = monitoringService.getMyCorporationMonitorings(pageable);
            return ResponseEntity.ok(monitorings);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene monitoreos por estación de monitoreo con paginación.
     * 
     * @param monitoringStationId el ID de la estación de monitoreo
     * @param pageable parámetros de paginación (page, size, sort)
     * @return página de monitoreos de la estación
     */
    @GetMapping("/by-station/{monitoringStationId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<Monitoring>> getMonitoringsByStation(@PathVariable Long monitoringStationId, Pageable pageable) {
        try {
            Page<Monitoring> monitorings = monitoringService.getMonitoringsByStation(monitoringStationId, pageable);
            return ResponseEntity.ok(monitorings);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Elimina un monitoreo.
     * 
     * @param id el ID del monitoreo a eliminar
     * @return true si se eliminó correctamente
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Boolean> deleteMonitoring(@PathVariable Long id) {
        try {
            boolean deleted = monitoringService.deleteMonitoring(id);
            return ResponseEntity.ok(deleted);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
}

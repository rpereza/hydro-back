package com.univercloud.hydro.controller;

import com.univercloud.hydro.entity.DischargeMonitoring;
import com.univercloud.hydro.service.DischargeMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de Monitoreo de Descargas.
 * Proporciona endpoints para operaciones CRUD y consultas de monitoreo de descargas.
 */
@RestController
@RequestMapping("/api/discharge-monitoring")
@CrossOrigin(origins = "*")
public class DischargeMonitoringController {
    
    @Autowired
    private DischargeMonitoringService dischargeMonitoringService;
    
    /**
     * Crea un nuevo monitoreo de descarga.
     * 
     * @param dischargeMonitoring el monitoreo de descarga a crear
     * @return el monitoreo de descarga creado
     */
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<DischargeMonitoring> createDischargeMonitoring(@Valid @RequestBody DischargeMonitoring dischargeMonitoring) {
        try {
            DischargeMonitoring createdDischargeMonitoring = dischargeMonitoringService.createDischargeMonitoring(dischargeMonitoring);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDischargeMonitoring);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Actualiza un monitoreo de descarga existente.
     * 
     * @param id el ID del monitoreo de descarga
     * @param dischargeMonitoring el monitoreo de descarga actualizado
     * @return el monitoreo de descarga actualizado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<DischargeMonitoring> updateDischargeMonitoring(@PathVariable Long id, @Valid @RequestBody DischargeMonitoring dischargeMonitoring) {
        try {
            dischargeMonitoring.setId(id);
            DischargeMonitoring updatedDischargeMonitoring = dischargeMonitoringService.updateDischargeMonitoring(dischargeMonitoring);
            return ResponseEntity.ok(updatedDischargeMonitoring);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene un monitoreo de descarga por su ID.
     * 
     * @param id el ID del monitoreo de descarga
     * @return el monitoreo de descarga si existe
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<DischargeMonitoring> getDischargeMonitoringById(@PathVariable Long id) {
        Optional<DischargeMonitoring> dischargeMonitoring = dischargeMonitoringService.getDischargeMonitoringById(id);
        return dischargeMonitoring.map(ResponseEntity::ok)
                                 .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtiene todos los monitoreos de descarga de la corporación.
     * 
     * @return lista de monitoreos de descarga
     */
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<DischargeMonitoring>> getAllDischargeMonitoring() {
        try {
            List<DischargeMonitoring> dischargeMonitoring = dischargeMonitoringService.getAllMyCorporationDischargeMonitorings();
            return ResponseEntity.ok(dischargeMonitoring);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Elimina un monitoreo de descarga.
     * 
     * @param id el ID del monitoreo de descarga a eliminar
     * @return true si se eliminó correctamente
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> deleteDischargeMonitoring(@PathVariable Long id) {
        try {
            boolean deleted = dischargeMonitoringService.deleteDischargeMonitoring(id);
            return ResponseEntity.ok(deleted);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
}

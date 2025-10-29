package com.univercloud.hydro.controller;

import com.univercloud.hydro.entity.EconomicActivity;
import com.univercloud.hydro.service.EconomicActivityService;
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
 * Controlador REST para la gestión de Actividades Económicas.
 * Proporciona endpoints para operaciones CRUD y consultas de actividades económicas.
 */
@RestController
@RequestMapping("/api/economic-activities")
@CrossOrigin(origins = "*")
public class EconomicActivityController {
    
    @Autowired
    private EconomicActivityService economicActivityService;
    
    /**
     * Crea una nueva actividad económica.
     * 
     * @param economicActivity la actividad económica a crear
     * @return la actividad económica creada
     */
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<EconomicActivity> createEconomicActivity(@Valid @RequestBody EconomicActivity economicActivity) {
        try {
            EconomicActivity createdEconomicActivity = economicActivityService.createEconomicActivity(economicActivity);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEconomicActivity);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Actualiza una actividad económica existente.
     * 
     * @param id el ID de la actividad económica
     * @param economicActivity la actividad económica actualizada
     * @return la actividad económica actualizada
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<EconomicActivity> updateEconomicActivity(@PathVariable Long id, @Valid @RequestBody EconomicActivity economicActivity) {
        try {
            economicActivity.setId(id);
            EconomicActivity updatedEconomicActivity = economicActivityService.updateEconomicActivity(economicActivity);
            return ResponseEntity.ok(updatedEconomicActivity);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene una actividad económica por su ID.
     * 
     * @param id el ID de la actividad económica
     * @return la actividad económica si existe
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<EconomicActivity> getEconomicActivityById(@PathVariable Long id) {
        Optional<EconomicActivity> economicActivity = economicActivityService.getEconomicActivityById(id);
        return economicActivity.map(ResponseEntity::ok)
                              .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtiene todas las actividades económicas.
     * 
     * @return lista de actividades económicas
     */
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<EconomicActivity>> getAllEconomicActivities() {
        try {
            List<EconomicActivity> economicActivities = economicActivityService.getAllEconomicActivities();
            return ResponseEntity.ok(economicActivities);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Busca una actividad económica por nombre.
     * 
     * @param name el nombre de la actividad económica
     * @return la actividad económica si existe
     */
    @GetMapping("/by-name")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<EconomicActivity> getEconomicActivityByName(@RequestParam String name) {
        Optional<EconomicActivity> economicActivity = economicActivityService.getEconomicActivityByName(name);
        return economicActivity.map(ResponseEntity::ok)
                              .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Busca actividades económicas por nombre (búsqueda parcial).
     * 
     * @param name el nombre o parte del nombre a buscar
     * @return lista de actividades económicas que coinciden
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<EconomicActivity>> searchEconomicActivitiesByName(@RequestParam String name) {
        try {
            List<EconomicActivity> economicActivities = economicActivityService.searchEconomicActivitiesByName(name);
            return ResponseEntity.ok(economicActivities);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene actividades económicas creadas en un rango de fechas.
     * 
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de actividades económicas creadas en el rango
     */
    @GetMapping("/by-date-range")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<EconomicActivity>> getEconomicActivitiesByDateRange(
            @RequestParam LocalDateTime startDate, 
            @RequestParam LocalDateTime endDate) {
        try {
            List<EconomicActivity> economicActivities = economicActivityService.getEconomicActivitiesByDateRange(startDate, endDate);
            return ResponseEntity.ok(economicActivities);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Elimina una actividad económica.
     * 
     * @param id el ID de la actividad económica a eliminar
     * @return true si se eliminó correctamente
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> deleteEconomicActivity(@PathVariable Long id) {
        try {
            boolean deleted = economicActivityService.deleteEconomicActivity(id);
            return ResponseEntity.ok(deleted);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Verifica si existe una actividad económica con el nombre especificado.
     * 
     * @param name el nombre de la actividad económica
     * @return true si existe, false en caso contrario
     */
    @GetMapping("/exists")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> existsByName(@RequestParam String name) {
        try {
            boolean exists = economicActivityService.existsByName(name);
            return ResponseEntity.ok(exists);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene actividades económicas ordenadas por fecha de creación (más recientes primero).
     * 
     * @return lista de actividades económicas ordenadas por fecha de creación descendente
     */
    @GetMapping("/recent")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<EconomicActivity>> getEconomicActivitiesOrderByCreatedAtDesc() {
        try {
            List<EconomicActivity> economicActivities = economicActivityService.getEconomicActivitiesOrderByCreatedAtDesc();
            return ResponseEntity.ok(economicActivities);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
}

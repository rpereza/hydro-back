package com.univercloud.hydro.controller;

import com.univercloud.hydro.entity.EconomicActivity;
import com.univercloud.hydro.service.EconomicActivityService;
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
    @PreAuthorize("hasRole('SUPER_ADMIN')")
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
    @PreAuthorize("hasRole('SUPER_ADMIN')")
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
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<EconomicActivity> getEconomicActivityById(@PathVariable Long id) {
        Optional<EconomicActivity> economicActivity = economicActivityService.getEconomicActivityById(id);
        return economicActivity.map(ResponseEntity::ok)
                              .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtiene todas las actividades económicas con paginación.
     * 
     * @param pageable parámetros de paginación
     * @return página de actividades económicas
     */
    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Page<EconomicActivity>> getAllEconomicActivities(Pageable pageable) {
        try {
            Page<EconomicActivity> economicActivities = economicActivityService.getAllEconomicActivities(pageable);
            return ResponseEntity.ok(economicActivities);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
        
    /**
     * Busca actividades económicas por nombre (búsqueda parcial) con paginación.
     * 
     * @param name el nombre o parte del nombre a buscar
     * @param pageable parámetros de paginación
     * @return página de actividades económicas que coinciden
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Page<EconomicActivity>> searchEconomicActivitiesByName(@RequestParam String name, Pageable pageable) {
        try {
            Page<EconomicActivity> economicActivities = economicActivityService.searchEconomicActivitiesByName(name, pageable);
            return ResponseEntity.ok(economicActivities);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Busca actividades económicas cuyo código comience con el código dado con paginación.
     * 
     * @param code el código o parte inicial del código a buscar
     * @param pageable parámetros de paginación
     * @return página de actividades económicas cuyo código comienza con el código dado
     */
    @GetMapping("/search-by-code")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Page<EconomicActivity>> searchEconomicActivitiesByCode(@RequestParam String code, Pageable pageable) {
        try {
            Page<EconomicActivity> economicActivities = economicActivityService.searchEconomicActivitiesByCode(code, pageable);
            return ResponseEntity.ok(economicActivities);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Búsqueda unificada de actividades económicas por código o nombre con paginación.
     * Busca en ambos campos simultáneamente.
     * 
     * @param query el término de búsqueda (código o nombre)
     * @param pageable parámetros de paginación
     * @return página de actividades económicas que coinciden con el código o nombre
     */
    @GetMapping("/search-unified")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<EconomicActivity>> searchEconomicActivities(
            @RequestParam String query, 
            Pageable pageable) {
        try {
            Page<EconomicActivity> economicActivities = economicActivityService
                    .searchEconomicActivitiesByCodeOrName(query, pageable);
            return ResponseEntity.ok(economicActivities);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

}

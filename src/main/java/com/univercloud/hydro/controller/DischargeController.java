package com.univercloud.hydro.controller;

import com.univercloud.hydro.entity.Discharge;
import com.univercloud.hydro.service.DischargeService;
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
 * Controlador REST para la gestión de Descargas.
 * Proporciona endpoints para operaciones CRUD y consultas de descargas de agua.
 */
@RestController
@RequestMapping("/api/discharges")
@CrossOrigin(origins = "*")
public class DischargeController {
    
    @Autowired
    private DischargeService dischargeService;
    
    /**
     * Crea una nueva descarga.
     * 
     * @param discharge la descarga a crear
     * @return la descarga creada
     */
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Discharge> createDischarge(@Valid @RequestBody Discharge discharge) {
        try {
            Discharge createdDischarge = dischargeService.createDischarge(discharge);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDischarge);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Actualiza una descarga existente.
     * 
     * @param id el ID de la descarga
     * @param discharge la descarga actualizada
     * @return la descarga actualizada
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Discharge> updateDischarge(@PathVariable Long id, @Valid @RequestBody Discharge discharge) {
        try {
            discharge.setId(id);
            Discharge updatedDischarge = dischargeService.updateDischarge(discharge);
            return ResponseEntity.ok(updatedDischarge);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene una descarga por su ID.
     * 
     * @param id el ID de la descarga
     * @return la descarga si existe
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Discharge> getDischargeById(@PathVariable Long id) {
        Optional<Discharge> discharge = dischargeService.getDischargeById(id);
        return discharge.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtiene todas las descargas de la corporación del usuario autenticado con paginación.
     * 
     * @param pageable parámetros de paginación
     * @return página de descargas
     */
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<Discharge>> getMyCorporationDischarges(Pageable pageable) {
        try {
            Page<Discharge> discharges = dischargeService.getMyCorporationDischarges(pageable);
            return ResponseEntity.ok(discharges);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene descargas por usuario de descarga.
     * 
     * @param dischargeUserId el ID del usuario de descarga
     * @return lista de descargas del usuario
     */
    @GetMapping("/by-discharge-user/{dischargeUserId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Discharge>> getDischargesByDischargeUser(@PathVariable Long dischargeUserId) {
        try {
            List<Discharge> discharges = dischargeService.getDischargesByDischargeUser(dischargeUserId);
            return ResponseEntity.ok(discharges);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene descargas por año.
     * 
     * @param year el año
     * @return lista de descargas del año
     */
    @GetMapping("/by-year/{year}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Discharge>> getDischargesByYear(@PathVariable Integer year) {
        try {
            List<Discharge> discharges = dischargeService.getDischargesByYear(year);
            return ResponseEntity.ok(discharges);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
        
    /**
     * Busca una descarga por número y año.
     * 
     * @param number el número de descarga
     * @param year el año
     * @return la descarga si existe
     */
    @GetMapping("/by-number-year")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Discharge> getDischargeByNumberAndYear(@RequestParam Integer number, @RequestParam Integer year) {
        Optional<Discharge> discharge = dischargeService.getDischargeByNumberAndYear(number, year);
        return discharge.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Busca descargas por nombre (búsqueda parcial).
     * 
     * @param name el nombre o parte del nombre a buscar
     * @return lista de descargas que coinciden
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Discharge>> searchDischargesByName(@RequestParam String name) {
        try {
            List<Discharge> discharges = dischargeService.searchDischargesByName(name);
            return ResponseEntity.ok(discharges);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Elimina una descarga.
     * 
     * @param id el ID de la descarga a eliminar
     * @return true si se eliminó correctamente
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Boolean> deleteDischarge(@PathVariable Long id) {
        try {
            boolean deleted = dischargeService.deleteDischarge(id);
            return ResponseEntity.ok(deleted);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

}

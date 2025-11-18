package com.univercloud.hydro.controller;

import com.univercloud.hydro.entity.DischargeParameter;
import com.univercloud.hydro.service.DischargeParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de Parámetros de Descarga.
 * Proporciona endpoints para operaciones CRUD y consultas de parámetros de descarga.
 */
@RestController
@RequestMapping("/api/discharge-parameters")
@CrossOrigin(origins = "*")
public class DischargeParameterController {
    
    @Autowired
    private DischargeParameterService dischargeParameterService;
    
    /**
     * Crea un nuevo parámetro de descarga.
     * 
     * @param dischargeParameter el parámetro de descarga a crear
     * @return el parámetro de descarga creado
     */
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<DischargeParameter> createDischargeParameter(@Valid @RequestBody DischargeParameter dischargeParameter) {
        try {
            DischargeParameter createdDischargeParameter = dischargeParameterService.createDischargeParameter(dischargeParameter);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDischargeParameter);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Actualiza un parámetro de descarga existente.
     * 
     * @param id el ID del parámetro de descarga
     * @param dischargeParameter el parámetro de descarga actualizado
     * @return el parámetro de descarga actualizado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<DischargeParameter> updateDischargeParameter(@PathVariable Long id, @Valid @RequestBody DischargeParameter dischargeParameter) {
        try {
            dischargeParameter.setId(id);
            DischargeParameter updatedDischargeParameter = dischargeParameterService.updateDischargeParameter(dischargeParameter);
            return ResponseEntity.ok(updatedDischargeParameter);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene un parámetro de descarga por su ID.
     * 
     * @param id el ID del parámetro de descarga
     * @return el parámetro de descarga si existe
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<DischargeParameter> getDischargeParameterById(@PathVariable Long id) {
        Optional<DischargeParameter> dischargeParameter = dischargeParameterService.getDischargeParameterById(id);
        return dischargeParameter.map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtiene todos los parámetros de descarga de la corporación.
     * 
     * @return lista de parámetros de descarga
     */
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<DischargeParameter>> getAllDischargeParameters() {
        try {
            List<DischargeParameter> dischargeParameters = dischargeParameterService.getAllMyCorporationDischargeParameters();
            return ResponseEntity.ok(dischargeParameters);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    
    /**
     * Elimina un parámetro de descarga.
     * 
     * @param id el ID del parámetro de descarga a eliminar
     * @return true si se eliminó correctamente
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> deleteDischargeParameter(@PathVariable Long id) {
        try {
            boolean deleted = dischargeParameterService.deleteDischargeParameter(id);
            return ResponseEntity.ok(deleted);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
}

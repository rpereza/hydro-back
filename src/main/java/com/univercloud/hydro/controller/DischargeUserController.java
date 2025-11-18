package com.univercloud.hydro.controller;

import com.univercloud.hydro.entity.DischargeUser;
import com.univercloud.hydro.service.DischargeUserService;
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
 * Controlador REST para la gestión de Usuarios de Descarga.
 * Proporciona endpoints para operaciones CRUD y consultas de usuarios de descarga.
 */
@RestController
@RequestMapping("/api/discharge-users")
@CrossOrigin(origins = "*")
public class DischargeUserController {
    
    @Autowired
    private DischargeUserService dischargeUserService;
    
    /**
     * Crea un nuevo usuario de descarga.
     * 
     * @param dischargeUser el usuario de descarga a crear
     * @return el usuario de descarga creado
     */
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<DischargeUser> createDischargeUser(@Valid @RequestBody DischargeUser dischargeUser) {
        try {
            DischargeUser createdDischargeUser = dischargeUserService.createDischargeUser(dischargeUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDischargeUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Actualiza un usuario de descarga existente.
     * 
     * @param id el ID del usuario de descarga
     * @param dischargeUser el usuario de descarga actualizado
     * @return el usuario de descarga actualizado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<DischargeUser> updateDischargeUser(@PathVariable Long id, @Valid @RequestBody DischargeUser dischargeUser) {
        try {
            dischargeUser.setId(id);
            DischargeUser updatedDischargeUser = dischargeUserService.updateDischargeUser(dischargeUser);
            return ResponseEntity.ok(updatedDischargeUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene un usuario de descarga por su ID.
     * 
     * @param id el ID del usuario de descarga
     * @return el usuario de descarga si existe
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<DischargeUser> getDischargeUserById(@PathVariable Long id) {
        Optional<DischargeUser> dischargeUser = dischargeUserService.getDischargeUserById(id);
        return dischargeUser.map(ResponseEntity::ok)
                           .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtiene todos los usuarios de descarga de la corporación con paginación.
     * 
     * @param pageable parámetros de paginación
     * @return página de usuarios de descarga
     */
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<DischargeUser>> getAllDischargeUsers(Pageable pageable) {
        try {
            Page<DischargeUser> dischargeUsers = dischargeUserService.getMyCorporationDischargeUsers(pageable);
            return ResponseEntity.ok(dischargeUsers);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Busca usuarios de descarga por nombre de empresa (búsqueda parcial).
     * 
     * @param companyName el nombre o parte del nombre de empresa a buscar
     * @return lista de usuarios de descarga que coinciden
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<DischargeUser>> searchDischargeUserByCompanyName(@RequestParam String companyName) {
        try {
            List<DischargeUser> dischargeUsers = dischargeUserService.searchDischargeUsersByCompanyName(companyName);
            return ResponseEntity.ok(dischargeUsers);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Busca usuarios de descarga por nombre de empresa (búsqueda parcial) que sean empresas de servicio público.
     * 
     * @param companyName el nombre o parte del nombre de empresa a buscar
     * @return lista de usuarios de descarga que coinciden y son empresas de servicio público
     */
    @GetMapping("/search/public-service")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<DischargeUser>> searchPublicServiceCompanyDischargeUserByCompanyName(@RequestParam String companyName) {
        try {
            List<DischargeUser> dischargeUsers = dischargeUserService.searchPublicServiceCompanyDischargeUsersByCompanyName(companyName);
            return ResponseEntity.ok(dischargeUsers);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Elimina un usuario de descarga.
     * 
     * @param id el ID del usuario de descarga a eliminar
     * @return true si se eliminó correctamente
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Boolean> deleteDischargeUser(@PathVariable Long id) {
        try {
            boolean deleted = dischargeUserService.deleteDischargeUser(id);
            return ResponseEntity.ok(deleted);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
}

package com.univercloud.hydro.controller;

import com.univercloud.hydro.entity.AuthorizationType;
import com.univercloud.hydro.service.AuthorizationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de Tipos de Autorización.
 * Proporciona endpoints para operaciones CRUD y consultas de tipos de autorización.
 */
@RestController
@RequestMapping("/api/authorization-types")
@CrossOrigin(origins = "*")
public class AuthorizationTypeController {
    
    @Autowired
    private AuthorizationTypeService authorizationTypeService;
    
    /**
     * Crea un nuevo tipo de autorización.
     * 
     * @param authorizationType el tipo de autorización a crear
     * @return el tipo de autorización creado
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorizationType> createAuthorizationType(@Valid @RequestBody AuthorizationType authorizationType) {
        try {
            AuthorizationType createdAuthorizationType = authorizationTypeService.createAuthorizationType(authorizationType);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthorizationType);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Actualiza un tipo de autorización existente.
     * 
     * @param id el ID del tipo de autorización
     * @param authorizationType el tipo de autorización actualizado
     * @return el tipo de autorización actualizado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorizationType> updateAuthorizationType(@PathVariable Long id, @Valid @RequestBody AuthorizationType authorizationType) {
        try {
            authorizationType.setId(id);
            AuthorizationType updatedAuthorizationType = authorizationTypeService.updateAuthorizationType(authorizationType);
            return ResponseEntity.ok(updatedAuthorizationType);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Obtiene un tipo de autorización por su ID.
     * 
     * @param id el ID del tipo de autorización
     * @return el tipo de autorización si existe
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<AuthorizationType> getAuthorizationTypeById(@PathVariable Long id) {
        Optional<AuthorizationType> authorizationType = authorizationTypeService.getAuthorizationTypeById(id);
        return authorizationType.map(ResponseEntity::ok)
                              .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtiene todos los tipos de autorización de la corporación con paginación.
     * 
     * @param pageable parámetros de paginación
     * @return página de tipos de autorización
     */
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Page<AuthorizationType>> getMyCorporationAuthorizationTypes(Pageable pageable) {
        try {
            Page<AuthorizationType> authorizationTypes = authorizationTypeService.getMyCorporationAuthorizationTypes(pageable);
            return ResponseEntity.ok(authorizationTypes);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene todos los tipos de autorización de la corporación.
     * 
     * @return lista de tipos de autorización
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<AuthorizationType>> getAllMyCorporationAuthorizationTypes() {
        try {
            List<AuthorizationType> authorizationTypes = authorizationTypeService.getAllMyCorporationAuthorizationTypes();
            return ResponseEntity.ok(authorizationTypes);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Busca un tipo de autorización por nombre.
     * 
     * @param name el nombre del tipo de autorización
     * @return el tipo de autorización si existe
     */
    @GetMapping("/by-name")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<AuthorizationType> getAuthorizationTypeByName(@RequestParam String name) {
        Optional<AuthorizationType> authorizationType = authorizationTypeService.getAuthorizationTypeByName(name);
        return authorizationType.map(ResponseEntity::ok)
                              .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Busca tipos de autorización por nombre (búsqueda parcial).
     * 
     * @param name el nombre o parte del nombre a buscar
     * @return lista de tipos de autorización que coinciden
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<AuthorizationType>> searchAuthorizationTypesByName(@RequestParam String name) {
        List<AuthorizationType> authorizationTypes = authorizationTypeService.searchAuthorizationTypesByName(name);
        return ResponseEntity.ok(authorizationTypes);
    }
    
    /**
     * Obtiene tipos de autorización creados en un rango de fechas.
     * 
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de tipos de autorización creados en el rango
     */
    @GetMapping("/by-date-range")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<AuthorizationType>> getAuthorizationTypesByDateRange(
            @RequestParam LocalDateTime startDate, 
            @RequestParam LocalDateTime endDate) {
        List<AuthorizationType> authorizationTypes = authorizationTypeService.getAuthorizationTypesByDateRange(startDate, endDate);
        return ResponseEntity.ok(authorizationTypes);
    }
    
    /**
     * Elimina un tipo de autorización.
     * 
     * @param id el ID del tipo de autorización a eliminar
     * @return true si se eliminó correctamente
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> deleteAuthorizationType(@PathVariable Long id) {
        try {
            boolean deleted = authorizationTypeService.deleteAuthorizationType(id);
            return ResponseEntity.ok(deleted);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Verifica si existe un tipo de autorización con el nombre especificado.
     * 
     * @param name el nombre del tipo de autorización
     * @return true si existe, false en caso contrario
     */
    @GetMapping("/exists")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> existsByName(@RequestParam String name) {
        boolean exists = authorizationTypeService.existsByName(name);
        return ResponseEntity.ok(exists);
    }
    
    /**
     * Obtiene tipos de autorización ordenados por fecha de creación (más recientes primero).
     * 
     * @return lista de tipos de autorización ordenados por fecha de creación descendente
     */
    @GetMapping("/recent")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<AuthorizationType>> getAuthorizationTypesOrderByCreatedAtDesc() {
        List<AuthorizationType> authorizationTypes = authorizationTypeService.getAuthorizationTypesOrderByCreatedAtDesc();
        return ResponseEntity.ok(authorizationTypes);
    }
    
    /**
     * Obtiene tipos de autorización de la corporación ordenados por nombre.
     * 
     * @return lista de tipos de autorización ordenados por nombre
     */
    @GetMapping("/ordered-by-name")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<AuthorizationType>> getMyCorporationAuthorizationTypesOrderByName() {
        try {
            List<AuthorizationType> authorizationTypes = authorizationTypeService.getMyCorporationAuthorizationTypesOrderByName();
            return ResponseEntity.ok(authorizationTypes);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}

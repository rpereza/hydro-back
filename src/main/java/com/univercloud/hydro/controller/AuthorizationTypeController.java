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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AuthorizationType>> getAllMyCorporationAuthorizationTypes() {
        try {
            List<AuthorizationType> authorizationTypes = authorizationTypeService.getAllMyCorporationAuthorizationTypes();
            return ResponseEntity.ok(authorizationTypes);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
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
}

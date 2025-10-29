package com.univercloud.hydro.controller;

import com.univercloud.hydro.dto.*;
import com.univercloud.hydro.service.CorporationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de Corporaciones.
 */
@RestController
@RequestMapping("/api/corporations")
@CrossOrigin(origins = "*")
public class CorporationController {
    
    @Autowired
    private CorporationService corporationService;
    
    /**
     * Crea una nueva corporación para el usuario autenticado.
     * 
     * @param request los datos de la corporación a crear
     * @return la corporación creada
     */
    @PostMapping
    public ResponseEntity<CorporationResponse> createCorporation(@Valid @RequestBody CreateCorporationRequest request) {
        try {
            CorporationResponse response = corporationService.createCorporation(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    /**
     * Obtiene la corporación del usuario autenticado.
     * 
     * @return la corporación del usuario
     */
    @GetMapping("/my-corporation")
    public ResponseEntity<CorporationResponse> getMyCorporation() {
        Optional<CorporationResponse> corporation = corporationService.getMyCorporation();
        return corporation.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtiene una corporación por su ID (solo para administradores).
     * 
     * @param id el ID de la corporación
     * @return la corporación
     */
    @GetMapping("/{id}")
    public ResponseEntity<CorporationResponse> getCorporationById(@PathVariable Long id) {
        try {
            Optional<CorporationResponse> corporation = corporationService.getCorporationById(id);
            return corporation.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene una corporación por su código.
     * 
     * @param code el código de la corporación
     * @return la corporación
     */
    @GetMapping("/by-code/{code}")
    public ResponseEntity<CorporationResponse> getCorporationByCode(@PathVariable String code) {
        Optional<CorporationResponse> corporation = corporationService.getCorporationByCode(code);
        return corporation.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtiene todas las corporaciones (solo para administradores).
     * 
     * @param pageable parámetros de paginación
     * @return página de corporaciones
     */
    @GetMapping
    public ResponseEntity<Page<CorporationResponse>> getAllCorporations(Pageable pageable) {
        try {
            Page<CorporationResponse> corporations = corporationService.getAllCorporations(pageable);
            return ResponseEntity.ok(corporations);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene todas las corporaciones activas.
     * 
     * @return lista de corporaciones activas
     */
    @GetMapping("/active")
    public ResponseEntity<List<CorporationResponse>> getActiveCorporations() {
        List<CorporationResponse> corporations = corporationService.getActiveCorporations();
        return ResponseEntity.ok(corporations);
    }
    
    /**
     * Actualiza la información de la corporación del usuario autenticado.
     * 
     * @param request los nuevos datos de la corporación
     * @return la corporación actualizada
     */
    @PutMapping("/my-corporation")
    public ResponseEntity<CorporationResponse> updateMyCorporation(@Valid @RequestBody CreateCorporationRequest request) {
        try {
            CorporationResponse response = corporationService.updateMyCorporation(request);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    /**
     * Desactiva la corporación del usuario autenticado.
     * 
     * @return true si se desactivó correctamente
     */
    @PutMapping("/my-corporation/deactivate")
    public ResponseEntity<Boolean> deactivateMyCorporation() {
        try {
            boolean result = corporationService.deactivateMyCorporation();
            return ResponseEntity.ok(result);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * Activa la corporación del usuario autenticado.
     * 
     * @return true si se activó correctamente
     */
    @PutMapping("/my-corporation/activate")
    public ResponseEntity<Boolean> activateMyCorporation() {
        try {
            boolean result = corporationService.activateMyCorporation();
            return ResponseEntity.ok(result);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * Invita a un usuario a la corporación del usuario autenticado.
     * 
     * @param request los datos del usuario a invitar
     * @return el usuario creado e invitado
     */
    @PostMapping("/users/invite")
    public ResponseEntity<UserCorporationResponse> inviteUser(@Valid @RequestBody InviteUserRequest request) {
        try {
            UserCorporationResponse response = corporationService.inviteUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    /**
     * Obtiene todos los usuarios de la corporación del usuario autenticado.
     * 
     * @return lista de usuarios de la corporación
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserCorporationResponse>> getUsersInMyCorporation() {
        try {
            List<UserCorporationResponse> users = corporationService.getUsersInMyCorporation();
            return ResponseEntity.ok(users);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * Obtiene todos los usuarios de una corporación específica (solo para administradores).
     * 
     * @param corporationId el ID de la corporación
     * @return lista de usuarios de la corporación
     */
    @GetMapping("/{corporationId}/users")
    public ResponseEntity<List<UserCorporationResponse>> getUsersInCorporation(@PathVariable Long corporationId) {
        try {
            List<UserCorporationResponse> users = corporationService.getUsersInCorporation(corporationId);
            return ResponseEntity.ok(users);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * Remueve un usuario de la corporación del usuario autenticado.
     * 
     * @param userId el ID del usuario a remover
     * @return true si se removió correctamente
     */
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Boolean> removeUserFromMyCorporation(@PathVariable Long userId) {
        try {
            boolean result = corporationService.removeUserFromMyCorporation(userId);
            return ResponseEntity.ok(result);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * Remueve un usuario de una corporación específica (solo para administradores).
     * 
     * @param corporationId el ID de la corporación
     * @param userId el ID del usuario a remover
     * @return true si se removió correctamente
     */
    @DeleteMapping("/{corporationId}/users/{userId}")
    public ResponseEntity<Boolean> removeUserFromCorporation(@PathVariable Long corporationId, 
                                                           @PathVariable Long userId) {
        try {
            boolean result = corporationService.removeUserFromCorporation(corporationId, userId);
            return ResponseEntity.ok(result);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * Verifica si el usuario autenticado puede crear una corporación.
     * 
     * @return true si puede crear una corporación
     */
    @GetMapping("/can-create")
    public ResponseEntity<Boolean> canCreateCorporation() {
        boolean canCreate = corporationService.canCreateCorporation();
        return ResponseEntity.ok(canCreate);
    }
    
    /**
     * Verifica si el usuario autenticado puede invitar usuarios a su corporación.
     * 
     * @return true si puede invitar usuarios
     */
    @GetMapping("/can-invite")
    public ResponseEntity<Boolean> canInviteUsers() {
        boolean canInvite = corporationService.canInviteUsers();
        return ResponseEntity.ok(canInvite);
    }
    
    /**
     * Obtiene estadísticas de la corporación del usuario autenticado.
     * 
     * @return estadísticas de la corporación
     */
    @GetMapping("/my-corporation/stats")
    public ResponseEntity<CorporationStatsResponse> getMyCorporationStats() {
        try {
            CorporationStatsResponse stats = corporationService.getMyCorporationStats();
            return ResponseEntity.ok(stats);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    /**
     * Busca corporaciones por nombre (búsqueda parcial).
     * 
     * @param name el nombre o parte del nombre a buscar
     * @return lista de corporaciones que coinciden
     */
    @GetMapping("/search")
    public ResponseEntity<List<CorporationResponse>> searchCorporationsByName(@RequestParam String name) {
        List<CorporationResponse> corporations = corporationService.searchCorporationsByName(name);
        return ResponseEntity.ok(corporations);
    }
    
    /**
     * Verifica si existe una corporación con el código especificado.
     * 
     * @param code el código a verificar
     * @return true si existe
     */
    @GetMapping("/exists/{code}")
    public ResponseEntity<Boolean> existsByCode(@PathVariable String code) {
        boolean exists = corporationService.existsByCode(code);
        return ResponseEntity.ok(exists);
    }
    
    /**
     * Verifica si el usuario autenticado es propietario de su corporación.
     * 
     * @return true si es propietario
     */
    @GetMapping("/is-owner")
    public ResponseEntity<Boolean> isOwnerOfMyCorporation() {
        boolean isOwner = corporationService.isOwnerOfMyCorporation();
        return ResponseEntity.ok(isOwner);
    }
}

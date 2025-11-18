package com.univercloud.hydro.controller;

import com.univercloud.hydro.entity.User;
import com.univercloud.hydro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de Usuarios.
 * Proporciona endpoints para operaciones CRUD y consultas de usuarios.
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * Crea un nuevo usuario.
     * 
     * @param user el usuario a crear
     * @return el usuario creado
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Actualiza un usuario existente.
     * 
     * @param id el ID del usuario
     * @param user el usuario actualizado
     * @return el usuario actualizado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        try {
            user.setId(id);
            User updatedUser = userService.updateUser(user);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Obtiene un usuario por su ID.
     * 
     * @param id el ID del usuario
     * @return el usuario si existe
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtiene todos los usuarios (solo para administradores).
     * 
     * @return lista de usuarios
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }
    
    /**
     * Obtiene todos los usuarios habilitados (solo para administradores).
     * 
     * @return lista de usuarios habilitados
     */
    @GetMapping("/enabled")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getEnabledUsers() {
        List<User> users = userService.findAllEnabledUsers();
        return ResponseEntity.ok(users);
    }
    
    /**
     * Busca un usuario por email.
     * 
     * @param email el email del usuario
     * @return el usuario si existe
     */
    @GetMapping("/by-email")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        Optional<User> user = userService.findByEmail(email);
        return user.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Busca un usuario por username.
     * 
     * @param username el username del usuario
     * @return el usuario si existe
     */
    @GetMapping("/by-username")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<User> getUserByUsername(@RequestParam String username) {
        Optional<User> user = userService.findByUsername(username);
        return user.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Elimina un usuario.
     * 
     * @param id el ID del usuario a eliminar
     * @return true si se eliminó correctamente
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(true);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Verifica si existe un usuario con el email especificado.
     * 
     * @param email el email del usuario
     * @return true si existe, false en caso contrario
     */
    @GetMapping("/exists/email")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> existsByEmail(@RequestParam String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
    
    /**
     * Verifica si existe un usuario con el username especificado.
     * 
     * @param username el username del usuario
     * @return true si existe, false en caso contrario
     */
    @GetMapping("/exists/username")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> existsByUsername(@RequestParam String username) {
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }
    
    /**
     * Habilita o deshabilita un usuario.
     * 
     * @param id el ID del usuario
     * @param enabled true para habilitar, false para deshabilitar
     * @return el usuario actualizado
     */
    @PutMapping("/{id}/enabled")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> setUserEnabled(@PathVariable Long id, @RequestParam boolean enabled) {
        try {
            User user = userService.setUserEnabled(id, enabled);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Añade un rol a un usuario.
     * 
     * @param id el ID del usuario
     * @param roleName el nombre del rol
     * @return el usuario actualizado
     */
    @PutMapping("/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> addRoleToUser(@PathVariable Long id, @RequestParam String roleName) {
        try {
            User user = userService.addRoleToUser(id, roleName);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Remueve un rol de un usuario.
     * 
     * @param id el ID del usuario
     * @param roleName el nombre del rol
     * @return el usuario actualizado
     */
    @DeleteMapping("/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> removeRoleFromUser(@PathVariable Long id, @RequestParam String roleName) {
        try {
            User user = userService.removeRoleFromUser(id, roleName);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

package com.univercloud.hydro.controller;

import com.univercloud.hydro.entity.MinimumTariff;
import com.univercloud.hydro.service.MinimumTariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de Tarifas Mínimas.
 * Proporciona endpoints para operaciones CRUD y consultas de tarifas mínimas.
 */
@RestController
@RequestMapping("/api/minimum-tariffs")
@CrossOrigin(origins = "*")
public class MinimumTariffController {
    
    @Autowired
    private MinimumTariffService minimumTariffService;
    
    /**
     * Crea una nueva tarifa mínima.
     * 
     * @param minimumTariff la tarifa mínima a crear
     * @return la tarifa mínima creada
     */
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<MinimumTariff> createMinimumTariff(@Valid @RequestBody MinimumTariff minimumTariff) {
        try {
            MinimumTariff createdMinimumTariff = minimumTariffService.createMinimumTariff(minimumTariff);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMinimumTariff);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Actualiza una tarifa mínima existente.
     * 
     * @param id el ID de la tarifa mínima
     * @param minimumTariff la tarifa mínima actualizada
     * @return la tarifa mínima actualizada
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<MinimumTariff> updateMinimumTariff(@PathVariable Long id, @Valid @RequestBody MinimumTariff minimumTariff) {
        try {
            minimumTariff.setId(id);
            MinimumTariff updatedMinimumTariff = minimumTariffService.updateMinimumTariff(minimumTariff);
            return ResponseEntity.ok(updatedMinimumTariff);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene una tarifa mínima por su ID.
     * 
     * @param id el ID de la tarifa mínima
     * @return la tarifa mínima si existe
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<MinimumTariff> getMinimumTariffById(@PathVariable Long id) {
        Optional<MinimumTariff> minimumTariff = minimumTariffService.getMinimumTariffById(id);
        return minimumTariff.map(ResponseEntity::ok)
                           .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtiene todas las tarifas mínimas de la corporación.
     * 
     * @return lista de tarifas mínimas
     */
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<MinimumTariff>> getAllMinimumTariffs() {
        try {
            List<MinimumTariff> minimumTariffs = minimumTariffService.getAllMyCorporationMinimumTariffs();
            return ResponseEntity.ok(minimumTariffs);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    
    /**
     * Elimina una tarifa mínima.
     * 
     * @param id el ID de la tarifa mínima a eliminar
     * @return true si se eliminó correctamente
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> deleteMinimumTariff(@PathVariable Long id) {
        try {
            boolean deleted = minimumTariffService.deleteMinimumTariff(id);
            return ResponseEntity.ok(deleted);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
}

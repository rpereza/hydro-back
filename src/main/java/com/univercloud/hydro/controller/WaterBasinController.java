package com.univercloud.hydro.controller;

import com.univercloud.hydro.entity.WaterBasin;
import com.univercloud.hydro.service.WaterBasinService;
import com.univercloud.hydro.service.WaterBasinService.WaterBasinStats;
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
 * Controlador REST para la gestión de Cuencas Hidrográficas.
 * Proporciona endpoints para operaciones CRUD y consultas de cuencas hidrográficas.
 */
@RestController
@RequestMapping("/api/water-basins")
@CrossOrigin(origins = "*")
public class WaterBasinController {
    
    @Autowired
    private WaterBasinService waterBasinService;
    
    /**
     * Crea una nueva cuenca hidrográfica.
     * 
     * @param waterBasin la cuenca hidrográfica a crear
     * @return la cuenca hidrográfica creada
     */
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<WaterBasin> createWaterBasin(@Valid @RequestBody WaterBasin waterBasin) {
        try {
            WaterBasin createdWaterBasin = waterBasinService.createWaterBasin(waterBasin);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdWaterBasin);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Actualiza una cuenca hidrográfica existente.
     * 
     * @param id el ID de la cuenca hidrográfica
     * @param waterBasin la cuenca hidrográfica actualizada
     * @return la cuenca hidrográfica actualizada
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<WaterBasin> updateWaterBasin(@PathVariable Long id, @Valid @RequestBody WaterBasin waterBasin) {
        try {
            waterBasin.setId(id);
            WaterBasin updatedWaterBasin = waterBasinService.updateWaterBasin(waterBasin);
            return ResponseEntity.ok(updatedWaterBasin);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene una cuenca hidrográfica por su ID.
     * 
     * @param id el ID de la cuenca hidrográfica
     * @return la cuenca hidrográfica si existe
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<WaterBasin> getWaterBasinById(@PathVariable Long id) {
        Optional<WaterBasin> waterBasin = waterBasinService.getWaterBasinById(id);
        return waterBasin.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtiene todas las cuencas hidrográficas de la corporación del usuario autenticado con paginación.
     * 
     * @param pageable parámetros de paginación
     * @return página de cuencas hidrográficas
     */
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Page<WaterBasin>> getMyCorporationWaterBasins(Pageable pageable) {
        try {
            Page<WaterBasin> waterBasins = waterBasinService.getMyCorporationWaterBasins(pageable);
            return ResponseEntity.ok(waterBasins);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene todas las cuencas hidrográficas de la corporación del usuario autenticado.
     * 
     * @return lista de cuencas hidrográficas
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<WaterBasin>> getAllMyCorporationWaterBasins() {
        try {
            List<WaterBasin> waterBasins = waterBasinService.getAllMyCorporationWaterBasins();
            return ResponseEntity.ok(waterBasins);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene todas las cuencas hidrográficas activas de la corporación del usuario autenticado.
     * 
     * @return lista de cuencas hidrográficas activas
     */
    @GetMapping("/active")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<WaterBasin>> getActiveMyCorporationWaterBasins() {
        try {
            List<WaterBasin> waterBasins = waterBasinService.getActiveMyCorporationWaterBasins();
            return ResponseEntity.ok(waterBasins);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene todas las cuencas hidrográficas activas.
     * 
     * @return lista de cuencas hidrográficas activas
     */
    @GetMapping("/all-active")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<WaterBasin>> getAllActiveWaterBasins() {
        List<WaterBasin> waterBasins = waterBasinService.getAllActiveWaterBasins();
        return ResponseEntity.ok(waterBasins);
    }
    
    /**
     * Obtiene todas las cuencas hidrográficas inactivas.
     * 
     * @return lista de cuencas hidrográficas inactivas
     */
    @GetMapping("/inactive")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<WaterBasin>> getAllInactiveWaterBasins() {
        List<WaterBasin> waterBasins = waterBasinService.getAllInactiveWaterBasins();
        return ResponseEntity.ok(waterBasins);
    }
    
    /**
     * Busca una cuenca hidrográfica por nombre.
     * 
     * @param name el nombre de la cuenca hidrográfica
     * @return la cuenca hidrográfica si existe
     */
    @GetMapping("/by-name")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<WaterBasin> getWaterBasinByName(@RequestParam String name) {
        Optional<WaterBasin> waterBasin = waterBasinService.getWaterBasinByName(name);
        return waterBasin.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Busca cuencas hidrográficas por nombre (búsqueda parcial).
     * 
     * @param name el nombre o parte del nombre a buscar
     * @return lista de cuencas hidrográficas que coinciden
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<WaterBasin>> searchWaterBasinsByName(@RequestParam String name) {
        try {
            List<WaterBasin> waterBasins = waterBasinService.searchWaterBasinsByName(name);
            return ResponseEntity.ok(waterBasins);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Busca cuencas hidrográficas activas por nombre (búsqueda parcial).
     * 
     * @param name el nombre o parte del nombre a buscar
     * @return lista de cuencas hidrográficas activas que coinciden
     */
    @GetMapping("/search-active")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<WaterBasin>> searchActiveWaterBasinsByName(@RequestParam String name) {
        try {
            List<WaterBasin> waterBasins = waterBasinService.searchActiveWaterBasinsByName(name);
            return ResponseEntity.ok(waterBasins);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene cuencas hidrográficas creadas en un rango de fechas.
     * 
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de cuencas hidrográficas creadas en el rango
     */
    @GetMapping("/by-date-range")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<WaterBasin>> getWaterBasinsByDateRange(
            @RequestParam LocalDateTime startDate, 
            @RequestParam LocalDateTime endDate) {
        try {
            List<WaterBasin> waterBasins = waterBasinService.getWaterBasinsByDateRange(startDate, endDate);
            return ResponseEntity.ok(waterBasins);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Cuenta el número de cuencas hidrográficas de la corporación del usuario autenticado.
     * 
     * @return número de cuencas hidrográficas
     */
    @GetMapping("/count")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Long> countMyCorporationWaterBasins() {
        try {
            long count = waterBasinService.countMyCorporationWaterBasins();
            return ResponseEntity.ok(count);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Cuenta cuencas hidrográficas activas de la corporación del usuario autenticado.
     * 
     * @return número de cuencas hidrográficas activas
     */
    @GetMapping("/count-active")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Long> countActiveMyCorporationWaterBasins() {
        try {
            long count = waterBasinService.countActiveMyCorporationWaterBasins();
            return ResponseEntity.ok(count);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Activa una cuenca hidrográfica.
     * 
     * @param id el ID de la cuenca hidrográfica a activar
     * @return true si se activó correctamente
     */
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> activateWaterBasin(@PathVariable Long id) {
        try {
            boolean activated = waterBasinService.activateWaterBasin(id);
            return ResponseEntity.ok(activated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Desactiva una cuenca hidrográfica.
     * 
     * @param id el ID de la cuenca hidrográfica a desactivar
     * @return true si se desactivó correctamente
     */
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> deactivateWaterBasin(@PathVariable Long id) {
        try {
            boolean deactivated = waterBasinService.deactivateWaterBasin(id);
            return ResponseEntity.ok(deactivated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Elimina una cuenca hidrográfica.
     * 
     * @param id el ID de la cuenca hidrográfica a eliminar
     * @return true si se eliminó correctamente
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> deleteWaterBasin(@PathVariable Long id) {
        try {
            boolean deleted = waterBasinService.deleteWaterBasin(id);
            return ResponseEntity.ok(deleted);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Verifica si existe una cuenca hidrográfica con el nombre especificado.
     * 
     * @param name el nombre de la cuenca hidrográfica
     * @return true si existe, false en caso contrario
     */
    @GetMapping("/exists")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> existsByName(@RequestParam String name) {
        boolean exists = waterBasinService.existsByName(name);
        return ResponseEntity.ok(exists);
    }
    
    /**
     * Obtiene cuencas hidrográficas ordenadas por fecha de creación (más recientes primero).
     * 
     * @return lista de cuencas hidrográficas ordenadas por fecha de creación descendente
     */
    @GetMapping("/recent")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<WaterBasin>> getWaterBasinsOrderByCreatedAtDesc() {
        try {
            List<WaterBasin> waterBasins = waterBasinService.getWaterBasinsOrderByCreatedAtDesc();
            return ResponseEntity.ok(waterBasins);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene cuencas hidrográficas activas ordenadas por nombre.
     * 
     * @return lista de cuencas hidrográficas activas ordenadas por nombre
     */
    @GetMapping("/active-ordered")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<WaterBasin>> getActiveWaterBasinsOrderByName() {
        List<WaterBasin> waterBasins = waterBasinService.getActiveWaterBasinsOrderByName();
        return ResponseEntity.ok(waterBasins);
    }
    
    /**
     * Obtiene cuencas hidrográficas de la corporación ordenadas por nombre.
     * 
     * @return lista de cuencas hidrográficas de la corporación ordenadas por nombre
     */
    @GetMapping("/ordered")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<WaterBasin>> getMyCorporationWaterBasinsOrderByName() {
        try {
            List<WaterBasin> waterBasins = waterBasinService.getMyCorporationWaterBasinsOrderByName();
            return ResponseEntity.ok(waterBasins);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene cuencas hidrográficas activas de la corporación ordenadas por nombre.
     * 
     * @return lista de cuencas hidrográficas activas de la corporación ordenadas por nombre
     */
    @GetMapping("/active-ordered-corporation")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<WaterBasin>> getActiveMyCorporationWaterBasinsOrderByName() {
        try {
            List<WaterBasin> waterBasins = waterBasinService.getActiveMyCorporationWaterBasinsOrderByName();
            return ResponseEntity.ok(waterBasins);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene estadísticas de cuencas hidrográficas de la corporación del usuario autenticado.
     * 
     * @return estadísticas de cuencas hidrográficas
     */
    @GetMapping("/stats")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<WaterBasinStats> getMyCorporationWaterBasinStats() {
        try {
            WaterBasinStats stats = waterBasinService.getMyCorporationWaterBasinStats();
            return ResponseEntity.ok(stats);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}

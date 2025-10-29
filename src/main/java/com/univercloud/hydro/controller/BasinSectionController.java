package com.univercloud.hydro.controller;

import com.univercloud.hydro.entity.BasinSection;
import com.univercloud.hydro.service.BasinSectionService;
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
 * Controlador REST para la gestión de Secciones de Cuenca.
 * Proporciona endpoints para operaciones CRUD y consultas de secciones de cuenca.
 */
@RestController
@RequestMapping("/api/basin-sections")
@CrossOrigin(origins = "*")
public class BasinSectionController {
    
    @Autowired
    private BasinSectionService basinSectionService;
    
    /**
     * Crea una nueva sección de cuenca.
     * 
     * @param basinSection la sección de cuenca a crear
     * @return la sección de cuenca creada
     */
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<BasinSection> createBasinSection(@Valid @RequestBody BasinSection basinSection) {
        try {
            BasinSection createdBasinSection = basinSectionService.createBasinSection(basinSection);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBasinSection);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Actualiza una sección de cuenca existente.
     * 
     * @param id el ID de la sección de cuenca
     * @param basinSection la sección de cuenca actualizada
     * @return la sección de cuenca actualizada
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<BasinSection> updateBasinSection(@PathVariable Long id, @Valid @RequestBody BasinSection basinSection) {
        try {
            basinSection.setId(id);
            BasinSection updatedBasinSection = basinSectionService.updateBasinSection(basinSection);
            return ResponseEntity.ok(updatedBasinSection);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene una sección de cuenca por su ID.
     * 
     * @param id el ID de la sección de cuenca
     * @return la sección de cuenca si existe
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<BasinSection> getBasinSectionById(@PathVariable Long id) {
        Optional<BasinSection> basinSection = basinSectionService.getBasinSectionById(id);
        return basinSection.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtiene todas las secciones de cuenca de la corporación del usuario autenticado con paginación.
     * 
     * @param pageable parámetros de paginación
     * @return página de secciones de cuenca
     */
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Page<BasinSection>> getMyCorporationBasinSections(Pageable pageable) {
        try {
            Page<BasinSection> basinSections = basinSectionService.getMyCorporationBasinSections(pageable);
            return ResponseEntity.ok(basinSections);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene todas las secciones de cuenca de la corporación del usuario autenticado.
     * 
     * @return lista de secciones de cuenca
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<BasinSection>> getAllMyCorporationBasinSections() {
        try {
            List<BasinSection> basinSections = basinSectionService.getAllMyCorporationBasinSections();
            return ResponseEntity.ok(basinSections);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene secciones de cuenca por cuenca hídrica.
     * 
     * @param waterBasinId el ID de la cuenca hídrica
     * @return lista de secciones de la cuenca
     */
    @GetMapping("/by-water-basin/{waterBasinId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<BasinSection>> getBasinSectionsByWaterBasin(@PathVariable Long waterBasinId) {
        try {
            List<BasinSection> basinSections = basinSectionService.getBasinSectionsByWaterBasin(waterBasinId);
            return ResponseEntity.ok(basinSections);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Busca una sección de cuenca por nombre.
     * 
     * @param name el nombre de la sección de cuenca
     * @return la sección de cuenca si existe
     */
    @GetMapping("/by-name")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<BasinSection> getBasinSectionByName(@RequestParam String name) {
        Optional<BasinSection> basinSection = basinSectionService.getBasinSectionByName(name);
        return basinSection.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Busca secciones de cuenca por nombre (búsqueda parcial).
     * 
     * @param name el nombre o parte del nombre a buscar
     * @return lista de secciones de cuenca que coinciden
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<BasinSection>> searchBasinSectionsByName(@RequestParam String name) {
        try {
            List<BasinSection> basinSections = basinSectionService.searchBasinSectionsByName(name);
            return ResponseEntity.ok(basinSections);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene secciones de cuenca creadas en un rango de fechas.
     * 
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de secciones de cuenca creadas en el rango
     */
    @GetMapping("/by-date-range")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<BasinSection>> getBasinSectionsByDateRange(
            @RequestParam LocalDateTime startDate, 
            @RequestParam LocalDateTime endDate) {
        try {
            List<BasinSection> basinSections = basinSectionService.getBasinSectionsByDateRange(startDate, endDate);
            return ResponseEntity.ok(basinSections);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Elimina una sección de cuenca.
     * 
     * @param id el ID de la sección de cuenca a eliminar
     * @return true si se eliminó correctamente
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> deleteBasinSection(@PathVariable Long id) {
        try {
            boolean deleted = basinSectionService.deleteBasinSection(id);
            return ResponseEntity.ok(deleted);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Verifica si existe una sección de cuenca con el nombre especificado.
     * 
     * @param name el nombre de la sección de cuenca
     * @return true si existe, false en caso contrario
     */
    @GetMapping("/exists")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> existsByName(@RequestParam String name) {
        try {
            boolean exists = basinSectionService.existsByName(name);
            return ResponseEntity.ok(exists);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene secciones de cuenca ordenadas por fecha de creación (más recientes primero).
     * 
     * @return lista de secciones de cuenca ordenadas por fecha de creación descendente
     */
    @GetMapping("/recent")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<BasinSection>> getBasinSectionsOrderByCreatedAtDesc() {
        try {
            List<BasinSection> basinSections = basinSectionService.getBasinSectionsOrderByCreatedAtDesc();
            return ResponseEntity.ok(basinSections);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene secciones de cuenca ordenadas por nombre.
     * 
     * @return lista de secciones de cuenca ordenadas por nombre
     */
    @GetMapping("/ordered-by-name")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<BasinSection>> getBasinSectionsOrderByName() {
        try {
            List<BasinSection> basinSections = basinSectionService.getBasinSectionsOrderByName();
            return ResponseEntity.ok(basinSections);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Cuenta el número de secciones de cuenca de la corporación del usuario autenticado.
     * 
     * @return número de secciones de cuenca
     */
    @GetMapping("/count")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Long> countMyCorporationBasinSections() {
        try {
            long count = basinSectionService.countMyCorporationBasinSections();
            return ResponseEntity.ok(count);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Cuenta secciones de cuenca por cuenca hídrica.
     * 
     * @param waterBasinId el ID de la cuenca hídrica
     * @return número de secciones de la cuenca
     */
    @GetMapping("/count/by-water-basin/{waterBasinId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Long> countBasinSectionsByWaterBasin(@PathVariable Long waterBasinId) {
        long count = basinSectionService.countBasinSectionsByWaterBasin(waterBasinId);
        return ResponseEntity.ok(count);
    }
}

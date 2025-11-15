package com.univercloud.hydro.controller;

import com.univercloud.hydro.entity.WaterBasin;
import com.univercloud.hydro.service.WaterBasinService;
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<WaterBasin>> getAllMyCorporationWaterBasins() {
        try {
            List<WaterBasin> waterBasins = waterBasinService.getAllMyCorporationWaterBasins();
            return ResponseEntity.ok(waterBasins);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
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
     * Elimina una cuenca hidrográfica.
     * 
     * @param id el ID de la cuenca hidrográfica a eliminar
     * @return true si se eliminó correctamente
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
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
}

package com.univercloud.hydro.controller;

import com.univercloud.hydro.entity.Discharge;
import com.univercloud.hydro.service.DischargeService;
import com.univercloud.hydro.service.DischargeService.DischargeStats;
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
 * Controlador REST para la gestión de Descargas.
 * Proporciona endpoints para operaciones CRUD y consultas de descargas de agua.
 */
@RestController
@RequestMapping("/api/discharges")
@CrossOrigin(origins = "*")
public class DischargeController {
    
    @Autowired
    private DischargeService dischargeService;
    
    /**
     * Crea una nueva descarga.
     * 
     * @param discharge la descarga a crear
     * @return la descarga creada
     */
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Discharge> createDischarge(@Valid @RequestBody Discharge discharge) {
        try {
            Discharge createdDischarge = dischargeService.createDischarge(discharge);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDischarge);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Actualiza una descarga existente.
     * 
     * @param id el ID de la descarga
     * @param discharge la descarga actualizada
     * @return la descarga actualizada
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Discharge> updateDischarge(@PathVariable Long id, @Valid @RequestBody Discharge discharge) {
        try {
            discharge.setId(id);
            Discharge updatedDischarge = dischargeService.updateDischarge(discharge);
            return ResponseEntity.ok(updatedDischarge);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene una descarga por su ID.
     * 
     * @param id el ID de la descarga
     * @return la descarga si existe
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Discharge> getDischargeById(@PathVariable Long id) {
        Optional<Discharge> discharge = dischargeService.getDischargeById(id);
        return discharge.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtiene todas las descargas de la corporación del usuario autenticado con paginación.
     * 
     * @param pageable parámetros de paginación
     * @return página de descargas
     */
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Page<Discharge>> getMyCorporationDischarges(Pageable pageable) {
        try {
            Page<Discharge> discharges = dischargeService.getMyCorporationDischarges(pageable);
            return ResponseEntity.ok(discharges);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene todas las descargas de la corporación del usuario autenticado.
     * 
     * @return lista de descargas
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Discharge>> getAllMyCorporationDischarges() {
        try {
            List<Discharge> discharges = dischargeService.getAllMyCorporationDischarges();
            return ResponseEntity.ok(discharges);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene descargas por usuario de descarga.
     * 
     * @param dischargeUserId el ID del usuario de descarga
     * @return lista de descargas del usuario
     */
    @GetMapping("/by-discharge-user/{dischargeUserId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Discharge>> getDischargesByDischargeUser(@PathVariable Long dischargeUserId) {
        try {
            List<Discharge> discharges = dischargeService.getDischargesByDischargeUser(dischargeUserId);
            return ResponseEntity.ok(discharges);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene descargas por municipio.
     * 
     * @param municipalityId el ID del municipio
     * @return lista de descargas del municipio
     */
    @GetMapping("/by-municipality/{municipalityId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Discharge>> getDischargesByMunicipality(@PathVariable Long municipalityId) {
        try {
            List<Discharge> discharges = dischargeService.getDischargesByMunicipality(municipalityId);
            return ResponseEntity.ok(discharges);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Obtiene descargas por sección de cuenca.
     * 
     * @param basinSectionId el ID de la sección de cuenca
     * @return lista de descargas de la sección
     */
    @GetMapping("/by-basin-section/{basinSectionId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Discharge>> getDischargesByBasinSection(@PathVariable Long basinSectionId) {
        try {
            List<Discharge> discharges = dischargeService.getDischargesByBasinSection(basinSectionId);
            return ResponseEntity.ok(discharges);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene descargas por año.
     * 
     * @param year el año
     * @return lista de descargas del año
     */
    @GetMapping("/by-year/{year}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Discharge>> getDischargesByYear(@PathVariable Integer year) {
        try {
            List<Discharge> discharges = dischargeService.getDischargesByYear(year);
            return ResponseEntity.ok(discharges);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene descargas por tipo de descarga.
     * 
     * @param dischargeType el tipo de descarga
     * @return lista de descargas del tipo especificado
     */
    @GetMapping("/by-type/{dischargeType}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Discharge>> getDischargesByType(@PathVariable Discharge.DischargeType dischargeType) {
        try {
            List<Discharge> discharges = dischargeService.getDischargesByType(dischargeType);
            return ResponseEntity.ok(discharges);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene descargas por tipo de recurso hídrico.
     * 
     * @param waterResourceType el tipo de recurso hídrico
     * @return lista de descargas del tipo de recurso
     */
    @GetMapping("/by-water-resource-type/{waterResourceType}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Discharge>> getDischargesByWaterResourceType(@PathVariable Discharge.WaterResourceType waterResourceType) {
        try {
            List<Discharge> discharges = dischargeService.getDischargesByWaterResourceType(waterResourceType);
            return ResponseEntity.ok(discharges);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Busca una descarga por número y año.
     * 
     * @param number el número de descarga
     * @param year el año
     * @return la descarga si existe
     */
    @GetMapping("/by-number-year")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Discharge> getDischargeByNumberAndYear(@RequestParam Integer number, @RequestParam Integer year) {
        Optional<Discharge> discharge = dischargeService.getDischargeByNumberAndYear(number, year);
        return discharge.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Busca descargas por nombre (búsqueda parcial).
     * 
     * @param name el nombre o parte del nombre a buscar
     * @return lista de descargas que coinciden
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Discharge>> searchDischargesByName(@RequestParam String name) {
        try {
            List<Discharge> discharges = dischargeService.searchDischargesByName(name);
            return ResponseEntity.ok(discharges);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene descargas que están siendo monitoreadas.
     * 
     * @return lista de descargas monitoreadas
     */
    @GetMapping("/monitored")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Discharge>> getMonitoredDischarges() {
        try {
            List<Discharge> discharges = dischargeService.getMonitoredDischarges();
            return ResponseEntity.ok(discharges);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene descargas que no están siendo monitoreadas.
     * 
     * @return lista de descargas no monitoreadas
     */
    @GetMapping("/unmonitored")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Discharge>> getUnmonitoredDischarges() {
        try {
            List<Discharge> discharges = dischargeService.getUnmonitoredDischarges();
            return ResponseEntity.ok(discharges);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene descargas con reúso de cuenca.
     * 
     * @return lista de descargas con reúso de cuenca
     */
    @GetMapping("/with-basin-reuse")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Discharge>> getDischargesWithBasinReuse() {
        try {
            List<Discharge> discharges = dischargeService.getDischargesWithBasinReuse();
            return ResponseEntity.ok(discharges);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene descargas sin reúso de cuenca.
     * 
     * @return lista de descargas sin reúso de cuenca
     */
    @GetMapping("/without-basin-reuse")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Discharge>> getDischargesWithoutBasinReuse() {
        try {
            List<Discharge> discharges = dischargeService.getDischargesWithoutBasinReuse();
            return ResponseEntity.ok(discharges);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene descargas creadas en un rango de fechas.
     * 
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de descargas creadas en el rango
     */
    @GetMapping("/by-date-range")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Discharge>> getDischargesByDateRange(
            @RequestParam LocalDateTime startDate, 
            @RequestParam LocalDateTime endDate) {
        try {
            List<Discharge> discharges = dischargeService.getDischargesByDateRange(startDate, endDate);
            return ResponseEntity.ok(discharges);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Cuenta el número de descargas de la corporación del usuario autenticado.
     * 
     * @return número de descargas
     */
    @GetMapping("/count")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Long> countMyCorporationDischarges() {
        try {
            long count = dischargeService.countMyCorporationDischarges();
            return ResponseEntity.ok(count);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Cuenta descargas por municipio.
     * 
     * @param municipalityId el ID del municipio
     * @return número de descargas del municipio
     */
    @GetMapping("/count/by-municipality/{municipalityId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Long> countDischargesByMunicipality(@PathVariable Long municipalityId) {
        long count = dischargeService.countDischargesByMunicipality(municipalityId);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Cuenta descargas por sección de cuenca.
     * 
     * @param basinSectionId el ID de la sección de cuenca
     * @return número de descargas de la sección
     */
    @GetMapping("/count/by-basin-section/{basinSectionId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Long> countDischargesByBasinSection(@PathVariable Long basinSectionId) {
        long count = dischargeService.countDischargesByBasinSection(basinSectionId);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Cuenta descargas por año.
     * 
     * @param year el año
     * @return número de descargas del año
     */
    @GetMapping("/count/by-year/{year}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Long> countDischargesByYear(@PathVariable Integer year) {
        try {
            long count = dischargeService.countDischargesByYear(year);
            return ResponseEntity.ok(count);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Elimina una descarga.
     * 
     * @param id el ID de la descarga a eliminar
     * @return true si se eliminó correctamente
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> deleteDischarge(@PathVariable Long id) {
        try {
            boolean deleted = dischargeService.deleteDischarge(id);
            return ResponseEntity.ok(deleted);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Verifica si existe una descarga con el número y año especificados.
     * 
     * @param number el número de descarga
     * @param year el año
     * @return true si existe, false en caso contrario
     */
    @GetMapping("/exists")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> existsByNumberAndYear(@RequestParam Integer number, @RequestParam Integer year) {
        boolean exists = dischargeService.existsByNumberAndYear(number, year);
        return ResponseEntity.ok(exists);
    }
    
    /**
     * Obtiene descargas ordenadas por fecha de creación (más recientes primero).
     * 
     * @return lista de descargas ordenadas por fecha de creación descendente
     */
    @GetMapping("/recent")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Discharge>> getDischargesOrderByCreatedAtDesc() {
        try {
            List<Discharge> discharges = dischargeService.getDischargesOrderByCreatedAtDesc();
            return ResponseEntity.ok(discharges);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene descargas por municipio y año.
     * 
     * @param municipalityId el ID del municipio
     * @param year el año
     * @return lista de descargas del municipio y año
     */
    @GetMapping("/by-municipality-year")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Discharge>> getDischargesByMunicipalityAndYear(
            @RequestParam Long municipalityId, 
            @RequestParam Integer year) {
        try {
            List<Discharge> discharges = dischargeService.getDischargesByMunicipalityAndYear(municipalityId, year);
            return ResponseEntity.ok(discharges);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Obtiene descargas por sección de cuenca y año.
     * 
     * @param basinSectionId el ID de la sección de cuenca
     * @param year el año
     * @return lista de descargas de la sección y año
     */
    @GetMapping("/by-basin-section-year")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Discharge>> getDischargesByBasinSectionAndYear(
            @RequestParam Long basinSectionId, 
            @RequestParam Integer year) {
        try {
            List<Discharge> discharges = dischargeService.getDischargesByBasinSectionAndYear(basinSectionId, year);
            return ResponseEntity.ok(discharges);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene estadísticas de descargas de la corporación del usuario autenticado.
     * 
     * @return estadísticas de descargas
     */
    @GetMapping("/stats")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<DischargeStats> getMyCorporationDischargeStats() {
        try {
            DischargeStats stats = dischargeService.getMyCorporationDischargeStats();
            return ResponseEntity.ok(stats);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}

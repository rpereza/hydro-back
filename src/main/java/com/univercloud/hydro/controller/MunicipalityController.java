package com.univercloud.hydro.controller;

import com.univercloud.hydro.entity.Municipality;
import com.univercloud.hydro.service.MunicipalityService;
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
 * Controlador REST para la gestión de Municipios.
 * Proporciona endpoints para operaciones CRUD y consultas de municipios.
 */
@RestController
@RequestMapping("/api/municipalities")
@CrossOrigin(origins = "*")
public class MunicipalityController {
    
    @Autowired
    private MunicipalityService municipalityService;
    
    /**
     * Crea un nuevo municipio.
     * 
     * @param municipality el municipio a crear
     * @return el municipio creado
     */
    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Municipality> createMunicipality(@Valid @RequestBody Municipality municipality) {
        try {
            Municipality createdMunicipality = municipalityService.createMunicipality(municipality);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMunicipality);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Actualiza un municipio existente.
     * 
     * @param id el ID del municipio
     * @param municipality el municipio actualizado
     * @return el municipio actualizado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Municipality> updateMunicipality(@PathVariable Long id, @Valid @RequestBody Municipality municipality) {
        try {
            municipality.setId(id);
            Municipality updatedMunicipality = municipalityService.updateMunicipality(municipality);
            return ResponseEntity.ok(updatedMunicipality);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene un municipio por su ID.
     * 
     * @param id el ID del municipio
     * @return el municipio si existe
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Municipality> getMunicipalityById(@PathVariable Long id) {
        Optional<Municipality> municipality = municipalityService.getMunicipalityById(id);
        return municipality.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtiene todos los municipios de la corporación del usuario autenticado con paginación.
     * 
     * @param pageable parámetros de paginación
     * @return página de municipios
     */
    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Page<Municipality>> getAllMunicipalities(Pageable pageable) {
        try {
            Page<Municipality> municipalities = municipalityService.getAllMunicipalities(pageable);
            return ResponseEntity.ok(municipalities);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene todos los municipios de la corporación del usuario autenticado.
     * 
     * @return lista de municipios
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<Municipality>> getAllMunicipalities() {
        try {
            List<Municipality> municipalities = municipalityService.getAllMunicipalities();
            return ResponseEntity.ok(municipalities);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Busca municipios por nombre (búsqueda parcial).
     * 
     * @param name el nombre o parte del nombre a buscar
     * @return lista de municipios que coinciden
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<Municipality>> searchMunicipalitiesByName(@RequestParam String name) {
        try {
            List<Municipality> municipalities = municipalityService.searchMunicipalitiesByName(name);
            return ResponseEntity.ok(municipalities);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene todos los municipios de un departamento.
     * 
     * @param departmentId el ID del departamento
     * @return lista de municipios del departamento
     */
    @GetMapping("/by-department/{departmentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Municipality>> getMunicipalitiesByDepartmentId(@PathVariable Long departmentId) {
        List<Municipality> municipalities = municipalityService.getMunicipalitiesByDepartmentId(departmentId);
        return ResponseEntity.ok(municipalities);
    }
    
}

package com.univercloud.hydro.controller;

import com.univercloud.hydro.entity.ProjectProgress;
import com.univercloud.hydro.service.ProjectProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Optional;

/**
 * Controlador REST para la gestión de Progreso de Proyectos.
 * Proporciona endpoints para operaciones CRUD y consultas de progreso de proyectos.
 */
@RestController
@RequestMapping("/api/project-progress")
@CrossOrigin(origins = "*")
public class ProjectProgressController {
    
    @Autowired
    private ProjectProgressService projectProgressService;
    
    /**
     * Crea un nuevo progreso de proyecto.
     * 
     * @param projectProgress el progreso de proyecto a crear
     * @return el progreso de proyecto creado
     */
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ProjectProgress> createProjectProgress(@Valid @RequestBody ProjectProgress projectProgress) {
        try {
            ProjectProgress createdProjectProgress = projectProgressService.createProjectProgress(projectProgress);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProjectProgress);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Actualiza un progreso de proyecto existente.
     * 
     * @param id el ID del progreso de proyecto
     * @param projectProgress el progreso de proyecto actualizado
     * @return el progreso de proyecto actualizado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ProjectProgress> updateProjectProgress(@PathVariable Long id, @Valid @RequestBody ProjectProgress projectProgress) {
        try {
            projectProgress.setId(id);
            ProjectProgress updatedProjectProgress = projectProgressService.updateProjectProgress(projectProgress);
            return ResponseEntity.ok(updatedProjectProgress);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene un progreso de proyecto por su ID.
     * 
     * @param id el ID del progreso de proyecto
     * @return el progreso de proyecto si existe
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ProjectProgress> getProjectProgressById(@PathVariable Long id) {
        Optional<ProjectProgress> projectProgress = projectProgressService.getProjectProgressById(id);
        return projectProgress.map(ResponseEntity::ok)
                             .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtiene todos los progresos de proyecto de la corporación con paginación.
     * 
     * @param pageable parámetros de paginación
     * @return página de progresos de proyecto
     */
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<ProjectProgress>> getAllProjectProgress(Pageable pageable) {
        try {
            Page<ProjectProgress> projectProgress = projectProgressService.getMyCorporationProjectProgresses(pageable);
            return ResponseEntity.ok(projectProgress);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Elimina un progreso de proyecto.
     * 
     * @param id el ID del progreso de proyecto a eliminar
     * @return true si se eliminó correctamente
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Boolean> deleteProjectProgress(@PathVariable Long id) {
        try {
            boolean deleted = projectProgressService.deleteProjectProgress(id);
            return ResponseEntity.ok(deleted);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
}

package com.univercloud.hydro.controller;

import com.univercloud.hydro.entity.Department;
import com.univercloud.hydro.service.DepartmentService;
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
 * Controlador REST para la gestión de Departamentos.
 * Proporciona endpoints para operaciones CRUD y consultas de departamentos.
 */
@RestController
@RequestMapping("/api/departments")
@CrossOrigin(origins = "*")
public class DepartmentController {
    
    @Autowired
    private DepartmentService departmentService;
    
    /**
     * Crea un nuevo departamento.
     * 
     * @param department el departamento a crear
     * @return el departamento creado
     */
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Department> createDepartment(@Valid @RequestBody Department department) {
        try {
            Department createdDepartment = departmentService.createDepartment(department);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Actualiza un departamento existente.
     * 
     * @param id el ID del departamento
     * @param department el departamento actualizado
     * @return el departamento actualizado
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id, @Valid @RequestBody Department department) {
        try {
            department.setId(id);
            Department updatedDepartment = departmentService.updateDepartment(department);
            return ResponseEntity.ok(updatedDepartment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene un departamento por su ID.
     * 
     * @param id el ID del departamento
     * @return el departamento si existe
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        Optional<Department> department = departmentService.getDepartmentById(id);
        return department.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtiene todos los departamentos de la corporación del usuario autenticado con paginación.
     * 
     * @param pageable parámetros de paginación
     * @return página de departamentos
     */
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Page<Department>> getAllDepartments(Pageable pageable) {
        try {
            Page<Department> departments = departmentService.getAllDepartments(pageable);
            return ResponseEntity.ok(departments);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene todos los departamentos de la corporación del usuario autenticado.
     * 
     * @return lista de departamentos
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Department>> getAllDepartments() {
        try {
            List<Department> departments = departmentService.getAllDepartments();
            return ResponseEntity.ok(departments);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Busca un departamento por nombre.
     * 
     * @param name el nombre del departamento
     * @return el departamento si existe
     */
    @GetMapping("/by-name")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Department> getDepartmentByName(@RequestParam String name) {
        Optional<Department> department = departmentService.getDepartmentByName(name);
        return department.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Busca departamentos por nombre (búsqueda parcial).
     * 
     * @param name el nombre o parte del nombre a buscar
     * @return lista de departamentos que coinciden
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Department>> searchDepartmentsByName(@RequestParam String name) {
        try {
            List<Department> departments = departmentService.searchDepartmentsByName(name);
            return ResponseEntity.ok(departments);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene departamentos creados en un rango de fechas.
     * 
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de departamentos creados en el rango
     */
    @GetMapping("/by-date-range")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Department>> getDepartmentsByDateRange(
            @RequestParam LocalDateTime startDate, 
            @RequestParam LocalDateTime endDate) {
        try {
            List<Department> departments = departmentService.getDepartmentsByDateRange(startDate, endDate);
            return ResponseEntity.ok(departments);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Elimina un departamento.
     * 
     * @param id el ID del departamento a eliminar
     * @return true si se eliminó correctamente
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> deleteDepartment(@PathVariable Long id) {
        try {
            boolean deleted = departmentService.deleteDepartment(id);
            return ResponseEntity.ok(deleted);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Verifica si existe un departamento con el nombre especificado.
     * 
     * @param name el nombre del departamento
     * @return true si existe, false en caso contrario
     */
    @GetMapping("/exists")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> existsByName(@RequestParam String name) {
        try {
            boolean exists = departmentService.existsByName(name);
            return ResponseEntity.ok(exists);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene departamentos ordenados por fecha de creación (más recientes primero).
     * 
     * @return lista de departamentos ordenados por fecha de creación descendente
     */
    @GetMapping("/recent")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Department>> getDepartmentsOrderByCreatedAtDesc() {
        try {
            List<Department> departments = departmentService.getDepartmentsOrderByCreatedAtDesc();
            return ResponseEntity.ok(departments);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene departamentos ordenados por nombre.
     * 
     * @return lista de departamentos ordenados por nombre
     */
    @GetMapping("/ordered-by-name")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Department>> getDepartmentsOrderByName() {
        try {
            List<Department> departments = departmentService.getDepartmentsOrderByName();
            return ResponseEntity.ok(departments);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
}

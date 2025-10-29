package com.univercloud.hydro.controller;

import com.univercloud.hydro.entity.Category;
import com.univercloud.hydro.service.CategoryService;
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
 * Controlador REST para la gestión de Categorías.
 * Proporciona endpoints para operaciones CRUD y consultas de categorías.
 */
@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    /**
     * Crea una nueva categoría.
     * 
     * @param category la categoría a crear
     * @return la categoría creada
     */
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
        try {
            Category createdCategory = categoryService.createCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Actualiza una categoría existente.
     * 
     * @param id el ID de la categoría
     * @param category la categoría actualizada
     * @return la categoría actualizada
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @Valid @RequestBody Category category) {
        try {
            category.setId(id);
            Category updatedCategory = categoryService.updateCategory(category);
            return ResponseEntity.ok(updatedCategory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene una categoría por su ID.
     * 
     * @param id el ID de la categoría
     * @return la categoría si existe
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Optional<Category> category = categoryService.getCategoryById(id);
        return category.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtiene todas las categorías de la corporación del usuario autenticado con paginación.
     * 
     * @param pageable parámetros de paginación
     * @return página de categorías
     */
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Page<Category>> getAllCategories(Pageable pageable) {
        try {
            Page<Category> categories = categoryService.getAllCategories(pageable);
            return ResponseEntity.ok(categories);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene todas las categorías de la corporación del usuario autenticado.
     * 
     * @return lista de categorías
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Category>> getAllCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(categories);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Busca una categoría por nombre.
     * 
     * @param name el nombre de la categoría
     * @return la categoría si existe
     */
    @GetMapping("/by-name")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Category> getCategoryByName(@RequestParam String name) {
        Optional<Category> category = categoryService.getCategoryByName(name);
        return category.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Busca categorías por nombre (búsqueda parcial).
     * 
     * @param name el nombre o parte del nombre a buscar
     * @return lista de categorías que coinciden
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Category>> searchCategoriesByName(@RequestParam String name) {
        try {
            List<Category> categories = categoryService.searchCategoriesByName(name);
            return ResponseEntity.ok(categories);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene categorías creadas en un rango de fechas.
     * 
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de categorías creadas en el rango
     */
    @GetMapping("/by-date-range")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Category>> getCategoriesByDateRange(
            @RequestParam LocalDateTime startDate, 
            @RequestParam LocalDateTime endDate) {
        try {
            List<Category> categories = categoryService.getCategoriesByDateRange(startDate, endDate);
            return ResponseEntity.ok(categories);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Elimina una categoría.
     * 
     * @param id el ID de la categoría a eliminar
     * @return true si se eliminó correctamente
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> deleteCategory(@PathVariable Long id) {
        try {
            boolean deleted = categoryService.deleteCategory(id);
            return ResponseEntity.ok(deleted);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Verifica si existe una categoría con el nombre especificado.
     * 
     * @param name el nombre de la categoría
     * @return true si existe, false en caso contrario
     */
    @GetMapping("/exists")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> existsByName(@RequestParam String name) {
        try {
            boolean exists = categoryService.existsByName(name);
            return ResponseEntity.ok(exists);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene categorías ordenadas por fecha de creación (más recientes primero).
     * 
     * @return lista de categorías ordenadas por fecha de creación descendente
     */
    @GetMapping("/recent")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Category>> getCategoriesOrderByCreatedAtDesc() {
        try {
            List<Category> categories = categoryService.getCategoriesOrderByCreatedAtDesc();
            return ResponseEntity.ok(categories);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Obtiene categorías ordenadas por nombre.
     * 
     * @return lista de categorías ordenadas por nombre
     */
    @GetMapping("/ordered-by-name")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Category>> getCategoriesOrderByName() {
        try {
            List<Category> categories = categoryService.getCategoriesOrderByName();
            return ResponseEntity.ok(categories);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
}

package com.univercloud.hydro.service;

import com.univercloud.hydro.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de Categorías.
 * Proporciona operaciones CRUD y lógica de negocio para categorías.
 */
public interface CategoryService {
    
    /**
     * Crea una nueva categoría.
     * @param category la categoría a crear
     * @return la categoría creada
     * @throws IllegalArgumentException si los datos son inválidos
     */
    Category createCategory(Category category);
    
    /**
     * Actualiza una categoría existente.
     * @param category la categoría a actualizar
     * @return la categoría actualizada
     * @throws IllegalArgumentException si la categoría no existe
     */
    Category updateCategory(Category category);
    
    /**
     * Obtiene una categoría por su ID.
     * @param id el ID de la categoría
     * @return la categoría, si existe
     */
    Optional<Category> getCategoryById(Long id);
    
    /**
     * Obtiene todas las categorías.
     * @param pageable parámetros de paginación
     * @return página de categorías
     */
    Page<Category> getAllCategories(Pageable pageable);
    
    /**
     * Obtiene todas las categorías.
     * @return lista de categorías
     */
    List<Category> getAllCategories();
    
    /**
     * Busca una categoría por nombre.
     * @param name el nombre de la categoría
     * @return la categoría si existe
     */
    Optional<Category> getCategoryByName(String name);
    
    /**
     * Busca categorías por nombre (búsqueda parcial).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de categorías que coinciden
     */
    List<Category> searchCategoriesByName(String name);
    
    /**
     * Obtiene categorías creadas en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de categorías creadas en el rango
     */
    List<Category> getCategoriesByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Elimina una categoría.
     * @param id el ID de la categoría a eliminar
     * @return true si se eliminó correctamente
     * @throws IllegalArgumentException si la categoría no existe
     */
    boolean deleteCategory(Long id);
    
    /**
     * Verifica si existe una categoría con el nombre especificado.
     * @param name el nombre de la categoría
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);
    
    /**
     * Obtiene categorías ordenadas por nombre.
     * @return lista de categorías ordenadas por nombre
     */
    List<Category> getCategoriesOrderByName();
    
    /**
     * Obtiene categorías ordenadas por fecha de creación (más recientes primero).
     * @return lista de categorías ordenadas por fecha de creación descendente
     */
    List<Category> getCategoriesOrderByCreatedAtDesc();
    
    /**
     * Obtiene estadísticas de categorías.
     * @return estadísticas de categorías
     */
    CategoryStats getCategoryStats();
    
    /**
     * Clase interna para estadísticas de categorías
     */
    class CategoryStats {
        private long totalCategories;
        private long totalMunicipalities;
        private double averageMunicipalitiesPerCategory;
        private long categoriesWithMunicipalities;
        private long categoriesWithoutMunicipalities;
        
        // Constructors
        public CategoryStats() {}
        
        // Getters and Setters
        public long getTotalCategories() { return totalCategories; }
        public void setTotalCategories(long totalCategories) { this.totalCategories = totalCategories; }
        
        public long getTotalMunicipalities() { return totalMunicipalities; }
        public void setTotalMunicipalities(long totalMunicipalities) { this.totalMunicipalities = totalMunicipalities; }
        
        public double getAverageMunicipalitiesPerCategory() { return averageMunicipalitiesPerCategory; }
        public void setAverageMunicipalitiesPerCategory(double averageMunicipalitiesPerCategory) { this.averageMunicipalitiesPerCategory = averageMunicipalitiesPerCategory; }
        
        public long getCategoriesWithMunicipalities() { return categoriesWithMunicipalities; }
        public void setCategoriesWithMunicipalities(long categoriesWithMunicipalities) { this.categoriesWithMunicipalities = categoriesWithMunicipalities; }
        
        public long getCategoriesWithoutMunicipalities() { return categoriesWithoutMunicipalities; }
        public void setCategoriesWithoutMunicipalities(long categoriesWithoutMunicipalities) { this.categoriesWithoutMunicipalities = categoriesWithoutMunicipalities; }
    }
}

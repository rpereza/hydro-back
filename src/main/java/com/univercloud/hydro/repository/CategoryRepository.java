package com.univercloud.hydro.repository;

import com.univercloud.hydro.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository para la entidad Category.
 * Proporciona métodos de consulta para gestionar categorías.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    /**
     * Busca una categoría por nombre.
     * @param name el nombre de la categoría
     * @return la categoría si existe
     */
    Optional<Category> findByName(String name);
    
    /**
     * Verifica si existe una categoría con el nombre especificado.
     * @param name el nombre de la categoría
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);
    
    /**
     * Busca categorías por nombre (búsqueda parcial, case-insensitive).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de categorías que coinciden con el nombre
     */
    @Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Category> findByNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * Busca categorías creadas en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de categorías creadas en el rango
     */
    List<Category> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Busca categorías ordenadas por nombre.
     * @return lista de categorías ordenadas por nombre
     */
    @Query("SELECT c FROM Category c ORDER BY c.name ASC")
    List<Category> findAllOrderByName();
    
    /**
     * Busca categorías ordenadas por fecha de creación (más recientes primero).
     * @return lista de categorías ordenadas por fecha de creación descendente
     */
    @Query("SELECT c FROM Category c ORDER BY c.createdAt DESC")
    List<Category> findAllOrderByCreatedAtDesc();
}

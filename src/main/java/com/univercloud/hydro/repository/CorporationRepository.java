package com.univercloud.hydro.repository;

import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para la entidad Corporation.
 * Proporciona métodos de consulta para gestionar corporaciones.
 */
@Repository
public interface CorporationRepository extends JpaRepository<Corporation, Long> {
    
    /**
     * Busca una corporación por su propietario.
     * @param owner el usuario propietario
     * @return la corporación del propietario, si existe
     */
    Optional<Corporation> findByOwner(User owner);
    
    /**
     * Verifica si existe una corporación para el propietario especificado.
     * @param owner el usuario propietario
     * @return true si existe una corporación para el propietario, false en caso contrario
     */
    boolean existsByOwner(User owner);
    
    /**
     * Verifica si existe una corporación con el código especificado.
     * @param code el código de la corporación
     * @return true si existe una corporación con el código, false en caso contrario
     */
    boolean existsByCode(String code);
    
    /**
     * Busca corporaciones por nombre (búsqueda parcial, case-insensitive).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de corporaciones que coinciden con el nombre
     */
    @Query("SELECT c FROM Corporation c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Corporation> findByNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * Cuenta el número de usuarios en una corporación específica.
     * @param corporationId el ID de la corporación
     * @return el número de usuarios en la corporación
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.corporation.id = :corporationId")
    long countUsersByCorporationId(@Param("corporationId") Long corporationId);
    
    /**
     * Busca corporaciones que contengan un usuario específico.
     * @param user el usuario a buscar
     * @return la corporación que contiene al usuario, si existe
     */
    @Query("SELECT c FROM Corporation c JOIN c.users u WHERE u = :user")
    Optional<Corporation> findByUser(@Param("user") User user);
    
    /**
     * Busca todas las corporaciones ordenadas por fecha de creación (más recientes primero).
     * @return lista de corporaciones ordenadas por fecha de creación descendente
     */
    @Query("SELECT c FROM Corporation c ORDER BY c.createdAt DESC")
    List<Corporation> findAllOrderByCreatedAtDesc();
    
    /**
     * Busca una corporación por ID cargando las relaciones owner y users.
     * @param id el ID de la corporación
     * @return la corporación con sus relaciones cargadas, si existe
     */
    @Query("SELECT DISTINCT c FROM Corporation c LEFT JOIN FETCH c.owner LEFT JOIN FETCH c.users WHERE c.id = :id")
    Optional<Corporation> findByIdWithRelations(@Param("id") Long id);
    
    /**
     * Busca una corporación por usuario cargando las relaciones owner y users.
     * @param user el usuario
     * @return la corporación con sus relaciones cargadas, si existe
     */
    @Query("SELECT DISTINCT c FROM Corporation c LEFT JOIN FETCH c.owner LEFT JOIN FETCH c.users WHERE :user MEMBER OF c.users")
    Optional<Corporation> findByUserWithRelations(@Param("user") User user);
    
    /**
     * Busca todas las corporaciones con paginación, cargando la relación owner.
     * @param pageable parámetros de paginación
     * @return página de corporaciones con owner cargado
     */
    @EntityGraph(attributePaths = {"owner"})
    Page<Corporation> findAll(Pageable pageable);
}

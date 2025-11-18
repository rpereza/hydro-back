package com.univercloud.hydro.repository;

import com.univercloud.hydro.entity.AuthorizationType;
import com.univercloud.hydro.entity.Corporation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para la entidad AuthorizationType.
 * Proporciona métodos de consulta para gestionar tipos de autorización.
 */
@Repository
public interface AuthorizationTypeRepository extends JpaRepository<AuthorizationType, Long> {
    
    /**
     * Verifica si existe un tipo de autorización con el nombre especificado.
     * @param name el nombre del tipo de autorización
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);
    
    /**
     * Busca tipos de autorización por corporación.
     * @param corporation la corporación
     * @return lista de tipos de autorización de la corporación
     */
    List<AuthorizationType> findByCorporation(Corporation corporation);
    
    /**
     * Busca tipos de autorización por corporación con paginación.
     * @param corporation la corporación
     * @param pageable parámetros de paginación
     * @return página de tipos de autorización de la corporación
     */
    Page<AuthorizationType> findByCorporation(Corporation corporation, Pageable pageable);
    
    /**
     * Busca un tipo de autorización por ID cargando la relación corporation.
     * @param id el ID del tipo de autorización
     * @return el tipo de autorización con corporation cargado, si existe
     */
    @Query("SELECT at FROM AuthorizationType at LEFT JOIN FETCH at.corporation WHERE at.id = :id")
    Optional<AuthorizationType> findByIdWithCorporation(@Param("id") Long id);
    
    /**
     * Busca un tipo de autorización por ID y corporación.
     * @param id el ID del tipo de autorización
     * @param corporationId el ID de la corporación
     * @return el tipo de autorización si existe y pertenece a la corporación
     */
    @Query("SELECT at FROM AuthorizationType at WHERE at.id = :id AND at.corporation.id = :corporationId")
    Optional<AuthorizationType> findByIdAndCorporationId(@Param("id") Long id, @Param("corporationId") Long corporationId);
}

package com.univercloud.hydro.service;

import com.univercloud.hydro.entity.AuthorizationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de Tipos de Autorización.
 * Proporciona operaciones CRUD y lógica de negocio para tipos de autorización.
 */
public interface AuthorizationTypeService {
    
    /**
     * Crea un nuevo tipo de autorización.
     * @param authorizationType el tipo de autorización a crear
     * @return el tipo de autorización creado
     * @throws IllegalArgumentException si los datos son inválidos
     */
    AuthorizationType createAuthorizationType(AuthorizationType authorizationType);
    
    /**
     * Actualiza un tipo de autorización existente.
     * @param authorizationType el tipo de autorización a actualizar
     * @return el tipo de autorización actualizado
     * @throws IllegalArgumentException si el tipo de autorización no existe
     */
    AuthorizationType updateAuthorizationType(AuthorizationType authorizationType);
    
    /**
     * Obtiene un tipo de autorización por su ID.
     * @param id el ID del tipo de autorización
     * @return el tipo de autorización, si existe
     */
    Optional<AuthorizationType> getAuthorizationTypeById(Long id);
    
    /**
     * Obtiene todos los tipos de autorización de la corporación del usuario autenticado.
     * @param pageable parámetros de paginación
     * @return página de tipos de autorización
     */
    Page<AuthorizationType> getMyCorporationAuthorizationTypes(Pageable pageable);
    
    /**
     * Obtiene todos los tipos de autorización de la corporación del usuario autenticado.
     * @return lista de tipos de autorización
     */
    List<AuthorizationType> getAllMyCorporationAuthorizationTypes();
    
    /**
     * Elimina un tipo de autorización.
     * @param id el ID del tipo de autorización a eliminar
     * @return true si se eliminó correctamente
     * @throws IllegalArgumentException si el tipo de autorización no existe
     */
    boolean deleteAuthorizationType(Long id);
    
    /**
     * Verifica si existe un tipo de autorización con el nombre especificado.
     * @param name el nombre del tipo de autorización
     * @return true si existe, false en caso contrario
     */
    boolean existsByName(String name);
}

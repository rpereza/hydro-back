package com.univercloud.hydro.service;

import com.univercloud.hydro.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de Corporaciones.
 * Proporciona operaciones CRUD y lógica de negocio para corporaciones.
 */
public interface CorporationService {
    
    /**
     * Crea una nueva corporación para el usuario autenticado.
     * @param request los datos de la corporación a crear
     * @return la corporación creada
     * @throws IllegalStateException si el usuario ya tiene una corporación
     * @throws IllegalArgumentException si los datos son inválidos
     */
    CorporationResponse createCorporation(CreateCorporationRequest request);
    
    /**
     * Obtiene la corporación del usuario autenticado.
     * @return la corporación del usuario, si existe
     */
    Optional<CorporationResponse> getMyCorporation();
    
    /**
     * Obtiene una corporación por su ID.
     * @param id el ID de la corporación
     * @return la corporación, si existe
     */
    Optional<CorporationResponse> getCorporationById(Long id);

    /**
     * Obtiene todas las corporaciones (solo para administradores).
     * @param pageable parámetros de paginación
     * @return página de corporaciones
     */
    Page<CorporationResponse> getAllCorporations(Pageable pageable);
    
    /**
     * Actualiza la información de la corporación del usuario autenticado.
     * @param request los nuevos datos de la corporación
     * @return la corporación actualizada
     * @throws IllegalStateException si el usuario no tiene corporación
     */
    CorporationResponse updateMyCorporation(CreateCorporationRequest request);
    
    /**
     * Invita a un usuario a la corporación del usuario autenticado.
     * @param request los datos del usuario a invitar
     * @return el usuario creado e invitado
     * @throws IllegalStateException si el usuario no puede invitar usuarios
     * @throws IllegalArgumentException si el usuario ya existe
     */
    UserCorporationResponse inviteUser(InviteUserRequest request);
    
    /**
     * Obtiene todos los usuarios de la corporación del usuario autenticado.
     * @return lista de usuarios de la corporación
     * @throws IllegalStateException si el usuario no tiene corporación
     */
    List<UserCorporationResponse> getUsersInMyCorporation();
    
    /**
     * Obtiene todos los usuarios de una corporación específica (solo para administradores).
     * @param corporationId el ID de la corporación
     * @return lista de usuarios de la corporación
     */
    List<UserCorporationResponse> getUsersInCorporation(Long corporationId);
    
    /**
     * Remueve un usuario de la corporación del usuario autenticado.
     * @param userId el ID del usuario a remover
     * @return true si se removió correctamente
     * @throws IllegalStateException si el usuario no puede remover usuarios
     * @throws IllegalArgumentException si el usuario no existe en la corporación
     */
    boolean removeUserFromMyCorporation(Long userId);
    
    /**
     * Remueve un usuario de una corporación específica (solo para administradores).
     * @param corporationId el ID de la corporación
     * @param userId el ID del usuario a remover
     * @return true si se removió correctamente
     */
    boolean removeUserFromCorporation(Long corporationId, Long userId);
    
    /**
     * Verifica si el usuario autenticado puede crear una corporación.
     * @return true si puede crear una corporación, false en caso contrario
     */
    boolean canCreateCorporation();
    
    /**
     * Verifica si el usuario autenticado puede invitar usuarios a su corporación.
     * @return true si puede invitar usuarios, false en caso contrario
     */
    boolean canInviteUsers();
    
    /**
     * Obtiene estadísticas de la corporación del usuario autenticado.
     * @return estadísticas de la corporación
     * @throws IllegalStateException si el usuario no tiene corporación
     */
    CorporationStatsResponse getMyCorporationStats();
    
    /**
     * Busca corporaciones por nombre (búsqueda parcial).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de corporaciones que coinciden
     */
    List<CorporationResponse> searchCorporationsByName(String name);
    
    /**
     * Verifica si el usuario autenticado es propietario de su corporación.
     * @return true si es propietario, false en caso contrario
     */
    boolean isOwnerOfMyCorporation();
}

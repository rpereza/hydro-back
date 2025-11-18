package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.dto.*;
import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.Role;
import com.univercloud.hydro.entity.User;
import com.univercloud.hydro.exception.DuplicateResourceException;
import com.univercloud.hydro.repository.CorporationRepository;
import com.univercloud.hydro.repository.RoleRepository;
import com.univercloud.hydro.repository.UserRepository;
import com.univercloud.hydro.service.CorporationService;
import com.univercloud.hydro.util.AuthorizationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementación del servicio para la gestión de Corporaciones.
 */
@Service
@Transactional
public class CorporationServiceImpl implements CorporationService {
    
    @Autowired
    private CorporationRepository corporationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private AuthorizationUtils authorizationUtils;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public CorporationResponse createCorporation(CreateCorporationRequest request) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        if (!authorizationUtils.canCreateCorporation()) {
            throw new IllegalStateException("User already has a corporation or cannot create one");
        }
        
        if (corporationRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Corporation", "code", request.getCode());
        }
        
        Corporation corporation = new Corporation();
        corporation.setName(request.getName());
        corporation.setCode(request.getCode());
        corporation.setDescription(request.getDescription());
        corporation.setOwner(currentUser);
        
        Corporation savedCorporation = corporationRepository.save(corporation);
        
        // Asignar la corporación al usuario y marcar que ya creó una
        currentUser.setCorporation(savedCorporation);
        currentUser.setHasCreatedCorporation(true);
        userRepository.save(currentUser);
        
        return mapToCorporationResponse(savedCorporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<CorporationResponse> getMyCorporation() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null || currentUser.getCorporation() == null) {
            return Optional.empty();
        }
        
        // Cargar la corporación con todas sus relaciones desde el repositorio
        Long corporationId = currentUser.getCorporation().getId();
        Corporation corporation = corporationRepository.findByIdWithRelations(corporationId)
                .orElse(null);
        
        if (corporation == null) {
            return Optional.empty();
        }
        
        CorporationResponse response = mapToCorporationResponse(corporation);
        response.setUserCount(corporation.getUsers().size());
        
        return Optional.of(response);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<CorporationResponse> getCorporationById(Long id) {
        if (!authorizationUtils.hasFullAccess()) {
            throw new IllegalStateException("Access denied: Admin privileges required");
        }
        
        return corporationRepository.findByIdWithRelations(id)
                .map(this::mapToCorporationResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<CorporationResponse> getAllCorporations(Pageable pageable) {
        if (!authorizationUtils.hasFullAccess()) {
            throw new IllegalStateException("Access denied: Admin privileges required");
        }
        
        return corporationRepository.findAll(pageable)
                .map(this::mapToCorporationResponse);
    }
    
    @Override
    public CorporationResponse updateMyCorporation(CreateCorporationRequest request) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null || currentUser.getCorporation() == null) {
            throw new IllegalStateException("User does not have a corporation");
        }
        
        // Cargar la corporación con todas sus relaciones desde el repositorio
        Long corporationId = currentUser.getCorporation().getId();
        Corporation corporation = corporationRepository.findByIdWithRelations(corporationId)
                .orElseThrow(() -> new IllegalStateException("Corporation not found"));
        
        // Verificar que el código no esté en uso por otra corporación
        if (!corporation.getCode().equals(request.getCode()) && 
            corporationRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Corporation", "code", request.getCode());
        }
        
        corporation.setName(request.getName());
        corporation.setCode(request.getCode());
        corporation.setDescription(request.getDescription());
        
        Corporation savedCorporation = corporationRepository.save(corporation);
        // Recargar con relaciones para el mapeo
        savedCorporation = corporationRepository.findByIdWithRelations(savedCorporation.getId())
                .orElse(savedCorporation);
        return mapToCorporationResponse(savedCorporation);
    }
    
    @Override
    public UserCorporationResponse inviteUser(InviteUserRequest request) {
        if (!authorizationUtils.canInviteUsers()) {
            throw new IllegalStateException("User cannot invite users to corporation");
        }
        
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        // Verificar si el usuario ya existe
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateResourceException("User", "username", request.getUsername());
        }
        
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }
        
        // Crear nuevo usuario
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setCorporation(corporation);
        newUser.setHasCreatedCorporation(false);
        
        // Asignar rol USER por defecto
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("USER role not found"));
        newUser.addRole(userRole);
        
        User savedUser = userRepository.save(newUser);
        
        return mapToUserCorporationResponse(savedUser, false);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserCorporationResponse> getUsersInMyCorporation() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null || currentUser.getCorporation() == null) {
            throw new IllegalStateException("User does not have a corporation");
        }
        
        // Cargar la corporación con todas sus relaciones desde el repositorio
        Long corporationId = currentUser.getCorporation().getId();
        Corporation corporation = corporationRepository.findByIdWithRelations(corporationId)
                .orElseThrow(() -> new IllegalStateException("Corporation not found"));
        
        return corporation.getUsers().stream()
                .map(user -> mapToUserCorporationResponse(user, corporation.isOwner(user)))
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<UserCorporationResponse> getUsersInCorporation(Long corporationId) {
        if (!authorizationUtils.hasFullAccess()) {
            throw new IllegalStateException("Access denied: Admin privileges required");
        }
        
        Corporation corporation = corporationRepository.findByIdWithRelations(corporationId)
                .orElseThrow(() -> new IllegalArgumentException("Corporation not found"));
        
        return corporation.getUsers().stream()
                .map(user -> mapToUserCorporationResponse(user, corporation.isOwner(user)))
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean removeUserFromMyCorporation(Long userId) {
        if (!authorizationUtils.canInviteUsers()) {
            throw new IllegalStateException("User cannot remove users from corporation");
        }
        
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null || currentUser.getCorporation() == null) {
            throw new IllegalStateException("User does not have a corporation");
        }
        
        // Cargar la corporación con todas sus relaciones desde el repositorio
        Long corporationId = currentUser.getCorporation().getId();
        Corporation corporation = corporationRepository.findByIdWithRelations(corporationId)
                .orElseThrow(() -> new IllegalStateException("Corporation not found"));
        
        User userToRemove = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        if (!corporation.getUsers().contains(userToRemove)) {
            throw new IllegalArgumentException("User is not in this corporation");
        }
        
        if (corporation.isOwner(userToRemove)) {
            throw new IllegalArgumentException("Cannot remove corporation owner");
        }
        
        userToRemove.setCorporation(null);
        userRepository.save(userToRemove);
        
        return true;
    }
    
    @Override
    public boolean removeUserFromCorporation(Long corporationId, Long userId) {
        if (!authorizationUtils.hasFullAccess()) {
            throw new IllegalStateException("Access denied: Admin privileges required");
        }
        
        Corporation corporation = corporationRepository.findByIdWithRelations(corporationId)
                .orElseThrow(() -> new IllegalArgumentException("Corporation not found"));
        
        User userToRemove = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        if (corporation.isOwner(userToRemove)) {
            throw new IllegalArgumentException("Cannot remove corporation owner");
        }
        
        userToRemove.setCorporation(null);
        userRepository.save(userToRemove);
        
        return true;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canCreateCorporation() {
        return authorizationUtils.canCreateCorporation();
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canInviteUsers() {
        return authorizationUtils.canInviteUsers();
    }
    
    @Override
    @Transactional(readOnly = true)
    public CorporationStatsResponse getMyCorporationStats() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null || currentUser.getCorporation() == null) {
            throw new IllegalStateException("User does not have a corporation");
        }
        
        // Cargar la corporación con todas sus relaciones desde el repositorio
        Long corporationId = currentUser.getCorporation().getId();
        Corporation corporation = corporationRepository.findByIdWithRelations(corporationId)
                .orElseThrow(() -> new IllegalStateException("Corporation not found"));
        
        CorporationStatsResponse stats = new CorporationStatsResponse();
        stats.setCorporationId(corporation.getId());
        stats.setCorporationName(corporation.getName());
        stats.setTotalUsers(corporation.getUsers().size());
        stats.setActiveUsers((int) corporation.getUsers().stream()
                .filter(User::isEnabled)
                .count());
        stats.setCreatedAt(corporation.getCreatedAt());
        
        // TODO: Implementar conteo de entidades de negocio
        stats.setTotalDischarges(0);
        stats.setTotalMonitorings(0);
        stats.setTotalInvoices(0);
        
        return stats;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CorporationResponse> searchCorporationsByName(String name) {
        return corporationRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToCorporationResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isOwnerOfMyCorporation() {
        return authorizationUtils.isCorporationOwner();
    }
    
    // Métodos auxiliares para mapeo
    private CorporationResponse mapToCorporationResponse(Corporation corporation) {
        CorporationResponse response = new CorporationResponse();
        response.setId(corporation.getId());
        response.setName(corporation.getName());
        response.setCode(corporation.getCode());
        response.setDescription(corporation.getDescription());
        response.setCreatedAt(corporation.getCreatedAt());
        response.setUpdatedAt(corporation.getUpdatedAt());
        
        if (corporation.getOwner() != null) {
            UserCorporationResponse ownerResponse = mapToUserCorporationResponse(corporation.getOwner(), true);
            response.setOwner(ownerResponse);
        }
        
        return response;
    }
    
    private UserCorporationResponse mapToUserCorporationResponse(User user, boolean isOwner) {
        UserCorporationResponse response = new UserCorporationResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEnabled(user.isEnabled());
        response.setCreatedAt(user.getCreatedAt());
        response.setOwner(isOwner);
        
        Set<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        response.setRoles(roles);
        
        return response;
    }
}

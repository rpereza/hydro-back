package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.entity.AuthorizationType;
import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.User;
import com.univercloud.hydro.repository.AuthorizationTypeRepository;
import com.univercloud.hydro.service.AuthorizationTypeService;
import com.univercloud.hydro.util.AuthorizationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para la gestión de Tipos de Autorización.
 */
@Service
@Transactional
public class AuthorizationTypeServiceImpl implements AuthorizationTypeService {
    
    @Autowired
    private AuthorizationTypeRepository authorizationTypeRepository;
    
    @Autowired
    private AuthorizationUtils authorizationUtils;
    
    @Override
    public AuthorizationType createAuthorizationType(AuthorizationType authorizationType) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        // Verificar que no existe un tipo de autorización con el mismo nombre
        if (authorizationType.getName() != null && authorizationTypeRepository.existsByName(authorizationType.getName())) {
            throw new IllegalArgumentException("Authorization type with name '" + authorizationType.getName() + "' already exists");
        }
        
        // Asignar corporación y usuario creador
        authorizationType.setCorporation(corporation);
        authorizationType.setCreatedBy(currentUser);
        authorizationType.setCreatedAt(LocalDateTime.now());
        authorizationType.setActive(true);
        
        return authorizationTypeRepository.save(authorizationType);
    }
    
    @Override
    public AuthorizationType updateAuthorizationType(AuthorizationType authorizationType) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        AuthorizationType existingAuthorizationType = authorizationTypeRepository.findById(authorizationType.getId())
                .orElseThrow(() -> new IllegalArgumentException("Authorization type not found"));
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null || !corporation.equals(existingAuthorizationType.getCorporation())) {
            throw new IllegalStateException("Access denied: Authorization type does not belong to your corporation");
        }
        
        // Verificar cambios en el nombre
        if (authorizationType.getName() != null && !authorizationType.getName().equals(existingAuthorizationType.getName())) {
            if (authorizationTypeRepository.existsByName(authorizationType.getName())) {
                throw new IllegalArgumentException("Authorization type with name '" + authorizationType.getName() + "' already exists");
            }
        }
        
        // Actualizar campos
        existingAuthorizationType.setName(authorizationType.getName());
        existingAuthorizationType.setActive(authorizationType.isActive());
        existingAuthorizationType.setUpdatedBy(currentUser);
        existingAuthorizationType.setUpdatedAt(LocalDateTime.now());
        
        return authorizationTypeRepository.save(existingAuthorizationType);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<AuthorizationType> getAuthorizationTypeById(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        Optional<AuthorizationType> authorizationType = authorizationTypeRepository.findById(id);
        if (authorizationType.isPresent() && !corporation.equals(authorizationType.get().getCorporation())) {
            throw new IllegalStateException("Access denied: Authorization type does not belong to your corporation");
        }
        
        return authorizationType;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<AuthorizationType> getMyCorporationAuthorizationTypes(Pageable pageable) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return authorizationTypeRepository.findByCorporation(corporation, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AuthorizationType> getAllMyCorporationAuthorizationTypes() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return authorizationTypeRepository.findByCorporation(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AuthorizationType> getActiveMyCorporationAuthorizationTypes() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return authorizationTypeRepository.findByCorporationAndIsActiveTrue(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AuthorizationType> getAllActiveAuthorizationTypes() {
        return authorizationTypeRepository.findByIsActiveTrue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AuthorizationType> getAllInactiveAuthorizationTypes() {
        return authorizationTypeRepository.findByIsActiveFalse();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<AuthorizationType> getAuthorizationTypeByName(String name) {
        return authorizationTypeRepository.findByName(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AuthorizationType> searchAuthorizationTypesByName(String name) {
        return authorizationTypeRepository.findByNameContainingIgnoreCase(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AuthorizationType> searchActiveAuthorizationTypesByName(String name) {
        return authorizationTypeRepository.findByIsActiveTrueAndNameContainingIgnoreCase(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AuthorizationType> getAuthorizationTypesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return authorizationTypeRepository.findByCreatedAtBetween(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countMyCorporationAuthorizationTypes() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return authorizationTypeRepository.countByCorporation(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countActiveMyCorporationAuthorizationTypes() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return authorizationTypeRepository.countByCorporationAndIsActiveTrue(corporation);
    }
    
    @Override
    public boolean activateAuthorizationType(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        AuthorizationType authorizationType = authorizationTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Authorization type not found"));
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null || !corporation.equals(authorizationType.getCorporation())) {
            throw new IllegalStateException("Access denied: Authorization type does not belong to your corporation");
        }
        
        authorizationType.setActive(true);
        authorizationType.setUpdatedBy(currentUser);
        authorizationType.setUpdatedAt(LocalDateTime.now());
        
        authorizationTypeRepository.save(authorizationType);
        return true;
    }
    
    @Override
    public boolean deactivateAuthorizationType(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        AuthorizationType authorizationType = authorizationTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Authorization type not found"));
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null || !corporation.equals(authorizationType.getCorporation())) {
            throw new IllegalStateException("Access denied: Authorization type does not belong to your corporation");
        }
        
        authorizationType.setActive(false);
        authorizationType.setUpdatedBy(currentUser);
        authorizationType.setUpdatedAt(LocalDateTime.now());
        
        authorizationTypeRepository.save(authorizationType);
        return true;
    }
    
    @Override
    public boolean deleteAuthorizationType(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        AuthorizationType authorizationType = authorizationTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Authorization type not found"));
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null || !corporation.equals(authorizationType.getCorporation())) {
            throw new IllegalStateException("Access denied: Authorization type does not belong to your corporation");
        }
        
        authorizationTypeRepository.delete(authorizationType);
        return true;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return authorizationTypeRepository.existsByName(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AuthorizationType> getAuthorizationTypesOrderByCreatedAtDesc() {
        return authorizationTypeRepository.findAllOrderByCreatedAtDesc();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AuthorizationType> getActiveAuthorizationTypesOrderByName() {
        return authorizationTypeRepository.findByIsActiveTrueOrderByName();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AuthorizationType> getMyCorporationAuthorizationTypesOrderByName() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return authorizationTypeRepository.findByCorporationOrderByName(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AuthorizationType> getActiveMyCorporationAuthorizationTypesOrderByName() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return authorizationTypeRepository.findByCorporationAndIsActiveTrueOrderByName(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public AuthorizationTypeStats getMyCorporationAuthorizationTypeStats() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        AuthorizationTypeStats stats = new AuthorizationTypeStats();
        stats.setTotalAuthorizationTypes(authorizationTypeRepository.countByCorporation(corporation));
        stats.setActiveAuthorizationTypes(authorizationTypeRepository.countByCorporationAndIsActiveTrue(corporation));
        stats.setInactiveAuthorizationTypes(stats.getTotalAuthorizationTypes() - stats.getActiveAuthorizationTypes());
        
        // Contar usuarios asociados a tipos de autorización de la corporación
        long totalUsers = authorizationTypeRepository.findByCorporation(corporation)
                .stream()
                .mapToLong(authType -> authType.getUsers().size())
                .sum();
        stats.setTotalUsers(totalUsers);
        
        return stats;
    }
}

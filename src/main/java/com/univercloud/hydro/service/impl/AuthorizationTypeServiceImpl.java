package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.entity.AuthorizationType;
import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.User;
import com.univercloud.hydro.exception.DuplicateResourceException;
import com.univercloud.hydro.exception.ResourceInUseException;
import com.univercloud.hydro.repository.AuthorizationTypeRepository;
import com.univercloud.hydro.repository.DischargeUserRepository;
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
    private DischargeUserRepository dischargeUserRepository;
    
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
            throw new DuplicateResourceException("AuthorizationType", "name", authorizationType.getName());
        }
        
        // Asignar corporación y usuario creador
        authorizationType.setCorporation(corporation);
        authorizationType.setCreatedBy(currentUser);
        authorizationType.setCreatedAt(LocalDateTime.now());
        
        return authorizationTypeRepository.save(authorizationType);
    }
    
    @Override
    public AuthorizationType updateAuthorizationType(AuthorizationType authorizationType) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        // Buscar directamente por ID y corporationId para evitar problemas con lazy loading
        AuthorizationType existingAuthorizationType = authorizationTypeRepository.findByIdAndCorporationId(
                authorizationType.getId(), corporation.getId())
                .orElseThrow(() -> new IllegalStateException("Access denied: Authorization type not found or does not belong to your corporation"));
        
        // Verificar cambios en el nombre
        if (authorizationType.getName() != null && !authorizationType.getName().equals(existingAuthorizationType.getName())) {
            if (authorizationTypeRepository.existsByName(authorizationType.getName())) {
                throw new DuplicateResourceException("AuthorizationType", "name", authorizationType.getName());
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
        
        // Buscar directamente por ID y corporationId para evitar problemas con lazy loading
        return authorizationTypeRepository.findByIdAndCorporationId(id, corporation.getId());
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
    public boolean deleteAuthorizationType(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        // Buscar directamente por ID y corporationId para evitar problemas con lazy loading
        AuthorizationType authorizationType = authorizationTypeRepository.findByIdAndCorporationId(id, corporation.getId())
                .orElseThrow(() -> new IllegalStateException("Access denied: Authorization type not found or does not belong to your corporation"));
        
        // Verificar si hay usuarios de descarga asociados
        long dischargeUserCount = dischargeUserRepository.countByAuthorizationTypeId(id);
        if (dischargeUserCount > 0) {
            throw new ResourceInUseException("AuthorizationType", "id", id, "DischargeUser", dischargeUserCount);
        }
        
        authorizationTypeRepository.delete(authorizationType);
        return true;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return authorizationTypeRepository.existsByName(name);
    }
}

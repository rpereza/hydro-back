package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.User;
import com.univercloud.hydro.entity.WaterBasin;
import com.univercloud.hydro.exception.DuplicateResourceException;
import com.univercloud.hydro.exception.ResourceInUseException;
import com.univercloud.hydro.exception.ResourceNotFoundException;
import com.univercloud.hydro.repository.WaterBasinRepository;
import com.univercloud.hydro.service.WaterBasinService;
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
 * Implementación del servicio para la gestión de Cuencas Hidrográficas.
 */
@Service
@Transactional
public class WaterBasinServiceImpl implements WaterBasinService {
    
    @Autowired
    private WaterBasinRepository waterBasinRepository;
    
    @Autowired
    private AuthorizationUtils authorizationUtils;
    
    @Override
    public WaterBasin createWaterBasin(WaterBasin waterBasin) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        // Verificar que no existe una cuenca con el mismo nombre en la corporación
        if (waterBasin.getName() != null && waterBasinRepository.existsByCorporationAndName(corporation, waterBasin.getName())) {
            throw new DuplicateResourceException("WaterBasin", "name", waterBasin.getName());
        }
        
        // Asignar corporación y usuario creador
        waterBasin.setCorporation(corporation);
        waterBasin.setCreatedBy(currentUser);
        waterBasin.setCreatedAt(LocalDateTime.now());
        waterBasin.setActive(waterBasin.isActive());
        
        return waterBasinRepository.save(waterBasin);
    }
    
    @Override
    public WaterBasin updateWaterBasin(WaterBasin waterBasin) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        WaterBasin existingWaterBasin = waterBasinRepository.findById(waterBasin.getId())
                .orElseThrow(() -> new ResourceNotFoundException("WaterBasin", "id", waterBasin.getId()));
        
        Corporation corporation = currentUser.getCorporation();
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (corporation == null || existingWaterBasin.getCorporation() == null || !existingWaterBasin.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalStateException("Access denied: Water basin does not belong to your corporation");
        }
        
        // Verificar cambios en el nombre
        if (waterBasin.getName() != null && !waterBasin.getName().equals(existingWaterBasin.getName())) {
            if (waterBasinRepository.existsByCorporationAndNameExcludingId(corporation, waterBasin.getName(), existingWaterBasin.getId())) {
                throw new DuplicateResourceException("WaterBasin", "name", waterBasin.getName());
            }
        }
        
        // Actualizar campos
        existingWaterBasin.setName(waterBasin.getName());
        existingWaterBasin.setDescription(waterBasin.getDescription());
        existingWaterBasin.setActive(waterBasin.isActive());
        existingWaterBasin.setUpdatedBy(currentUser);
        existingWaterBasin.setUpdatedAt(LocalDateTime.now());
        
        return waterBasinRepository.save(existingWaterBasin);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<WaterBasin> getWaterBasinById(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        // Buscar directamente por ID y corporationId para evitar problemas con lazy loading
        return waterBasinRepository.findByIdAndCorporationId(id, corporation.getId());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<WaterBasin> getMyCorporationWaterBasins(Pageable pageable) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return waterBasinRepository.findByCorporation(corporation, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WaterBasin> getAllMyCorporationWaterBasins() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return waterBasinRepository.findByCorporation(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WaterBasin> searchWaterBasinsByName(String name) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return waterBasinRepository.findByCorporationAndNameContainingIgnoreCase(corporation, name);
    }
    
    @Override
    public boolean deleteWaterBasin(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        WaterBasin waterBasin = waterBasinRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WaterBasin", "id", id));
        
        Corporation corporation = currentUser.getCorporation();
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (corporation == null || waterBasin.getCorporation() == null || !waterBasin.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalStateException("Access denied: Water basin does not belong to your corporation");
        }
        
        // Verificar que no tenga secciones asociadas
        long basinSectionCount = waterBasinRepository.countBasinSectionsByWaterBasinId(id);
        if (basinSectionCount > 0) {
            throw new ResourceInUseException("WaterBasin", "id", id, "BasinSection", basinSectionCount);
        }
        
        waterBasinRepository.delete(waterBasin);
        return true;
    }
}

package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.User;
import com.univercloud.hydro.entity.WaterBasin;
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
        
        // Verificar que no existe una cuenca con el mismo nombre
        if (waterBasin.getName() != null && waterBasinRepository.existsByName(waterBasin.getName())) {
            throw new IllegalArgumentException("Water basin with name '" + waterBasin.getName() + "' already exists");
        }
        
        // Asignar corporación y usuario creador
        waterBasin.setCorporation(corporation);
        waterBasin.setCreatedBy(currentUser);
        waterBasin.setCreatedAt(LocalDateTime.now());
        waterBasin.setActive(true);
        
        return waterBasinRepository.save(waterBasin);
    }
    
    @Override
    public WaterBasin updateWaterBasin(WaterBasin waterBasin) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        WaterBasin existingWaterBasin = waterBasinRepository.findById(waterBasin.getId())
                .orElseThrow(() -> new IllegalArgumentException("Water basin not found"));
        
        Corporation corporation = currentUser.getCorporation();
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (corporation == null || existingWaterBasin.getCorporation() == null || !existingWaterBasin.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalStateException("Access denied: Water basin does not belong to your corporation");
        }
        
        // Verificar cambios en el nombre
        if (waterBasin.getName() != null && !waterBasin.getName().equals(existingWaterBasin.getName())) {
            if (waterBasinRepository.existsByName(waterBasin.getName())) {
                throw new IllegalArgumentException("Water basin with name '" + waterBasin.getName() + "' already exists");
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
    public List<WaterBasin> getActiveMyCorporationWaterBasins() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return waterBasinRepository.findByCorporationAndIsActiveTrue(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WaterBasin> getAllActiveWaterBasins() {
        return waterBasinRepository.findByIsActiveTrue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WaterBasin> getAllInactiveWaterBasins() {
        return waterBasinRepository.findByIsActiveFalse();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<WaterBasin> getWaterBasinByName(String name) {
        return waterBasinRepository.findByName(name);
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
    @Transactional(readOnly = true)
    public List<WaterBasin> searchActiveWaterBasinsByName(String name) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return waterBasinRepository.findByCorporationAndIsActiveTrue(corporation)
                .stream()
                .filter(waterBasin -> waterBasin.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WaterBasin> getWaterBasinsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return waterBasinRepository.findByCorporationAndCreatedAtBetween(corporation, startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countMyCorporationWaterBasins() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return waterBasinRepository.countByCorporationId(corporation.getId());
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countActiveMyCorporationWaterBasins() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return waterBasinRepository.countActiveByCorporationId(corporation.getId());
    }
    
    @Override
    public boolean activateWaterBasin(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        WaterBasin waterBasin = waterBasinRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Water basin not found"));
        
        Corporation corporation = currentUser.getCorporation();
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (corporation == null || waterBasin.getCorporation() == null || !waterBasin.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalStateException("Access denied: Water basin does not belong to your corporation");
        }
        
        waterBasin.setActive(true);
        waterBasin.setUpdatedBy(currentUser);
        waterBasin.setUpdatedAt(LocalDateTime.now());
        waterBasinRepository.save(waterBasin);
        
        return true;
    }
    
    @Override
    public boolean deactivateWaterBasin(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        WaterBasin waterBasin = waterBasinRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Water basin not found"));
        
        Corporation corporation = currentUser.getCorporation();
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (corporation == null || waterBasin.getCorporation() == null || !waterBasin.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalStateException("Access denied: Water basin does not belong to your corporation");
        }
        
        waterBasin.setActive(false);
        waterBasin.setUpdatedBy(currentUser);
        waterBasin.setUpdatedAt(LocalDateTime.now());
        waterBasinRepository.save(waterBasin);
        
        return true;
    }
    
    @Override
    public boolean deleteWaterBasin(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        WaterBasin waterBasin = waterBasinRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Water basin not found"));
        
        Corporation corporation = currentUser.getCorporation();
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (corporation == null || waterBasin.getCorporation() == null || !waterBasin.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalStateException("Access denied: Water basin does not belong to your corporation");
        }
        
        // Verificar que no tenga secciones asociadas
        if (!waterBasin.getSections().isEmpty()) {
            throw new IllegalStateException("Cannot delete water basin with associated sections");
        }
        
        waterBasinRepository.delete(waterBasin);
        return true;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return waterBasinRepository.existsByName(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WaterBasin> getWaterBasinsOrderByCreatedAtDesc() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return waterBasinRepository.findByCorporationOrderByCreatedAtDesc(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WaterBasin> getActiveWaterBasinsOrderByName() {
        return waterBasinRepository.findActiveOrderByName();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WaterBasin> getMyCorporationWaterBasinsOrderByName() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return waterBasinRepository.findByCorporationOrderByName(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WaterBasin> getActiveMyCorporationWaterBasinsOrderByName() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return waterBasinRepository.findActiveByCorporationOrderByName(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public WaterBasinStats getMyCorporationWaterBasinStats() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        WaterBasinStats stats = new WaterBasinStats();
        
        List<WaterBasin> allWaterBasins = waterBasinRepository.findByCorporation(corporation);
        
        stats.setTotalWaterBasins(allWaterBasins.size());
        stats.setActiveWaterBasins((int) allWaterBasins.stream().filter(WaterBasin::isActive).count());
        stats.setInactiveWaterBasins((int) allWaterBasins.stream().filter(wb -> !wb.isActive()).count());
        
        // Contar secciones totales
        long totalSections = allWaterBasins.stream()
                .mapToLong(wb -> wb.getSections().size())
                .sum();
        stats.setTotalBasinSections(totalSections);
        
        // Contar secciones activas
        long activeSections = allWaterBasins.stream()
                .flatMap(wb -> wb.getSections().stream())
                .filter(section -> section.isActive())
                .count();
        stats.setActiveBasinSections(activeSections);
        
        // TODO: Implementar conteo de descargas y monitoreos
        stats.setTotalDischarges(0);
        stats.setTotalMonitorings(0);
        
        return stats;
    }
}

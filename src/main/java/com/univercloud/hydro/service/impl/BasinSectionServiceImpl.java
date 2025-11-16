package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.entity.BasinSection;
import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.User;
import com.univercloud.hydro.entity.WaterBasin;
import com.univercloud.hydro.exception.DuplicateResourceException;
import com.univercloud.hydro.exception.ResourceInUseException;
import com.univercloud.hydro.exception.ResourceNotFoundException;
import com.univercloud.hydro.repository.BasinSectionRepository;
import com.univercloud.hydro.repository.DischargeRepository;
import com.univercloud.hydro.repository.MonitoringStationRepository;
import com.univercloud.hydro.repository.WaterBasinRepository;
import com.univercloud.hydro.service.BasinSectionService;
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
 * Implementación del servicio para la gestión de Secciones de Cuenca.
 * Proporciona operaciones CRUD y lógica de negocio para secciones de cuenca.
 */
@Service
@Transactional
public class BasinSectionServiceImpl implements BasinSectionService {
    
    @Autowired
    private BasinSectionRepository basinSectionRepository;
    
    @Autowired
    private WaterBasinRepository waterBasinRepository;
    
    @Autowired
    private MonitoringStationRepository monitoringStationRepository;
    
    @Autowired
    private DischargeRepository dischargeRepository;
    
    @Autowired
    private AuthorizationUtils authorizationUtils;
    
    @Override
    @Transactional
    public BasinSection createBasinSection(BasinSection basinSection) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        // Verificar que la cuenca hidrográfica pertenezca a la corporación
        if (basinSection.getWaterBasin() == null) {
            throw new IllegalArgumentException("Water basin is required");
        }
        
        Optional<WaterBasin> waterBasinOpt = waterBasinRepository.findById(basinSection.getWaterBasin().getId());
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (waterBasinOpt.isEmpty() || waterBasinOpt.get().getCorporation() == null || !waterBasinOpt.get().getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalArgumentException("Water basin does not belong to your corporation");
        }
        
        WaterBasin waterBasin = waterBasinOpt.get();
        
        // Verificar que el nombre no exista en la corporación y cuenca hidrográfica
        if (basinSection.getName() != null && basinSectionRepository.existsByCorporationAndWaterBasinAndName(corporation, waterBasin, basinSection.getName())) {
            throw new DuplicateResourceException("BasinSection", "name", basinSection.getName());
        }
        
        basinSection.setCorporation(corporation);
        basinSection.setCreatedBy(currentUser);
        basinSection.setCreatedAt(LocalDateTime.now());
        
        return basinSectionRepository.save(basinSection);
    }
    
    @Override
    @Transactional
    public BasinSection updateBasinSection(BasinSection basinSection) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        Optional<BasinSection> existingOpt = basinSectionRepository.findById(basinSection.getId());
        if (existingOpt.isEmpty()) {
            throw new ResourceNotFoundException("BasinSection", "id", basinSection.getId());
        }
        
        BasinSection existing = existingOpt.get();
        
        // Verificar que pertenezca a la corporación del usuario
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (existing.getCorporation() == null || !existing.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalArgumentException("You do not have permission to update this basin section");
        }
        
        // Verificar que la cuenca hidrográfica pertenezca a la corporación
        if (basinSection.getWaterBasin() == null) {
            throw new IllegalArgumentException("Water basin is required");
        }
        
        Optional<WaterBasin> waterBasinOpt = waterBasinRepository.findById(basinSection.getWaterBasin().getId());
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (waterBasinOpt.isEmpty() || waterBasinOpt.get().getCorporation() == null || !waterBasinOpt.get().getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalArgumentException("Water basin does not belong to your corporation");
        }
        
        WaterBasin waterBasin = waterBasinOpt.get();
        
        // Verificar que el nombre no exista en la corporación y cuenca hidrográfica (si cambió el nombre o la cuenca)
        boolean nameChanged = basinSection.getName() != null && !existing.getName().equals(basinSection.getName());
        boolean waterBasinChanged = !existing.getWaterBasin().getId().equals(waterBasin.getId());
        
        if (nameChanged || waterBasinChanged) {
            if (basinSectionRepository.existsByCorporationAndWaterBasinAndNameExcludingId(corporation, waterBasin, basinSection.getName(), existing.getId())) {
                throw new DuplicateResourceException("BasinSection", "name", basinSection.getName());
            }
        }
        
        existing.setName(basinSection.getName());
        existing.setDescription(basinSection.getDescription());
        existing.setWaterBasin(basinSection.getWaterBasin());
        existing.setActive(basinSection.isActive());
        existing.setUpdatedBy(currentUser);
        existing.setUpdatedAt(LocalDateTime.now());
        
        return basinSectionRepository.save(existing);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<BasinSection> getBasinSectionById(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        // Buscar directamente por ID y corporationId para evitar problemas con lazy loading
        return basinSectionRepository.findByIdAndCorporationId(id, corporation.getId());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<BasinSection> getMyCorporationBasinSections(Pageable pageable) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        return basinSectionRepository.findByCorporation(corporation, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BasinSection> getAllMyCorporationBasinSections() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        return basinSectionRepository.findByCorporation(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BasinSection> getBasinSectionsByWaterBasin(Long waterBasinId) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        Optional<WaterBasin> waterBasinOpt = waterBasinRepository.findById(waterBasinId);
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (waterBasinOpt.isEmpty() || waterBasinOpt.get().getCorporation() == null || !waterBasinOpt.get().getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalArgumentException("Water basin does not belong to your corporation");
        }
        
        return basinSectionRepository.findByWaterBasinAndIsActiveTrue(waterBasinOpt.get());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BasinSection> searchBasinSectionsByName(String name) {
        return basinSectionRepository.findByNameContainingIgnoreCase(name);
    }
    
    @Override
    @Transactional
    public boolean deleteBasinSection(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        Optional<BasinSection> basinSectionOpt = basinSectionRepository.findById(id);
        if (basinSectionOpt.isEmpty()) {
            throw new ResourceNotFoundException("BasinSection", "id", id);
        }
        
        BasinSection basinSection = basinSectionOpt.get();
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (basinSection.getCorporation() == null || !basinSection.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalArgumentException("You do not have permission to delete this basin section");
        }
        
        // Verificar si hay estaciones de monitoreo asociadas
        long monitoringStationCount = monitoringStationRepository.countByBasinSectionId(id);
        if (monitoringStationCount > 0) {
            throw new ResourceInUseException("BasinSection", "id", id, "MonitoringStation", monitoringStationCount);
        }
        
        // Verificar si hay descargas asociadas
        long dischargeCount = dischargeRepository.countByBasinSectionId(id);
        if (dischargeCount > 0) {
            throw new ResourceInUseException("BasinSection", "id", id, "Discharge", dischargeCount);
        }
        
        basinSectionRepository.delete(basinSection);
        return true;
    }
    
}

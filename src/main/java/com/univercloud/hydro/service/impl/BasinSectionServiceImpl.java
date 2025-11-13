package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.entity.BasinSection;
import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.User;
import com.univercloud.hydro.entity.WaterBasin;
import com.univercloud.hydro.repository.BasinSectionRepository;
import com.univercloud.hydro.repository.DischargeRepository;
import com.univercloud.hydro.repository.DischargeMonitoringRepository;
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
    private DischargeRepository dischargeRepository;
    
    @Autowired
    private DischargeMonitoringRepository dischargeMonitoringRepository;
    
    @Autowired
    private AuthorizationUtils authorizationUtils;
    
    @Override
    @Transactional
    public BasinSection createBasinSection(BasinSection basinSection) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        // Verificar que el nombre no exista
        if (existsByName(basinSection.getName())) {
            throw new IllegalArgumentException("Ya existe una sección de cuenca con el nombre: " + basinSection.getName());
        }
        
        // Verificar que la cuenca hidrográfica pertenezca a la corporación
        if (basinSection.getWaterBasin() != null) {
            Optional<WaterBasin> waterBasinOpt = waterBasinRepository.findById(basinSection.getWaterBasin().getId());
            if (waterBasinOpt.isEmpty() || !waterBasinOpt.get().getCorporation().equals(corporation)) {
                throw new IllegalArgumentException("La cuenca hidrográfica no pertenece a su corporación");
            }
        }
        
        basinSection.setCorporation(corporation);
        basinSection.setCreatedBy(currentUser);
        basinSection.setUpdatedBy(currentUser);
        basinSection.setCreatedAt(LocalDateTime.now());
        basinSection.setUpdatedAt(LocalDateTime.now());
        
        return basinSectionRepository.save(basinSection);
    }
    
    @Override
    @Transactional
    public BasinSection updateBasinSection(BasinSection basinSection) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<BasinSection> existingOpt = basinSectionRepository.findById(basinSection.getId());
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la sección de cuenca con ID: " + basinSection.getId());
        }
        
        BasinSection existing = existingOpt.get();
        
        // Verificar que pertenezca a la corporación del usuario
        if (!existing.getCorporation().equals(corporation)) {
            throw new IllegalArgumentException("No tiene permisos para actualizar esta sección de cuenca");
        }
        
        // Verificar que el nombre no exista (si cambió)
        if (!existing.getName().equals(basinSection.getName()) && existsByName(basinSection.getName())) {
            throw new IllegalArgumentException("Ya existe una sección de cuenca con el nombre: " + basinSection.getName());
        }
        
        // Verificar que la cuenca hidrográfica pertenezca a la corporación
        if (basinSection.getWaterBasin() != null) {
            Optional<WaterBasin> waterBasinOpt = waterBasinRepository.findById(basinSection.getWaterBasin().getId());
            if (waterBasinOpt.isEmpty() || !waterBasinOpt.get().getCorporation().equals(corporation)) {
                throw new IllegalArgumentException("La cuenca hidrográfica no pertenece a su corporación");
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
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
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
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        return basinSectionRepository.findByCorporation(corporation, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BasinSection> getAllMyCorporationBasinSections() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        return basinSectionRepository.findByCorporation(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BasinSection> getBasinSectionsByWaterBasin(Long waterBasinId) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<WaterBasin> waterBasinOpt = waterBasinRepository.findById(waterBasinId);
        if (waterBasinOpt.isEmpty() || !waterBasinOpt.get().getCorporation().equals(corporation)) {
            throw new IllegalArgumentException("La cuenca hidrográfica no pertenece a su corporación");
        }
        
        return basinSectionRepository.findByWaterBasin(waterBasinOpt.get());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BasinSection> getActiveBasinSectionsByWaterBasin(Long waterBasinId) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<WaterBasin> waterBasinOpt = waterBasinRepository.findById(waterBasinId);
        if (waterBasinOpt.isEmpty() || !waterBasinOpt.get().getCorporation().equals(corporation)) {
            throw new IllegalArgumentException("La cuenca hidrográfica no pertenece a su corporación");
        }
        
        return basinSectionRepository.findByWaterBasinAndIsActiveTrue(waterBasinOpt.get());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BasinSection> getActiveMyCorporationBasinSections() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        return basinSectionRepository.findByCorporationAndIsActiveTrue(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BasinSection> getAllActiveBasinSections() {
        return basinSectionRepository.findByIsActiveTrue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BasinSection> getAllInactiveBasinSections() {
        return basinSectionRepository.findByIsActiveFalse();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<BasinSection> getBasinSectionByName(String name) {
        return basinSectionRepository.findByName(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BasinSection> searchBasinSectionsByName(String name) {
        return basinSectionRepository.findByNameContainingIgnoreCase(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BasinSection> searchBasinSectionsByWaterBasinAndName(Long waterBasinId, String name) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<WaterBasin> waterBasinOpt = waterBasinRepository.findById(waterBasinId);
        if (waterBasinOpt.isEmpty() || !waterBasinOpt.get().getCorporation().equals(corporation)) {
            throw new IllegalArgumentException("La cuenca hidrográfica no pertenece a su corporación");
        }
        
        return basinSectionRepository.findByWaterBasinAndNameContainingIgnoreCase(waterBasinOpt.get(), name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BasinSection> searchActiveBasinSectionsByName(String name) {
        return basinSectionRepository.findActiveByNameContainingIgnoreCase(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BasinSection> getBasinSectionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return basinSectionRepository.findByCreatedAtBetween(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countBasinSectionsByWaterBasin(Long waterBasinId) {
        return basinSectionRepository.countByWaterBasinId(waterBasinId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countActiveBasinSectionsByWaterBasin(Long waterBasinId) {
        return basinSectionRepository.countActiveByWaterBasinId(waterBasinId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countMyCorporationBasinSections() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        return basinSectionRepository.countByCorporationId(corporation.getId());
    }
    
    @Override
    @Transactional
    public boolean activateBasinSection(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<BasinSection> basinSectionOpt = basinSectionRepository.findById(id);
        if (basinSectionOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la sección de cuenca con ID: " + id);
        }
        
        BasinSection basinSection = basinSectionOpt.get();
        if (!basinSection.getCorporation().equals(corporation)) {
            throw new IllegalArgumentException("No tiene permisos para activar esta sección de cuenca");
        }
        
        basinSection.setActive(true);
        basinSection.setUpdatedBy(currentUser);
        basinSection.setUpdatedAt(LocalDateTime.now());
        
        basinSectionRepository.save(basinSection);
        return true;
    }
    
    @Override
    @Transactional
    public boolean deactivateBasinSection(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<BasinSection> basinSectionOpt = basinSectionRepository.findById(id);
        if (basinSectionOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la sección de cuenca con ID: " + id);
        }
        
        BasinSection basinSection = basinSectionOpt.get();
        if (!basinSection.getCorporation().equals(corporation)) {
            throw new IllegalArgumentException("No tiene permisos para desactivar esta sección de cuenca");
        }
        
        basinSection.setActive(false);
        basinSection.setUpdatedBy(currentUser);
        basinSection.setUpdatedAt(LocalDateTime.now());
        
        basinSectionRepository.save(basinSection);
        return true;
    }
    
    @Override
    @Transactional
    public boolean deleteBasinSection(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<BasinSection> basinSectionOpt = basinSectionRepository.findById(id);
        if (basinSectionOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la sección de cuenca con ID: " + id);
        }
        
        BasinSection basinSection = basinSectionOpt.get();
        if (!basinSection.getCorporation().equals(corporation)) {
            throw new IllegalArgumentException("No tiene permisos para eliminar esta sección de cuenca");
        }
        
        basinSectionRepository.delete(basinSection);
        return true;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return basinSectionRepository.existsByName(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BasinSection> getBasinSectionsOrderByName() {
        return basinSectionRepository.findAllOrderByName();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BasinSection> getBasinSectionsByWaterBasinOrderByName(Long waterBasinId) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<WaterBasin> waterBasinOpt = waterBasinRepository.findById(waterBasinId);
        if (waterBasinOpt.isEmpty() || !waterBasinOpt.get().getCorporation().equals(corporation)) {
            throw new IllegalArgumentException("La cuenca hidrográfica no pertenece a su corporación");
        }
        
        return basinSectionRepository.findByWaterBasinOrderByName(waterBasinOpt.get());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BasinSection> getActiveBasinSectionsOrderByName() {
        return basinSectionRepository.findActiveOrderByName();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BasinSection> getMyCorporationBasinSectionsOrderByName() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        return basinSectionRepository.findByCorporationOrderByName(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BasinSection> getBasinSectionsOrderByCreatedAtDesc() {
        return basinSectionRepository.findAllOrderByCreatedAtDesc();
    }
    
    @Override
    @Transactional(readOnly = true)
    public BasinSectionStats getMyCorporationBasinSectionStats() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        BasinSectionStats stats = new BasinSectionStats();
        
        // Estadísticas básicas
        long totalBasinSections = basinSectionRepository.countByCorporationId(corporation.getId());
        long activeBasinSections = basinSectionRepository.findByCorporationAndIsActiveTrue(corporation).size();
        long inactiveBasinSections = totalBasinSections - activeBasinSections;
        
        // Estadísticas de cuencas hidrográficas
        long totalWaterBasins = waterBasinRepository.countByCorporationId(corporation.getId());
        
        // Estadísticas de descargas
        long totalDischarges = dischargeRepository.countByCorporationId(corporation.getId());
        
        // Estadísticas de monitoreos
        long totalMonitorings = dischargeMonitoringRepository.countByCorporationId(corporation.getId());
        
        // Promedio de secciones por cuenca
        long averageSectionsPerBasin = totalWaterBasins > 0 ? totalBasinSections / totalWaterBasins : 0;
        
        stats.setTotalBasinSections(totalBasinSections);
        stats.setActiveBasinSections(activeBasinSections);
        stats.setInactiveBasinSections(inactiveBasinSections);
        stats.setTotalWaterBasins(totalWaterBasins);
        stats.setTotalDischarges(totalDischarges);
        stats.setTotalMonitorings(totalMonitorings);
        stats.setAverageSectionsPerBasin(averageSectionsPerBasin);
        
        return stats;
    }
}

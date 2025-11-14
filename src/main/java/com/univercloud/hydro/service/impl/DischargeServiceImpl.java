package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.Discharge;
import com.univercloud.hydro.entity.DischargeUser;
import com.univercloud.hydro.entity.Municipality;
import com.univercloud.hydro.entity.BasinSection;
import com.univercloud.hydro.entity.User;
import com.univercloud.hydro.repository.DischargeRepository;
import com.univercloud.hydro.repository.DischargeUserRepository;
import com.univercloud.hydro.repository.MunicipalityRepository;
import com.univercloud.hydro.repository.BasinSectionRepository;
import com.univercloud.hydro.service.DischargeService;
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
 * Implementación del servicio para la gestión de Descargas.
 */
@Service
@Transactional
public class DischargeServiceImpl implements DischargeService {
    
    @Autowired
    private DischargeRepository dischargeRepository;
    
    @Autowired
    private DischargeUserRepository dischargeUserRepository;
    
    @Autowired
    private MunicipalityRepository municipalityRepository;
    
    @Autowired
    private BasinSectionRepository basinSectionRepository;
    
    @Autowired
    private AuthorizationUtils authorizationUtils;
    
    @Override
    public Discharge createDischarge(Discharge discharge) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        // Validar que el usuario de descarga pertenece a la corporación
        if (discharge.getDischargeUser() != null) {
            DischargeUser dischargeUser = dischargeUserRepository.findById(discharge.getDischargeUser().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Discharge user not found"));
            
            // Comparar por ID para evitar problemas con proxies de Hibernate
            if (dischargeUser.getCorporation() == null || !dischargeUser.getCorporation().getId().equals(corporation.getId())) {
                throw new IllegalArgumentException("Discharge user does not belong to your corporation");
            }
        }
        
        // Validar que el municipio existe
        if (discharge.getMunicipality() != null) {
            municipalityRepository.findById(discharge.getMunicipality().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Municipality not found"));
        }
        
        // Validar que la sección de cuenca pertenece a la corporación
        if (discharge.getBasinSection() != null) {
            BasinSection basinSection = basinSectionRepository.findById(discharge.getBasinSection().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Basin section not found"));
            
            // Comparar por ID para evitar problemas con proxies de Hibernate
            if (basinSection.getCorporation() == null || !basinSection.getCorporation().getId().equals(corporation.getId())) {
                throw new IllegalArgumentException("Basin section does not belong to your corporation");
            }
        }
        
        // Verificar que no existe una descarga con el mismo número y año
        if (discharge.getNumber() != null && discharge.getYear() != null) {
            if (dischargeRepository.existsByNumberAndYear(discharge.getNumber(), discharge.getYear())) {
                throw new IllegalArgumentException("Discharge with number " + discharge.getNumber() + 
                        " and year " + discharge.getYear() + " already exists");
            }
        }
        
        // Asignar corporación y usuario creador
        discharge.setCorporation(corporation);
        discharge.setCreatedBy(currentUser);
        discharge.setCreatedAt(LocalDateTime.now());
        
        return dischargeRepository.save(discharge);
    }
    
    @Override
    public Discharge updateDischarge(Discharge discharge) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Discharge existingDischarge = dischargeRepository.findById(discharge.getId())
                .orElseThrow(() -> new IllegalArgumentException("Discharge not found"));
        
        Corporation corporation = currentUser.getCorporation();
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (corporation == null || existingDischarge.getCorporation() == null || !existingDischarge.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalStateException("Access denied: Discharge does not belong to your corporation");
        }
        
        // Validar cambios en número y año
        if (discharge.getNumber() != null && discharge.getYear() != null) {
            if (!discharge.getNumber().equals(existingDischarge.getNumber()) || 
                !discharge.getYear().equals(existingDischarge.getYear())) {
                
                if (dischargeRepository.existsByNumberAndYear(discharge.getNumber(), discharge.getYear())) {
                    throw new IllegalArgumentException("Discharge with number " + discharge.getNumber() + 
                            " and year " + discharge.getYear() + " already exists");
                }
            }
        }
        
        // Actualizar campos
        existingDischarge.setDischargeUser(discharge.getDischargeUser());
        existingDischarge.setBasinSection(discharge.getBasinSection());
        existingDischarge.setMunicipality(discharge.getMunicipality());
        existingDischarge.setDischargeType(discharge.getDischargeType());
        existingDischarge.setNumber(discharge.getNumber());
        existingDischarge.setYear(discharge.getYear());
        existingDischarge.setName(discharge.getName());
        existingDischarge.setDischargePoint(discharge.getDischargePoint());
        existingDischarge.setWaterResourceType(discharge.getWaterResourceType());
        existingDischarge.setLatitude(discharge.getLatitude());
        existingDischarge.setLongitude(discharge.getLongitude());
        existingDischarge.setBasinRehuse(discharge.isBasinRehuse());
        existingDischarge.setCcDboVert(discharge.getCcDboVert());
        existingDischarge.setCcSstVert(discharge.getCcSstVert());
        existingDischarge.setCcDboCap(discharge.getCcDboCap());
        existingDischarge.setCcSstCap(discharge.getCcSstCap());
        existingDischarge.setCcDboTotal(discharge.getCcDboTotal());
        existingDischarge.setCcSstTotal(discharge.getCcSstTotal());
        existingDischarge.setDqo(discharge.getDqo());
        existingDischarge.setSourceMonitored(discharge.isSourceMonitored());
        existingDischarge.setUpdatedBy(currentUser);
        existingDischarge.setUpdatedAt(LocalDateTime.now());
        
        return dischargeRepository.save(existingDischarge);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Discharge> getDischargeById(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        // Buscar directamente por ID y corporationId para evitar problemas con lazy loading
        return dischargeRepository.findByIdAndCorporationId(id, corporation.getId());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Discharge> getMyCorporationDischarges(Pageable pageable) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return dischargeRepository.findByCorporation(corporation, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Discharge> getAllMyCorporationDischarges() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return dischargeRepository.findByCorporation(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Discharge> getDischargesByDischargeUser(Long dischargeUserId) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        DischargeUser dischargeUser = dischargeUserRepository.findById(dischargeUserId)
                .orElseThrow(() -> new IllegalArgumentException("Discharge user not found"));
        
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (dischargeUser.getCorporation() == null || !dischargeUser.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalArgumentException("Discharge user does not belong to your corporation");
        }
        
        return dischargeRepository.findByDischargeUser(dischargeUser);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Discharge> getDischargesByMunicipality(Long municipalityId) {
        Municipality municipality = municipalityRepository.findById(municipalityId)
                .orElseThrow(() -> new IllegalArgumentException("Municipality not found"));
        
        return dischargeRepository.findByMunicipality(municipality);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Discharge> getDischargesByBasinSection(Long basinSectionId) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        BasinSection basinSection = basinSectionRepository.findById(basinSectionId)
                .orElseThrow(() -> new IllegalArgumentException("Basin section not found"));
        
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (basinSection.getCorporation() == null || !basinSection.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalArgumentException("Basin section does not belong to your corporation");
        }
        
        return dischargeRepository.findByBasinSection(basinSection);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Discharge> getDischargesByYear(Integer year) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return dischargeRepository.findByCorporationAndYear(corporation, year);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Discharge> getDischargesByType(Discharge.DischargeType dischargeType) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return dischargeRepository.findByCorporationAndDischargeType(corporation, dischargeType);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Discharge> getDischargesByWaterResourceType(Discharge.WaterResourceType waterResourceType) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return dischargeRepository.findByCorporationAndWaterResourceType(corporation, waterResourceType);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Discharge> getDischargeByNumberAndYear(Integer number, Integer year) {
        return dischargeRepository.findByNumberAndYear(number, year);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Discharge> searchDischargesByName(String name) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return dischargeRepository.findByCorporationAndNameContainingIgnoreCase(corporation, name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Discharge> getMonitoredDischarges() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return dischargeRepository.findByCorporationAndIsSourceMonitoredTrue(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Discharge> getUnmonitoredDischarges() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return dischargeRepository.findByCorporationAndIsSourceMonitoredFalse(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Discharge> getDischargesWithBasinReuse() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return dischargeRepository.findByCorporationAndIsBasinRehuseTrue(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Discharge> getDischargesWithoutBasinReuse() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return dischargeRepository.findByCorporationAndIsBasinRehuseFalse(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Discharge> getDischargesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return dischargeRepository.findByCorporationAndCreatedAtBetween(corporation, startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countMyCorporationDischarges() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return dischargeRepository.countByCorporationId(corporation.getId());
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countDischargesByMunicipality(Long municipalityId) {
        return dischargeRepository.countByMunicipalityId(municipalityId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countDischargesByBasinSection(Long basinSectionId) {
        return dischargeRepository.countByBasinSectionId(basinSectionId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countDischargesByYear(Integer year) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return dischargeRepository.countByCorporationAndYear(corporation.getId(), year);
    }
    
    @Override
    public boolean deleteDischarge(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Discharge discharge = dischargeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Discharge not found"));
        
        Corporation corporation = currentUser.getCorporation();
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (corporation == null || discharge.getCorporation() == null || !discharge.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalStateException("Access denied: Discharge does not belong to your corporation");
        }
        
        dischargeRepository.delete(discharge);
        return true;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByNumberAndYear(Integer number, Integer year) {
        return dischargeRepository.existsByNumberAndYear(number, year);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Discharge> getDischargesOrderByCreatedAtDesc() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return dischargeRepository.findByCorporationOrderByCreatedAtDesc(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Discharge> getDischargesByMunicipalityAndYear(Long municipalityId, Integer year) {
        Municipality municipality = municipalityRepository.findById(municipalityId)
                .orElseThrow(() -> new IllegalArgumentException("Municipality not found"));
        
        return dischargeRepository.findByMunicipalityAndYear(municipality, year);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Discharge> getDischargesByBasinSectionAndYear(Long basinSectionId, Integer year) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        BasinSection basinSection = basinSectionRepository.findById(basinSectionId)
                .orElseThrow(() -> new IllegalArgumentException("Basin section not found"));
        
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (basinSection.getCorporation() == null || !basinSection.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalArgumentException("Basin section does not belong to your corporation");
        }
        
        return dischargeRepository.findByBasinSectionAndYear(basinSection, year);
    }
    
    @Override
    @Transactional(readOnly = true)
    public DischargeStats getMyCorporationDischargeStats() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        DischargeStats stats = new DischargeStats();
        
        List<Discharge> allDischarges = dischargeRepository.findByCorporation(corporation);
        
        stats.setTotalDischarges(allDischarges.size());
        stats.setMonitoredDischarges((int) allDischarges.stream().filter(Discharge::isSourceMonitored).count());
        stats.setUnmonitoredDischarges((int) allDischarges.stream().filter(d -> !d.isSourceMonitored()).count());
        stats.setDischargesWithBasinReuse((int) allDischarges.stream().filter(Discharge::isBasinRehuse).count());
        stats.setDischargesWithoutBasinReuse((int) allDischarges.stream().filter(d -> !d.isBasinRehuse()).count());
        stats.setDomesticDischarges((int) allDischarges.stream().filter(d -> d.getDischargeType() == Discharge.DischargeType.ARD).count());
        stats.setNonDomesticDischarges((int) allDischarges.stream().filter(d -> d.getDischargeType() == Discharge.DischargeType.ARND).count());
        stats.setRiverDischarges((int) allDischarges.stream().filter(d -> d.getWaterResourceType() == Discharge.WaterResourceType.RIVER).count());
        stats.setLakeDischarges((int) allDischarges.stream().filter(d -> d.getWaterResourceType() == Discharge.WaterResourceType.LAKE).count());
        
        return stats;
    }
}

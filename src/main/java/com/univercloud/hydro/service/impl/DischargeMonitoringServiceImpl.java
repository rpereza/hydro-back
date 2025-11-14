package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.DischargeMonitoring;
import com.univercloud.hydro.entity.User;
import com.univercloud.hydro.repository.DischargeMonitoringRepository;
import com.univercloud.hydro.service.DischargeMonitoringService;
import com.univercloud.hydro.util.AuthorizationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para la gestión de Monitoreos de Descarga.
 */
@Service
@Transactional
public class DischargeMonitoringServiceImpl implements DischargeMonitoringService {
    
    @Autowired
    private DischargeMonitoringRepository dischargeMonitoringRepository;
    
    @Autowired
    private AuthorizationUtils authorizationUtils;
    
    @Override
    public DischargeMonitoring createDischargeMonitoring(DischargeMonitoring dischargeMonitoring) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        // Asignar corporación y usuario creador
        dischargeMonitoring.setCorporation(corporation);
        dischargeMonitoring.setCreatedBy(currentUser);
        dischargeMonitoring.setCreatedAt(LocalDateTime.now());
        
        return dischargeMonitoringRepository.save(dischargeMonitoring);
    }
    
    @Override
    public DischargeMonitoring updateDischargeMonitoring(DischargeMonitoring dischargeMonitoring) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        DischargeMonitoring existingDischargeMonitoring = dischargeMonitoringRepository.findById(dischargeMonitoring.getId())
                .orElseThrow(() -> new IllegalArgumentException("Discharge monitoring not found"));
        
        Corporation corporation = currentUser.getCorporation();
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (corporation == null || existingDischargeMonitoring.getCorporation() == null || !existingDischargeMonitoring.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalStateException("Access denied: Discharge monitoring does not belong to your corporation");
        }
        
        // Actualizar campos
        existingDischargeMonitoring.setDischarge(dischargeMonitoring.getDischarge());
        existingDischargeMonitoring.setMonitoringStation(dischargeMonitoring.getMonitoringStation());
        existingDischargeMonitoring.setOd(dischargeMonitoring.getOd());
        existingDischargeMonitoring.setSst(dischargeMonitoring.getSst());
        existingDischargeMonitoring.setDqo(dischargeMonitoring.getDqo());
        existingDischargeMonitoring.setCe(dischargeMonitoring.getCe());
        existingDischargeMonitoring.setPh(dischargeMonitoring.getPh());
        existingDischargeMonitoring.setN(dischargeMonitoring.getN());
        existingDischargeMonitoring.setP(dischargeMonitoring.getP());
        existingDischargeMonitoring.setRnp(dischargeMonitoring.getRnp());
        existingDischargeMonitoring.setIod(dischargeMonitoring.getIod());
        existingDischargeMonitoring.setIsst(dischargeMonitoring.getIsst());
        existingDischargeMonitoring.setIdqo(dischargeMonitoring.getIdqo());
        existingDischargeMonitoring.setIce(dischargeMonitoring.getIce());
        existingDischargeMonitoring.setIph(dischargeMonitoring.getIph());
        existingDischargeMonitoring.setIrnp(dischargeMonitoring.getIrnp());
        existingDischargeMonitoring.setCaudalVolumen(dischargeMonitoring.getCaudalVolumen());
        existingDischargeMonitoring.setLatitude(dischargeMonitoring.getLatitude());
        existingDischargeMonitoring.setLongitude(dischargeMonitoring.getLongitude());
        existingDischargeMonitoring.setUpdatedBy(currentUser);
        existingDischargeMonitoring.setUpdatedAt(LocalDateTime.now());
        
        return dischargeMonitoringRepository.save(existingDischargeMonitoring);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<DischargeMonitoring> getDischargeMonitoringById(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        // Buscar directamente por ID y corporationId para evitar problemas con lazy loading
        return dischargeMonitoringRepository.findByIdAndCorporationId(id, corporation.getId());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<DischargeMonitoring> getMyCorporationDischargeMonitorings(Pageable pageable) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return dischargeMonitoringRepository.findByCorporation(corporation, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeMonitoring> getAllMyCorporationDischargeMonitorings() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return dischargeMonitoringRepository.findByCorporation(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeMonitoring> getDischargeMonitoringsByDischarge(Long dischargeId) {
        return dischargeMonitoringRepository.findByDischargeId(dischargeId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeMonitoring> getDischargeMonitoringsByMonitoringStation(Long monitoringStationId) {
        return dischargeMonitoringRepository.findByMonitoringStationId(monitoringStationId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeMonitoring> getDischargeMonitoringsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return dischargeMonitoringRepository.findByCreatedAtBetween(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeMonitoring> getDischargeMonitoringsByOdRange(BigDecimal minOd, BigDecimal maxOd) {
        return dischargeMonitoringRepository.findByOdBetween(minOd, maxOd);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeMonitoring> getDischargeMonitoringsBySstRange(BigDecimal minSst, BigDecimal maxSst) {
        return dischargeMonitoringRepository.findBySstBetween(minSst, maxSst);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeMonitoring> getDischargeMonitoringsByDqoRange(BigDecimal minDqo, BigDecimal maxDqo) {
        return dischargeMonitoringRepository.findByDqoBetween(minDqo, maxDqo);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeMonitoring> getDischargeMonitoringsByPhRange(BigDecimal minPh, BigDecimal maxPh) {
        return dischargeMonitoringRepository.findByPhBetween(minPh, maxPh);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countMyCorporationDischargeMonitorings() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return dischargeMonitoringRepository.countByCorporation(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countDischargeMonitoringsByDischarge(Long dischargeId) {
        return dischargeMonitoringRepository.countByDischargeId(dischargeId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countDischargeMonitoringsByMonitoringStation(Long monitoringStationId) {
        return dischargeMonitoringRepository.countByMonitoringStationId(monitoringStationId);
    }
    
    @Override
    public boolean deleteDischargeMonitoring(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        DischargeMonitoring dischargeMonitoring = dischargeMonitoringRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Discharge monitoring not found"));
        
        Corporation corporation = currentUser.getCorporation();
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (corporation == null || dischargeMonitoring.getCorporation() == null || !dischargeMonitoring.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalStateException("Access denied: Discharge monitoring does not belong to your corporation");
        }
        
        dischargeMonitoringRepository.delete(dischargeMonitoring);
        return true;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeMonitoring> getDischargeMonitoringsOrderByCreatedAtDesc() {
        return dischargeMonitoringRepository.findAllOrderByCreatedAtDesc();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeMonitoring> getMyCorporationDischargeMonitoringsOrderByCreatedAtDesc() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return dischargeMonitoringRepository.findByCorporationOrderByCreatedAtDesc(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public DischargeMonitoringStats getMyCorporationDischargeMonitoringStats() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        DischargeMonitoringStats stats = new DischargeMonitoringStats();
        List<DischargeMonitoring> monitorings = dischargeMonitoringRepository.findByCorporation(corporation);
        
        stats.setTotalDischargeMonitorings(monitorings.size());
        
        // Contar descargas únicas
        long uniqueDischarges = monitorings.stream()
                .map(monitoring -> monitoring.getDischarge().getId())
                .distinct()
                .count();
        stats.setTotalDischarges(uniqueDischarges);
        
        // Contar estaciones de monitoreo únicas
        long uniqueStations = monitorings.stream()
                .filter(monitoring -> monitoring.getMonitoringStation() != null)
                .map(monitoring -> monitoring.getMonitoringStation().getId())
                .distinct()
                .count();
        stats.setTotalMonitoringStations(uniqueStations);
        
        // Calcular promedios
        if (!monitorings.isEmpty()) {
            BigDecimal totalOd = monitorings.stream()
                    .map(DischargeMonitoring::getOd)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.setAverageOd(totalOd.divide(BigDecimal.valueOf(monitorings.size()), 3, BigDecimal.ROUND_HALF_UP));
            
            BigDecimal totalSst = monitorings.stream()
                    .map(DischargeMonitoring::getSst)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.setAverageSst(totalSst.divide(BigDecimal.valueOf(monitorings.size()), 3, BigDecimal.ROUND_HALF_UP));
            
            BigDecimal totalDqo = monitorings.stream()
                    .map(DischargeMonitoring::getDqo)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.setAverageDqo(totalDqo.divide(BigDecimal.valueOf(monitorings.size()), 3, BigDecimal.ROUND_HALF_UP));
            
            BigDecimal totalPh = monitorings.stream()
                    .map(DischargeMonitoring::getPh)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.setAveragePh(totalPh.divide(BigDecimal.valueOf(monitorings.size()), 3, BigDecimal.ROUND_HALF_UP));
            
            BigDecimal totalCaudalVolumen = monitorings.stream()
                    .map(DischargeMonitoring::getCaudalVolumen)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.setAverageCaudalVolumen(totalCaudalVolumen.divide(BigDecimal.valueOf(monitorings.size()), 2, BigDecimal.ROUND_HALF_UP));
        }
        
        return stats;
    }
}

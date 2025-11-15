package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.DischargeParameter;
import com.univercloud.hydro.entity.User;
import com.univercloud.hydro.exception.ResourceNotFoundException;
import com.univercloud.hydro.repository.DischargeParameterRepository;
import com.univercloud.hydro.service.DischargeParameterService;
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
 * Implementación del servicio para la gestión de Parámetros de Descarga.
 */
@Service
@Transactional
public class DischargeParameterServiceImpl implements DischargeParameterService {
    
    @Autowired
    private DischargeParameterRepository dischargeParameterRepository;
    
    @Autowired
    private AuthorizationUtils authorizationUtils;
    
    @Override
    public DischargeParameter createDischargeParameter(DischargeParameter dischargeParameter) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        // Asignar corporación y usuario creador
        dischargeParameter.setCorporation(corporation);
        dischargeParameter.setCreatedBy(currentUser);
        dischargeParameter.setCreatedAt(LocalDateTime.now());
        
        return dischargeParameterRepository.save(dischargeParameter);
    }
    
    @Override
    public DischargeParameter updateDischargeParameter(DischargeParameter dischargeParameter) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        DischargeParameter existingDischargeParameter = dischargeParameterRepository.findById(dischargeParameter.getId())
                .orElseThrow(() -> new ResourceNotFoundException("DischargeParameter", "id", dischargeParameter.getId()));
        
        Corporation corporation = currentUser.getCorporation();
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (corporation == null || existingDischargeParameter.getCorporation() == null || !existingDischargeParameter.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalStateException("Access denied: Discharge parameter does not belong to your corporation");
        }
        
        // Actualizar campos
        existingDischargeParameter.setDischarge(dischargeParameter.getDischarge());
        existingDischargeParameter.setMonth(dischargeParameter.getMonth());
        existingDischargeParameter.setOrigin(dischargeParameter.getOrigin());
        existingDischargeParameter.setCaudalVolumen(dischargeParameter.getCaudalVolumen());
        existingDischargeParameter.setFrequency(dischargeParameter.getFrequency());
        existingDischargeParameter.setDuration(dischargeParameter.getDuration());
        existingDischargeParameter.setConcDbo(dischargeParameter.getConcDbo());
        existingDischargeParameter.setConcSst(dischargeParameter.getConcSst());
        existingDischargeParameter.setCcDbo(dischargeParameter.getCcDbo());
        existingDischargeParameter.setCcSst(dischargeParameter.getCcSst());
        existingDischargeParameter.setUpdatedBy(currentUser);
        existingDischargeParameter.setUpdatedAt(LocalDateTime.now());
        
        return dischargeParameterRepository.save(existingDischargeParameter);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<DischargeParameter> getDischargeParameterById(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        // Buscar directamente por ID y corporationId para evitar problemas con lazy loading
        return dischargeParameterRepository.findByIdAndCorporationId(id, corporation.getId());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<DischargeParameter> getMyCorporationDischargeParameters(Pageable pageable) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return dischargeParameterRepository.findByCorporation(corporation, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeParameter> getAllMyCorporationDischargeParameters() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return dischargeParameterRepository.findByCorporation(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeParameter> getDischargeParametersByDischarge(Long dischargeId) {
        return dischargeParameterRepository.findByDischargeId(dischargeId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeParameter> getDischargeParametersByMonth(DischargeParameter.Month month) {
        return dischargeParameterRepository.findByMonth(month);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeParameter> getDischargeParametersByOrigin(DischargeParameter.Origin origin) {
        return dischargeParameterRepository.findByOrigin(origin);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeParameter> getDischargeParametersByDischargeAndMonth(Long dischargeId, DischargeParameter.Month month) {
        return dischargeParameterRepository.findByDischargeIdAndMonth(dischargeId, month);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeParameter> getDischargeParametersByDischargeAndOrigin(Long dischargeId, DischargeParameter.Origin origin) {
        return dischargeParameterRepository.findByDischargeIdAndOrigin(dischargeId, origin);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeParameter> getDischargeParametersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return dischargeParameterRepository.findByCreatedAtBetween(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeParameter> getDischargeParametersByCaudalVolumenRange(BigDecimal minCaudalVolumen, BigDecimal maxCaudalVolumen) {
        return dischargeParameterRepository.findByCaudalVolumenBetween(minCaudalVolumen, maxCaudalVolumen);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeParameter> getDischargeParametersByFrequencyRange(Short minFrequency, Short maxFrequency) {
        return dischargeParameterRepository.findByFrequencyBetween(minFrequency, maxFrequency);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeParameter> getDischargeParametersByDurationRange(Short minDuration, Short maxDuration) {
        return dischargeParameterRepository.findByDurationBetween(minDuration, maxDuration);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countMyCorporationDischargeParameters() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return dischargeParameterRepository.countByCorporation(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countDischargeParametersByDischarge(Long dischargeId) {
        return dischargeParameterRepository.countByDischargeId(dischargeId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countDischargeParametersByMonth(DischargeParameter.Month month) {
        return dischargeParameterRepository.countByMonth(month);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countDischargeParametersByOrigin(DischargeParameter.Origin origin) {
        return dischargeParameterRepository.countByOrigin(origin);
    }
    
    @Override
    public boolean deleteDischargeParameter(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        DischargeParameter dischargeParameter = dischargeParameterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DischargeParameter", "id", id));
        
        Corporation corporation = currentUser.getCorporation();
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (corporation == null || dischargeParameter.getCorporation() == null || !dischargeParameter.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalStateException("Access denied: Discharge parameter does not belong to your corporation");
        }
        
        dischargeParameterRepository.delete(dischargeParameter);
        return true;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeParameter> getDischargeParametersOrderByCreatedAtDesc() {
        return dischargeParameterRepository.findAllOrderByCreatedAtDesc();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeParameter> getMyCorporationDischargeParametersOrderByCreatedAtDesc() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return dischargeParameterRepository.findByCorporationOrderByCreatedAtDesc(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public DischargeParameterStats getMyCorporationDischargeParameterStats() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        DischargeParameterStats stats = new DischargeParameterStats();
        List<DischargeParameter> parameters = dischargeParameterRepository.findByCorporation(corporation);
        
        stats.setTotalDischargeParameters(parameters.size());
        
        // Contar descargas únicas
        long uniqueDischarges = parameters.stream()
                .map(parameter -> parameter.getDischarge().getId())
                .distinct()
                .count();
        stats.setTotalDischarges(uniqueDischarges);
        
        // Contar meses únicos
        long uniqueMonths = parameters.stream()
                .map(DischargeParameter::getMonth)
                .distinct()
                .count();
        stats.setTotalMonths(uniqueMonths);
        
        // Contar orígenes únicos
        long uniqueOrigins = parameters.stream()
                .map(DischargeParameter::getOrigin)
                .distinct()
                .count();
        stats.setTotalOrigins(uniqueOrigins);
        
        // Calcular promedios
        if (!parameters.isEmpty()) {
            BigDecimal totalCaudalVolumen = parameters.stream()
                    .map(DischargeParameter::getCaudalVolumen)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.setAverageCaudalVolumen(totalCaudalVolumen.divide(BigDecimal.valueOf(parameters.size()), 2, BigDecimal.ROUND_HALF_UP));
            
            BigDecimal totalFrequency = parameters.stream()
                    .map(parameter -> BigDecimal.valueOf(parameter.getFrequency()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.setAverageFrequency(totalFrequency.divide(BigDecimal.valueOf(parameters.size()), 2, BigDecimal.ROUND_HALF_UP));
            
            BigDecimal totalDuration = parameters.stream()
                    .map(parameter -> BigDecimal.valueOf(parameter.getDuration()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.setAverageDuration(totalDuration.divide(BigDecimal.valueOf(parameters.size()), 2, BigDecimal.ROUND_HALF_UP));
            
            BigDecimal totalConcDbo = parameters.stream()
                    .map(DischargeParameter::getConcDbo)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.setAverageConcDbo(totalConcDbo.divide(BigDecimal.valueOf(parameters.size()), 2, BigDecimal.ROUND_HALF_UP));
            
            BigDecimal totalConcSst = parameters.stream()
                    .map(DischargeParameter::getConcSst)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.setAverageConcSst(totalConcSst.divide(BigDecimal.valueOf(parameters.size()), 2, BigDecimal.ROUND_HALF_UP));
            
            BigDecimal totalCcDbo = parameters.stream()
                    .map(DischargeParameter::getCcDbo)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.setAverageCcDbo(totalCcDbo.divide(BigDecimal.valueOf(parameters.size()), 2, BigDecimal.ROUND_HALF_UP));
            
            BigDecimal totalCcSst = parameters.stream()
                    .map(DischargeParameter::getCcSst)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.setAverageCcSst(totalCcSst.divide(BigDecimal.valueOf(parameters.size()), 2, BigDecimal.ROUND_HALF_UP));
        }
        
        return stats;
    }
}

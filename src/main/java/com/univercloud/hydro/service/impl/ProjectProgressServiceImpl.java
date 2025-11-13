package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.ProjectProgress;
import com.univercloud.hydro.entity.User;
import com.univercloud.hydro.repository.ProjectProgressRepository;
import com.univercloud.hydro.service.ProjectProgressService;
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
 * Implementación del servicio para la gestión de Progreso de Proyectos.
 */
@Service
@Transactional
public class ProjectProgressServiceImpl implements ProjectProgressService {
    
    @Autowired
    private ProjectProgressRepository projectProgressRepository;
    
    @Autowired
    private AuthorizationUtils authorizationUtils;
    
    @Override
    public ProjectProgress createProjectProgress(ProjectProgress projectProgress) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        // Verificar que no existe un progreso de proyecto para el mismo usuario de descarga y año en la corporación
        if (projectProgress.getDischargeUser() != null && projectProgress.getYear() != null && 
            projectProgressRepository.existsByCorporationAndDischargeUserAndYear(corporation, projectProgress.getDischargeUser(), projectProgress.getYear())) {
            throw new IllegalArgumentException("Project progress for discharge user " + projectProgress.getDischargeUser().getId() + 
                                               " and year " + projectProgress.getYear() + " already exists in your corporation");
        }
        
        // Asignar corporación y usuario creador
        projectProgress.setCorporation(corporation);
        projectProgress.setCreatedBy(currentUser);
        projectProgress.setCreatedAt(LocalDateTime.now());
        
        return projectProgressRepository.save(projectProgress);
    }
    
    @Override
    public ProjectProgress updateProjectProgress(ProjectProgress projectProgress) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        ProjectProgress existingProjectProgress = projectProgressRepository.findById(projectProgress.getId())
                .orElseThrow(() -> new IllegalArgumentException("Project progress not found"));
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null || !corporation.equals(existingProjectProgress.getCorporation())) {
            throw new IllegalStateException("Access denied: Project progress does not belong to your corporation");
        }
        
        // Verificar cambios en usuario de descarga y año
        if ((projectProgress.getDischargeUser() != null && !projectProgress.getDischargeUser().equals(existingProjectProgress.getDischargeUser())) ||
            (projectProgress.getYear() != null && !projectProgress.getYear().equals(existingProjectProgress.getYear()))) {
            if (projectProgressRepository.existsByCorporationAndDischargeUserAndYear(corporation, projectProgress.getDischargeUser(), projectProgress.getYear())) {
                throw new IllegalArgumentException("Project progress for discharge user " + projectProgress.getDischargeUser().getId() + 
                                                   " and year " + projectProgress.getYear() + " already exists in your corporation");
            }
        }
        
        // Actualizar campos
        existingProjectProgress.setDischargeUser(projectProgress.getDischargeUser());
        existingProjectProgress.setYear(projectProgress.getYear());
        existingProjectProgress.setCciPercentage(projectProgress.getCciPercentage());
        existingProjectProgress.setCevPercentage(projectProgress.getCevPercentage());
        existingProjectProgress.setCdsPercentage(projectProgress.getCdsPercentage());
        existingProjectProgress.setCcsPercentage(projectProgress.getCcsPercentage());
        existingProjectProgress.setUpdatedBy(currentUser);
        existingProjectProgress.setUpdatedAt(LocalDateTime.now());
        
        return projectProgressRepository.save(existingProjectProgress);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<ProjectProgress> getProjectProgressById(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        // Buscar directamente por ID y corporationId para evitar problemas con lazy loading
        return projectProgressRepository.findByIdAndCorporationId(id, corporation.getId());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProjectProgress> getMyCorporationProjectProgresses(Pageable pageable) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return projectProgressRepository.findByCorporation(corporation, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProjectProgress> getAllMyCorporationProjectProgresses() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return projectProgressRepository.findByCorporation(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProjectProgress> getProjectProgressesByDischargeUser(Long dischargeUserId) {
        return projectProgressRepository.findByDischargeUserId(dischargeUserId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProjectProgress> getProjectProgressesByYear(Integer year) {
        return projectProgressRepository.findByYear(year);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProjectProgress> getProjectProgressesByDischargeUserAndYear(Long dischargeUserId, Integer year) {
        return projectProgressRepository.findByDischargeUserIdAndYear(dischargeUserId, year);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProjectProgress> getProjectProgressesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return projectProgressRepository.findByCreatedAtBetween(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProjectProgress> getProjectProgressesByCciPercentageRange(BigDecimal minCciPercentage, BigDecimal maxCciPercentage) {
        return projectProgressRepository.findByCciPercentageBetween(minCciPercentage, maxCciPercentage);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProjectProgress> getProjectProgressesByCevPercentageRange(BigDecimal minCevPercentage, BigDecimal maxCevPercentage) {
        return projectProgressRepository.findByCevPercentageBetween(minCevPercentage, maxCevPercentage);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProjectProgress> getProjectProgressesByCdsPercentageRange(BigDecimal minCdsPercentage, BigDecimal maxCdsPercentage) {
        return projectProgressRepository.findByCdsPercentageBetween(minCdsPercentage, maxCdsPercentage);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProjectProgress> getProjectProgressesByCcsPercentageRange(BigDecimal minCcsPercentage, BigDecimal maxCcsPercentage) {
        return projectProgressRepository.findByCcsPercentageBetween(minCcsPercentage, maxCcsPercentage);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countMyCorporationProjectProgresses() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return projectProgressRepository.countByCorporation(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countProjectProgressesByDischargeUser(Long dischargeUserId) {
        return projectProgressRepository.countByDischargeUserId(dischargeUserId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countProjectProgressesByYear(Integer year) {
        return projectProgressRepository.countByYear(year);
    }
    
    @Override
    public boolean deleteProjectProgress(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        ProjectProgress projectProgress = projectProgressRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project progress not found"));
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null || !corporation.equals(projectProgress.getCorporation())) {
            throw new IllegalStateException("Access denied: Project progress does not belong to your corporation");
        }
        
        projectProgressRepository.delete(projectProgress);
        return true;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByDischargeUserAndYear(Long dischargeUserId, Integer year) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return projectProgressRepository.existsByCorporationAndDischargeUserAndYear(corporation, dischargeUserId, year);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProjectProgress> getProjectProgressesOrderByCreatedAtDesc() {
        return projectProgressRepository.findAllOrderByCreatedAtDesc();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProjectProgress> getProjectProgressesOrderByYearDesc() {
        return projectProgressRepository.findAllOrderByYearDesc();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProjectProgress> getMyCorporationProjectProgressesOrderByYearDesc() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return projectProgressRepository.findByCorporationOrderByYearDesc(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProjectProgressStats getMyCorporationProjectProgressStats() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        ProjectProgressStats stats = new ProjectProgressStats();
        List<ProjectProgress> progresses = projectProgressRepository.findByCorporation(corporation);
        
        stats.setTotalProjectProgresses(progresses.size());
        
        // Contar usuarios de descarga únicos
        long uniqueDischargeUsers = progresses.stream()
                .map(progress -> progress.getDischargeUser().getId())
                .distinct()
                .count();
        stats.setTotalDischargeUsers(uniqueDischargeUsers);
        
        // Contar años únicos
        long uniqueYears = progresses.stream()
                .map(ProjectProgress::getYear)
                .distinct()
                .count();
        stats.setTotalYears(uniqueYears);
        
        // Calcular promedios y valores mínimos/máximos
        if (!progresses.isEmpty()) {
            BigDecimal totalCciPercentage = progresses.stream()
                    .map(ProjectProgress::getCciPercentage)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.setAverageCciPercentage(totalCciPercentage.divide(BigDecimal.valueOf(progresses.size()), 2, BigDecimal.ROUND_HALF_UP));
            
            BigDecimal totalCevPercentage = progresses.stream()
                    .map(ProjectProgress::getCevPercentage)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.setAverageCevPercentage(totalCevPercentage.divide(BigDecimal.valueOf(progresses.size()), 2, BigDecimal.ROUND_HALF_UP));
            
            BigDecimal totalCdsPercentage = progresses.stream()
                    .map(ProjectProgress::getCdsPercentage)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.setAverageCdsPercentage(totalCdsPercentage.divide(BigDecimal.valueOf(progresses.size()), 2, BigDecimal.ROUND_HALF_UP));
            
            BigDecimal totalCcsPercentage = progresses.stream()
                    .map(ProjectProgress::getCcsPercentage)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.setAverageCcsPercentage(totalCcsPercentage.divide(BigDecimal.valueOf(progresses.size()), 2, BigDecimal.ROUND_HALF_UP));
            
            // Valores mínimos y máximos
            stats.setMinCciPercentage(progresses.stream().map(ProjectProgress::getCciPercentage).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
            stats.setMaxCciPercentage(progresses.stream().map(ProjectProgress::getCciPercentage).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
            
            stats.setMinCevPercentage(progresses.stream().map(ProjectProgress::getCevPercentage).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
            stats.setMaxCevPercentage(progresses.stream().map(ProjectProgress::getCevPercentage).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
            
            stats.setMinCdsPercentage(progresses.stream().map(ProjectProgress::getCdsPercentage).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
            stats.setMaxCdsPercentage(progresses.stream().map(ProjectProgress::getCdsPercentage).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
            
            stats.setMinCcsPercentage(progresses.stream().map(ProjectProgress::getCcsPercentage).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
            stats.setMaxCcsPercentage(progresses.stream().map(ProjectProgress::getCcsPercentage).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
        }
        
        return stats;
    }
}

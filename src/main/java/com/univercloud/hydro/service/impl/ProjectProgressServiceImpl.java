package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.ProjectProgress;
import com.univercloud.hydro.entity.User;
import com.univercloud.hydro.exception.DuplicateResourceException;
import com.univercloud.hydro.exception.ResourceNotFoundException;
import com.univercloud.hydro.repository.ProjectProgressRepository;
import com.univercloud.hydro.service.ProjectProgressService;
import com.univercloud.hydro.util.AuthorizationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
            throw new DuplicateResourceException("Project progress for discharge user " + projectProgress.getDischargeUser().getId() + 
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
                .orElseThrow(() -> new ResourceNotFoundException("ProjectProgress", "id", projectProgress.getId()));
        
        Corporation corporation = currentUser.getCorporation();
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (corporation == null || existingProjectProgress.getCorporation() == null || !existingProjectProgress.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalStateException("Access denied: Project progress does not belong to your corporation");
        }
        
        // Verificar cambios en usuario de descarga y año
        if ((projectProgress.getDischargeUser() != null && !projectProgress.getDischargeUser().getId().equals(existingProjectProgress.getDischargeUser().getId())) ||
            (projectProgress.getYear() != null && !projectProgress.getYear().equals(existingProjectProgress.getYear()))) {
            if (projectProgressRepository.existsByCorporationAndDischargeUserAndYear(corporation, projectProgress.getDischargeUser(), projectProgress.getYear())) {
                throw new DuplicateResourceException("Project progress for discharge user " + projectProgress.getDischargeUser().getId() + 
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
    public boolean deleteProjectProgress(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        ProjectProgress projectProgress = projectProgressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProjectProgress", "id", id));
        
        Corporation corporation = currentUser.getCorporation();
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (corporation == null || projectProgress.getCorporation() == null || !projectProgress.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalStateException("Access denied: Project progress does not belong to your corporation");
        }
        
        projectProgressRepository.delete(projectProgress);
        return true;
    }
}

package com.univercloud.hydro.service;

import com.univercloud.hydro.entity.ProjectProgress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Servicio para la gestión de Progreso de Proyectos.
 * Proporciona operaciones CRUD y lógica de negocio para progreso de proyectos.
 */
public interface ProjectProgressService {
    
    /**
     * Crea un nuevo progreso de proyecto.
     * @param projectProgress el progreso de proyecto a crear
     * @return el progreso de proyecto creado
     * @throws IllegalArgumentException si los datos son inválidos
     */
    ProjectProgress createProjectProgress(ProjectProgress projectProgress);
    
    /**
     * Actualiza un progreso de proyecto existente.
     * @param projectProgress el progreso de proyecto a actualizar
     * @return el progreso de proyecto actualizado
     * @throws IllegalArgumentException si el progreso de proyecto no existe
     */
    ProjectProgress updateProjectProgress(ProjectProgress projectProgress);
    
    /**
     * Obtiene un progreso de proyecto por su ID.
     * @param id el ID del progreso de proyecto
     * @return el progreso de proyecto, si existe
     */
    Optional<ProjectProgress> getProjectProgressById(Long id);
    
    /**
     * Obtiene todos los progresos de proyecto de la corporación del usuario autenticado.
     * @param pageable parámetros de paginación
     * @return página de progresos de proyecto
     */
    Page<ProjectProgress> getMyCorporationProjectProgresses(Pageable pageable);
    
    /**
     * Elimina un progreso de proyecto.
     * @param id el ID del progreso de proyecto a eliminar
     * @return true si se eliminó correctamente
     * @throws IllegalArgumentException si el progreso de proyecto no existe
     */
    boolean deleteProjectProgress(Long id);
}

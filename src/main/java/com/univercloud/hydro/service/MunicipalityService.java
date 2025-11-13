package com.univercloud.hydro.service;

import com.univercloud.hydro.entity.Municipality;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de Municipios.
 * Proporciona operaciones CRUD y lógica de negocio para municipios.
 */
public interface MunicipalityService {
    
    /**
     * Crea un nuevo municipio.
     * @param municipality el municipio a crear
     * @return el municipio creado
     * @throws IllegalArgumentException si los datos son inválidos
     */
    Municipality createMunicipality(Municipality municipality);
    
    /**
     * Actualiza un municipio existente.
     * @param municipality el municipio a actualizar
     * @return el municipio actualizado
     * @throws IllegalArgumentException si el municipio no existe
     */
    Municipality updateMunicipality(Municipality municipality);
    
    /**
     * Obtiene un municipio por su ID.
     * @param id el ID del municipio
     * @return el municipio, si existe
     */
    Optional<Municipality> getMunicipalityById(Long id);
    
    /**
     * Obtiene todos los municipios.
     * @param pageable parámetros de paginación
     * @return página de municipios
     */
    Page<Municipality> getAllMunicipalities(Pageable pageable);
    
    /**
     * Obtiene todos los municipios.
     * @return lista de municipios
     */
    List<Municipality> getAllMunicipalities();
    
    /**
     * Busca municipios por nombre (búsqueda parcial).
     * @param name el nombre o parte del nombre a buscar
     * @return lista de municipios que coinciden
     */
    List<Municipality> searchMunicipalitiesByName(String name);
    
    /**
     * Busca un municipio por nombre.
     * @param name el nombre del municipio
     * @return el municipio si existe
     */
    Optional<Municipality> getMunicipalityByName(String name);
    
    /**
     * Verifica si existe un municipio con el código especificado.
     * @param code el código del municipio
     * @return true si existe, false en caso contrario
     */
    boolean existsByCode(String code);
    
    /**
     * Obtiene todos los municipios de un departamento.
     * @param departmentId el ID del departamento
     * @return lista de municipios del departamento
     */
    List<Municipality> getMunicipalitiesByDepartmentId(Long departmentId);
}

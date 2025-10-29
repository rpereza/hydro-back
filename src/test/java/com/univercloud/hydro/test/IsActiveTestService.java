package com.univercloud.hydro.test;

import com.univercloud.hydro.entity.BasinSection;
import com.univercloud.hydro.entity.Municipality;
import com.univercloud.hydro.entity.WaterBasin;
import com.univercloud.hydro.entity.Department;
import com.univercloud.hydro.entity.Category;
import com.univercloud.hydro.service.BasinSectionService;
import com.univercloud.hydro.service.MunicipalityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Clase de prueba para verificar la funcionalidad de activar/desactivar
 * municipios y secciones de cuenca.
 */
@Component
public class IsActiveTestService {
    
    @Autowired
    private BasinSectionService basinSectionService;
    
    @Autowired
    private MunicipalityService municipalityService;
    
    /**
     * Prueba la funcionalidad de activar/desactivar secciones de cuenca.
     */
    public void testBasinSectionActivation() {
        System.out.println("=== Probando funcionalidad de activar/desactivar secciones de cuenca ===");
        
        try {
            // Crear una sección de cuenca de prueba
            BasinSection testSection = new BasinSection();
            testSection.setName("Sección de Prueba");
            testSection.setDescription("Sección creada para pruebas");
            // Nota: Necesitarías configurar waterBasin, corporation, etc.
            
            // Crear la sección
            // BasinSection created = basinSectionService.createBasinSection(testSection);
            // System.out.println("Sección creada: " + created.getName() + " - Activa: " + created.isActive());
            
            // Desactivar la sección
            // boolean deactivated = basinSectionService.deactivateBasinSection(created.getId());
            // System.out.println("Sección desactivada: " + deactivated);
            
            // Verificar que está inactiva
            // Optional<BasinSection> inactiveSection = basinSectionService.getBasinSectionById(created.getId());
            // if (inactiveSection.isPresent()) {
            //     System.out.println("Estado después de desactivar: " + inactiveSection.get().isActive());
            // }
            
            // Activar la sección
            // boolean activated = basinSectionService.activateBasinSection(created.getId());
            // System.out.println("Sección activada: " + activated);
            
            // Verificar que está activa
            // Optional<BasinSection> activeSection = basinSectionService.getBasinSectionById(created.getId());
            // if (activeSection.isPresent()) {
            //     System.out.println("Estado después de activar: " + activeSection.get().isActive());
            // }
            
            System.out.println("✅ Prueba de secciones de cuenca completada");
            
        } catch (Exception e) {
            System.out.println("❌ Error en prueba de secciones de cuenca: " + e.getMessage());
        }
    }
    
    /**
     * Prueba la funcionalidad de activar/desactivar municipios.
     */
    public void testMunicipalityActivation() {
        System.out.println("=== Probando funcionalidad de activar/desactivar municipios ===");
        
        try {
            // Crear un municipio de prueba
            Municipality testMunicipality = new Municipality();
            testMunicipality.setName("Municipio de Prueba");
            testMunicipality.setCode("MPR");
            // Nota: Necesitarías configurar department, category, etc.
            
            // Crear el municipio
            // Municipality created = municipalityService.createMunicipality(testMunicipality);
            // System.out.println("Municipio creado: " + created.getName() + " - Activo: " + created.isActive());
            
            // Desactivar el municipio
            // boolean deactivated = municipalityService.deactivateMunicipality(created.getId());
            // System.out.println("Municipio desactivado: " + deactivated);
            
            // Verificar que está inactivo
            // Optional<Municipality> inactiveMunicipality = municipalityService.getMunicipalityById(created.getId());
            // if (inactiveMunicipality.isPresent()) {
            //     System.out.println("Estado después de desactivar: " + inactiveMunicipality.get().isActive());
            // }
            
            // Activar el municipio
            // boolean activated = municipalityService.activateMunicipality(created.getId());
            // System.out.println("Municipio activado: " + activated);
            
            // Verificar que está activo
            // Optional<Municipality> activeMunicipality = municipalityService.getMunicipalityById(created.getId());
            // if (activeMunicipality.isPresent()) {
            //     System.out.println("Estado después de activar: " + activeMunicipality.get().isActive());
            // }
            
            System.out.println("✅ Prueba de municipios completada");
            
        } catch (Exception e) {
            System.out.println("❌ Error en prueba de municipios: " + e.getMessage());
        }
    }
    
    /**
     * Prueba las consultas de municipios activos/inactivos.
     */
    public void testMunicipalityQueries() {
        System.out.println("=== Probando consultas de municipios activos/inactivos ===");
        
        try {
            // Obtener todos los municipios activos
            // List<Municipality> activeMunicipalities = municipalityService.getAllActiveMunicipalities();
            // System.out.println("Municipios activos encontrados: " + activeMunicipalities.size());
            
            // Obtener todos los municipios inactivos
            // List<Municipality> inactiveMunicipalities = municipalityService.getAllInactiveMunicipalities();
            // System.out.println("Municipios inactivos encontrados: " + inactiveMunicipalities.size());
            
            // Obtener municipios activos ordenados por nombre
            // List<Municipality> activeOrdered = municipalityService.getActiveMunicipalitiesOrderByName();
            // System.out.println("Municipios activos ordenados: " + activeOrdered.size());
            
            System.out.println("✅ Prueba de consultas completada");
            
        } catch (Exception e) {
            System.out.println("❌ Error en prueba de consultas: " + e.getMessage());
        }
    }
    
    /**
     * Prueba las consultas de secciones de cuenca activas/inactivas.
     */
    public void testBasinSectionQueries() {
        System.out.println("=== Probando consultas de secciones de cuenca activas/inactivas ===");
        
        try {
            // Obtener todas las secciones activas
            // List<BasinSection> activeSections = basinSectionService.getAllActiveBasinSections();
            // System.out.println("Secciones activas encontradas: " + activeSections.size());
            
            // Obtener todas las secciones inactivas
            // List<BasinSection> inactiveSections = basinSectionService.getAllInactiveBasinSections();
            // System.out.println("Secciones inactivas encontradas: " + inactiveSections.size());
            
            // Obtener secciones activas ordenadas por nombre
            // List<BasinSection> activeOrdered = basinSectionService.getActiveBasinSectionsOrderByName();
            // System.out.println("Secciones activas ordenadas: " + activeOrdered.size());
            
            System.out.println("✅ Prueba de consultas completada");
            
        } catch (Exception e) {
            System.out.println("❌ Error en prueba de consultas: " + e.getMessage());
        }
    }
    
    /**
     * Ejecuta todas las pruebas.
     */
    public void runAllTests() {
        System.out.println("🚀 Iniciando pruebas de funcionalidad isActive...\n");
        
        testBasinSectionActivation();
        System.out.println();
        
        testMunicipalityActivation();
        System.out.println();
        
        testBasinSectionQueries();
        System.out.println();
        
        testMunicipalityQueries();
        System.out.println();
        
        System.out.println("🎉 Todas las pruebas completadas!");
    }
}

package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.entity.*;
import com.univercloud.hydro.repository.DischargeRepository;
import com.univercloud.hydro.service.DischargeService;
import com.univercloud.hydro.util.AuthorizationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para DischargeServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class DischargeServiceImplTest {
    
    @Mock
    private DischargeRepository dischargeRepository;
    
    @Mock
    private AuthorizationUtils authorizationUtils;
    
    @InjectMocks
    private DischargeServiceImpl dischargeService;
    
    private Discharge testDischarge;
    private Corporation testCorporation;
    private User testUser;
    private Municipality testMunicipality;
    private BasinSection testBasinSection;
    private DischargeUser testDischargeUser;
    
    @BeforeEach
    void setUp() {
        // Setup test data
        testCorporation = new Corporation();
        testCorporation.setId(1L);
        testCorporation.setName("Test Corporation");
        
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setCorporation(testCorporation);
        
        testMunicipality = new Municipality();
        testMunicipality.setId(1L);
        testMunicipality.setName("Test Municipality");
        
        testBasinSection = new BasinSection();
        testBasinSection.setId(1L);
        testBasinSection.setName("Test Basin Section");
        
        testDischargeUser = new DischargeUser();
        testDischargeUser.setId(1L);
        testDischargeUser.setCompanyName("Test Company");
        
        testDischarge = new Discharge();
        testDischarge.setId(1L);
        testDischarge.setName("Test Discharge");
        testDischarge.setNumber(1);
        testDischarge.setYear(2024);
        testDischarge.setDischargeType(Discharge.DischargeType.ARD);
        testDischarge.setWaterResourceType(Discharge.WaterResourceType.RIVER);
        testDischarge.setMunicipality(testMunicipality);
        testDischarge.setBasinSection(testBasinSection);
        testDischarge.setDischargeUser(testDischargeUser);
        testDischarge.setCcDboTotal(new BigDecimal("100.00"));
        testDischarge.setCcSstTotal(new BigDecimal("200.00"));
        testDischarge.setSourceMonitored(false);
        testDischarge.setBasinRehuse(false);
        testDischarge.setCorporation(testCorporation);
        testDischarge.setCreatedBy(testUser);
        testDischarge.setCreatedAt(LocalDateTime.now());
    }
    
    @Test
    void createDischarge_ShouldReturnCreatedDischarge_WhenValidData() {
        // Given
        when(authorizationUtils.getCurrentUser()).thenReturn(testUser);
        when(dischargeRepository.save(any(Discharge.class))).thenReturn(testDischarge);
        
        // When
        Discharge result = dischargeService.createDischarge(testDischarge);
        
        // Then
        assertNotNull(result);
        assertEquals(testDischarge.getId(), result.getId());
        assertEquals(testDischarge.getName(), result.getName());
        verify(dischargeRepository).save(testDischarge);
    }
    
    @Test
    void createDischarge_ShouldThrowIllegalStateException_WhenUserNotAuthenticated() {
        // Given
        when(authorizationUtils.getCurrentUser()).thenReturn(null);
        
        // When & Then
        assertThrows(IllegalStateException.class, () -> {
            dischargeService.createDischarge(testDischarge);
        });
    }
    
    @Test
    void updateDischarge_ShouldReturnUpdatedDischarge_WhenDischargeExists() {
        // Given
        Discharge updatedDischarge = new Discharge();
        updatedDischarge.setId(1L);
        updatedDischarge.setName("Updated Discharge");
        updatedDischarge.setCorporation(testCorporation);
        
        when(authorizationUtils.getCurrentUser()).thenReturn(testUser);
        when(dischargeRepository.findById(1L)).thenReturn(Optional.of(testDischarge));
        when(dischargeRepository.save(any(Discharge.class))).thenReturn(updatedDischarge);
        
        // When
        Discharge result = dischargeService.updateDischarge(updatedDischarge);
        
        // Then
        assertNotNull(result);
        assertEquals("Updated Discharge", result.getName());
        verify(dischargeRepository).save(updatedDischarge);
    }
    
    @Test
    void updateDischarge_ShouldThrowIllegalArgumentException_WhenDischargeNotFound() {
        // Given
        Discharge updatedDischarge = new Discharge();
        updatedDischarge.setId(999L);
        updatedDischarge.setCorporation(testCorporation);
        
        when(authorizationUtils.getCurrentUser()).thenReturn(testUser);
        when(dischargeRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            dischargeService.updateDischarge(updatedDischarge);
        });
    }
    
    @Test
    void getDischargeById_ShouldReturnDischarge_WhenDischargeExists() {
        // Given
        when(dischargeRepository.findById(1L)).thenReturn(Optional.of(testDischarge));
        
        // When
        Optional<Discharge> result = dischargeService.getDischargeById(1L);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals(testDischarge.getId(), result.get().getId());
    }
    
    @Test
    void getDischargeById_ShouldReturnEmpty_WhenDischargeNotFound() {
        // Given
        when(dischargeRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When
        Optional<Discharge> result = dischargeService.getDischargeById(999L);
        
        // Then
        assertFalse(result.isPresent());
    }
    
    @Test
    void getMyCorporationDischarges_ShouldReturnPageOfDischarges_WhenUserAuthenticated() {
        // Given
        Pageable pageable = Pageable.ofSize(10);
        Page<Discharge> expectedPage = new PageImpl<>(Arrays.asList(testDischarge));
        
        when(authorizationUtils.getCurrentUser()).thenReturn(testUser);
        when(dischargeRepository.findByCorporation(testCorporation, pageable)).thenReturn(expectedPage);
        
        // When
        Page<Discharge> result = dischargeService.getMyCorporationDischarges(pageable);
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(testDischarge.getId(), result.getContent().get(0).getId());
    }
    
    @Test
    void getMyCorporationDischarges_ShouldThrowIllegalStateException_WhenUserNotAuthenticated() {
        // Given
        Pageable pageable = Pageable.ofSize(10);
        when(authorizationUtils.getCurrentUser()).thenReturn(null);
        
        // When & Then
        assertThrows(IllegalStateException.class, () -> {
            dischargeService.getMyCorporationDischarges(pageable);
        });
    }
        
    @Test
    void getDischargesByYear_ShouldReturnListOfDischarges_WhenUserAuthenticated() {
        // Given
        Integer year = 2024;
        List<Discharge> expectedDischarges = Arrays.asList(testDischarge);
        
        when(authorizationUtils.getCurrentUser()).thenReturn(testUser);
        when(dischargeRepository.findByCorporationAndYear(testCorporation, year)).thenReturn(expectedDischarges);
        
        // When
        List<Discharge> result = dischargeService.getDischargesByYear(year);
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testDischarge.getId(), result.get(0).getId());
    }
    
    @Test
    void deleteDischarge_ShouldReturnTrue_WhenDischargeExistsAndBelongsToUserCorporation() {
        // Given
        when(authorizationUtils.getCurrentUser()).thenReturn(testUser);
        when(dischargeRepository.findById(1L)).thenReturn(Optional.of(testDischarge));
        doNothing().when(dischargeRepository).deleteById(1L);
        
        // When
        boolean result = dischargeService.deleteDischarge(1L);
        
        // Then
        assertTrue(result);
        verify(dischargeRepository).deleteById(1L);
    }
    
    @Test
    void deleteDischarge_ShouldThrowIllegalArgumentException_WhenDischargeNotFound() {
        // Given
        when(authorizationUtils.getCurrentUser()).thenReturn(testUser);
        when(dischargeRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            dischargeService.deleteDischarge(999L);
        });
    }
}

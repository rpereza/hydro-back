package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.entity.*;
import com.univercloud.hydro.repository.WaterBasinRepository;
import com.univercloud.hydro.service.WaterBasinService;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para WaterBasinServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class WaterBasinServiceImplTest {
    
    @Mock
    private WaterBasinRepository waterBasinRepository;
    
    @Mock
    private AuthorizationUtils authorizationUtils;
    
    @InjectMocks
    private WaterBasinServiceImpl waterBasinService;
    
    private WaterBasin testWaterBasin;
    private Corporation testCorporation;
    private User testUser;
    
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
        
        testWaterBasin = new WaterBasin();
        testWaterBasin.setId(1L);
        testWaterBasin.setName("Test Water Basin");
        testWaterBasin.setDescription("Test Description");
        testWaterBasin.setActive(true);
        testWaterBasin.setCorporation(testCorporation);
        testWaterBasin.setCreatedBy(testUser);
        testWaterBasin.setCreatedAt(LocalDateTime.now());
    }
    
    @Test
    void createWaterBasin_ShouldReturnCreatedWaterBasin_WhenValidData() {
        // Given
        when(authorizationUtils.getCurrentUser()).thenReturn(testUser);
        when(waterBasinRepository.save(any(WaterBasin.class))).thenReturn(testWaterBasin);
        
        // When
        WaterBasin result = waterBasinService.createWaterBasin(testWaterBasin);
        
        // Then
        assertNotNull(result);
        assertEquals(testWaterBasin.getId(), result.getId());
        assertEquals(testWaterBasin.getName(), result.getName());
        verify(waterBasinRepository).save(testWaterBasin);
    }
    
    @Test
    void createWaterBasin_ShouldThrowIllegalStateException_WhenUserNotAuthenticated() {
        // Given
        when(authorizationUtils.getCurrentUser()).thenReturn(null);
        
        // When & Then
        assertThrows(IllegalStateException.class, () -> {
            waterBasinService.createWaterBasin(testWaterBasin);
        });
    }
    
    @Test
    void updateWaterBasin_ShouldReturnUpdatedWaterBasin_WhenWaterBasinExists() {
        // Given
        WaterBasin updatedWaterBasin = new WaterBasin();
        updatedWaterBasin.setId(1L);
        updatedWaterBasin.setName("Updated Water Basin");
        updatedWaterBasin.setCorporation(testCorporation);
        
        when(authorizationUtils.getCurrentUser()).thenReturn(testUser);
        when(waterBasinRepository.findById(1L)).thenReturn(Optional.of(testWaterBasin));
        when(waterBasinRepository.save(any(WaterBasin.class))).thenReturn(updatedWaterBasin);
        
        // When
        WaterBasin result = waterBasinService.updateWaterBasin(updatedWaterBasin);
        
        // Then
        assertNotNull(result);
        assertEquals("Updated Water Basin", result.getName());
        verify(waterBasinRepository).save(updatedWaterBasin);
    }
    
    @Test
    void updateWaterBasin_ShouldThrowIllegalArgumentException_WhenWaterBasinNotFound() {
        // Given
        WaterBasin updatedWaterBasin = new WaterBasin();
        updatedWaterBasin.setId(999L);
        updatedWaterBasin.setCorporation(testCorporation);
        
        when(authorizationUtils.getCurrentUser()).thenReturn(testUser);
        when(waterBasinRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            waterBasinService.updateWaterBasin(updatedWaterBasin);
        });
    }
    
    @Test
    void getWaterBasinById_ShouldReturnWaterBasin_WhenWaterBasinExists() {
        // Given
        when(waterBasinRepository.findById(1L)).thenReturn(Optional.of(testWaterBasin));
        
        // When
        Optional<WaterBasin> result = waterBasinService.getWaterBasinById(1L);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals(testWaterBasin.getId(), result.get().getId());
    }
    
    @Test
    void getWaterBasinById_ShouldReturnEmpty_WhenWaterBasinNotFound() {
        // Given
        when(waterBasinRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When
        Optional<WaterBasin> result = waterBasinService.getWaterBasinById(999L);
        
        // Then
        assertFalse(result.isPresent());
    }
    
    @Test
    void getMyCorporationWaterBasins_ShouldReturnPageOfWaterBasins_WhenUserAuthenticated() {
        // Given
        Pageable pageable = Pageable.ofSize(10);
        Page<WaterBasin> expectedPage = new PageImpl<>(Arrays.asList(testWaterBasin));
        
        when(authorizationUtils.getCurrentUser()).thenReturn(testUser);
        when(waterBasinRepository.findByCorporation(testCorporation, pageable)).thenReturn(expectedPage);
        
        // When
        Page<WaterBasin> result = waterBasinService.getMyCorporationWaterBasins(pageable);
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(testWaterBasin.getId(), result.getContent().get(0).getId());
    }
    
    @Test
    void getMyCorporationWaterBasins_ShouldThrowIllegalStateException_WhenUserNotAuthenticated() {
        // Given
        Pageable pageable = Pageable.ofSize(10);
        when(authorizationUtils.getCurrentUser()).thenReturn(null);
        
        // When & Then
        assertThrows(IllegalStateException.class, () -> {
            waterBasinService.getMyCorporationWaterBasins(pageable);
        });
    }
    
    @Test
    void getAllMyCorporationWaterBasins_ShouldReturnListOfWaterBasins_WhenUserAuthenticated() {
        // Given
        List<WaterBasin> expectedWaterBasins = Arrays.asList(testWaterBasin);
        
        when(authorizationUtils.getCurrentUser()).thenReturn(testUser);
        when(waterBasinRepository.findByCorporation(testCorporation)).thenReturn(expectedWaterBasins);
        
        // When
        List<WaterBasin> result = waterBasinService.getAllMyCorporationWaterBasins();
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testWaterBasin.getId(), result.get(0).getId());
    }
    
    @Test
    void getActiveMyCorporationWaterBasins_ShouldReturnListOfActiveWaterBasins_WhenUserAuthenticated() {
        // Given
        List<WaterBasin> expectedWaterBasins = Arrays.asList(testWaterBasin);
        
        when(authorizationUtils.getCurrentUser()).thenReturn(testUser);
        when(waterBasinRepository.findByCorporationAndIsActiveTrue(testCorporation)).thenReturn(expectedWaterBasins);
        
        // When
        List<WaterBasin> result = waterBasinService.getActiveMyCorporationWaterBasins();
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testWaterBasin.getId(), result.get(0).getId());
    }
    
    @Test
    void getAllActiveWaterBasins_ShouldReturnListOfActiveWaterBasins() {
        // Given
        List<WaterBasin> expectedWaterBasins = Arrays.asList(testWaterBasin);
        
        when(waterBasinRepository.findByIsActiveTrue()).thenReturn(expectedWaterBasins);
        
        // When
        List<WaterBasin> result = waterBasinService.getAllActiveWaterBasins();
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testWaterBasin.getId(), result.get(0).getId());
    }
    
    @Test
    void getAllInactiveWaterBasins_ShouldReturnListOfInactiveWaterBasins() {
        // Given
        List<WaterBasin> expectedWaterBasins = Arrays.asList(testWaterBasin);
        
        when(waterBasinRepository.findByIsActiveFalse()).thenReturn(expectedWaterBasins);
        
        // When
        List<WaterBasin> result = waterBasinService.getAllInactiveWaterBasins();
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testWaterBasin.getId(), result.get(0).getId());
    }
    
    @Test
    void getWaterBasinByName_ShouldReturnWaterBasin_WhenWaterBasinExists() {
        // Given
        String name = "Test Water Basin";
        
        when(waterBasinRepository.findByName(name)).thenReturn(Optional.of(testWaterBasin));
        
        // When
        Optional<WaterBasin> result = waterBasinService.getWaterBasinByName(name);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals(testWaterBasin.getId(), result.get().getId());
    }
    
    @Test
    void getWaterBasinByName_ShouldReturnEmpty_WhenWaterBasinNotFound() {
        // Given
        String name = "Non-existent Water Basin";
        
        when(waterBasinRepository.findByName(name)).thenReturn(Optional.empty());
        
        // When
        Optional<WaterBasin> result = waterBasinService.getWaterBasinByName(name);
        
        // Then
        assertFalse(result.isPresent());
    }
    
    @Test
    void searchWaterBasinsByName_ShouldReturnListOfWaterBasins_WhenUserAuthenticated() {
        // Given
        String name = "Test";
        List<WaterBasin> expectedWaterBasins = Arrays.asList(testWaterBasin);
        
        when(authorizationUtils.getCurrentUser()).thenReturn(testUser);
        when(waterBasinRepository.findByCorporationAndNameContainingIgnoreCase(testCorporation, name)).thenReturn(expectedWaterBasins);
        
        // When
        List<WaterBasin> result = waterBasinService.searchWaterBasinsByName(name);
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testWaterBasin.getId(), result.get(0).getId());
    }
    
    @Test
    void searchActiveWaterBasinsByName_ShouldReturnListOfActiveWaterBasins_WhenUserAuthenticated() {
        // Given
        String name = "Test";
        List<WaterBasin> expectedWaterBasins = Arrays.asList(testWaterBasin);
        
        when(authorizationUtils.getCurrentUser()).thenReturn(testUser);
        when(waterBasinRepository.findByCorporationAndNameContainingIgnoreCase(testCorporation, name)).thenReturn(expectedWaterBasins);
        
        // When
        List<WaterBasin> result = waterBasinService.searchActiveWaterBasinsByName(name);
        
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testWaterBasin.getId(), result.get(0).getId());
    }
    
    @Test
    void activateWaterBasin_ShouldReturnTrue_WhenWaterBasinExistsAndBelongsToUserCorporation() {
        // Given
        when(authorizationUtils.getCurrentUser()).thenReturn(testUser);
        when(waterBasinRepository.findById(1L)).thenReturn(Optional.of(testWaterBasin));
        when(waterBasinRepository.save(any(WaterBasin.class))).thenReturn(testWaterBasin);
        
        // When
        boolean result = waterBasinService.activateWaterBasin(1L);
        
        // Then
        assertTrue(result);
        assertTrue(testWaterBasin.isActive());
        verify(waterBasinRepository).save(testWaterBasin);
    }
    
    @Test
    void deactivateWaterBasin_ShouldReturnTrue_WhenWaterBasinExistsAndBelongsToUserCorporation() {
        // Given
        when(authorizationUtils.getCurrentUser()).thenReturn(testUser);
        when(waterBasinRepository.findById(1L)).thenReturn(Optional.of(testWaterBasin));
        when(waterBasinRepository.save(any(WaterBasin.class))).thenReturn(testWaterBasin);
        
        // When
        boolean result = waterBasinService.deactivateWaterBasin(1L);
        
        // Then
        assertTrue(result);
        assertFalse(testWaterBasin.isActive());
        verify(waterBasinRepository).save(testWaterBasin);
    }
    
    @Test
    void deleteWaterBasin_ShouldReturnTrue_WhenWaterBasinExistsAndBelongsToUserCorporation() {
        // Given
        when(authorizationUtils.getCurrentUser()).thenReturn(testUser);
        when(waterBasinRepository.findById(1L)).thenReturn(Optional.of(testWaterBasin));
        doNothing().when(waterBasinRepository).deleteById(1L);
        
        // When
        boolean result = waterBasinService.deleteWaterBasin(1L);
        
        // Then
        assertTrue(result);
        verify(waterBasinRepository).deleteById(1L);
    }
    
    @Test
    void deleteWaterBasin_ShouldThrowIllegalArgumentException_WhenWaterBasinNotFound() {
        // Given
        when(authorizationUtils.getCurrentUser()).thenReturn(testUser);
        when(waterBasinRepository.findById(999L)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            waterBasinService.deleteWaterBasin(999L);
        });
    }
    
    @Test
    void countMyCorporationWaterBasins_ShouldReturnCount_WhenUserAuthenticated() {
        // Given
        long expectedCount = 5L;
        
        when(authorizationUtils.getCurrentUser()).thenReturn(testUser);
        when(waterBasinRepository.count()).thenReturn(expectedCount);
        
        // When
        long result = waterBasinService.countMyCorporationWaterBasins();
        
        // Then
        assertEquals(expectedCount, result);
    }
    
    @Test
    void countActiveMyCorporationWaterBasins_ShouldReturnCount_WhenUserAuthenticated() {
        // Given
        long expectedCount = 3L;
        
        when(authorizationUtils.getCurrentUser()).thenReturn(testUser);
        when(waterBasinRepository.count()).thenReturn(expectedCount);
        
        // When
        long result = waterBasinService.countActiveMyCorporationWaterBasins();
        
        // Then
        assertEquals(expectedCount, result);
    }
    
    @Test
    void existsByName_ShouldReturnTrue_WhenWaterBasinExists() {
        // Given
        String name = "Test Water Basin";
        
        when(waterBasinRepository.existsByName(name)).thenReturn(true);
        
        // When
        boolean result = waterBasinService.existsByName(name);
        
        // Then
        assertTrue(result);
    }
    
    @Test
    void existsByName_ShouldReturnFalse_WhenWaterBasinDoesNotExist() {
        // Given
        String name = "Non-existent Water Basin";
        
        when(waterBasinRepository.existsByName(name)).thenReturn(false);
        
        // When
        boolean result = waterBasinService.existsByName(name);
        
        // Then
        assertFalse(result);
    }
    
    @Test
    void getMyCorporationWaterBasinStats_ShouldReturnStats_WhenUserAuthenticated() {
        // Given
        long totalWaterBasins = 10L;
        
        when(authorizationUtils.getCurrentUser()).thenReturn(testUser);
        when(waterBasinRepository.count()).thenReturn(totalWaterBasins);
        
        // When
        WaterBasinService.WaterBasinStats result = waterBasinService.getMyCorporationWaterBasinStats();
        
        // Then
        assertNotNull(result);
        assertEquals(totalWaterBasins, result.getTotalWaterBasins());
    }
}

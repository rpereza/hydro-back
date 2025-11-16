package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.entity.Monitoring;
import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.User;
import com.univercloud.hydro.entity.MonitoringStation;
import com.univercloud.hydro.exception.DuplicateResourceException;
import com.univercloud.hydro.exception.ResourceNotFoundException;
import com.univercloud.hydro.repository.MonitoringRepository;
import com.univercloud.hydro.repository.MonitoringStationRepository;
import com.univercloud.hydro.service.MonitoringService;
import com.univercloud.hydro.util.AuthorizationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para la gestión de Monitoreos.
 * Proporciona operaciones CRUD y lógica de negocio para monitoreos de estaciones.
 */
@Service
@Transactional
public class MonitoringServiceImpl implements MonitoringService {
    
    @Autowired
    private MonitoringRepository monitoringRepository;
    
    @Autowired
    private MonitoringStationRepository monitoringStationRepository;
    
    @Autowired
    private AuthorizationUtils authorizationUtils;
    
    @Override
    @Transactional
    public Monitoring createMonitoring(Monitoring monitoring) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        // Verificar que no exista un monitoreo para la misma estación y fecha
        if (existsByStationAndDate(monitoring.getMonitoringStation().getId(), monitoring.getMonitoringDate())) {
            throw new DuplicateResourceException("Monitoring", "monitoringStation and monitoringDate", monitoring.getMonitoringStation().getId() + "/" + monitoring.getMonitoringDate());
        }
        
        // Verificar que la estación de monitoreo pertenezca a la corporación
        if (monitoring.getMonitoringStation() != null) {
            Optional<MonitoringStation> stationOpt = monitoringStationRepository.findById(monitoring.getMonitoringStation().getId());
            // Comparar por ID para evitar problemas con proxies de Hibernate
            if (stationOpt.isEmpty() || stationOpt.get().getCorporation() == null || !stationOpt.get().getCorporation().getId().equals(corporation.getId())) {
                throw new IllegalStateException("Monitoring station does not belong to your corporation");
            }
        }
        
        monitoring.setCorporation(corporation);
        monitoring.setCreatedBy(currentUser);
        monitoring.setCreatedAt(LocalDateTime.now());
        
        // Calcular iod basado en el valor de od
        if (monitoring.getOd() != null) {
            monitoring.setIod(calculateIod(monitoring.getOd()));
        }
        
        // Calcular isst basado en el valor de sst
        if (monitoring.getSst() != null) {
            monitoring.setIsst(calculateIsst(monitoring.getSst()));
        }
        
        // Calcular idqo basado en el valor de dqo
        if (monitoring.getDqo() != null) {
            monitoring.setIdqo(calculateIdqo(monitoring.getDqo()));
        }
        
        // Calcular ice basado en el valor de ce
        if (monitoring.getCe() != null) {
            monitoring.setIce(calculateIce(monitoring.getCe()));
        }
        
        // Calcular iph basado en el valor de ph
        if (monitoring.getPh() != null) {
            monitoring.setIph(calculateIph(monitoring.getPh()));
        }
        
        // Calcular rnp e irnp basados en los valores de n y p
        calculateRnpAndIrnp(monitoring);
        
        // Calcular numberIcaVariables basado en los índices calculados
        monitoring.setNumberIcaVariables(calculateNumberIcaVariables(monitoring));
        
        // Calcular icaCoefficient basado en numberIcaVariables
        monitoring.setIcaCoefficient(calculateIcaCoefficient(monitoring));
        
        // Calcular qualityClasification basado en icaCoefficient
        monitoring.setQualityClasification(calculateQualityClasification(monitoring.getIcaCoefficient()));
        
        return monitoringRepository.save(monitoring);
    }
    
    @Override
    @Transactional
    public Monitoring updateMonitoring(Monitoring monitoring) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        Optional<Monitoring> existingOpt = monitoringRepository.findById(monitoring.getId());
        if (existingOpt.isEmpty()) {
            throw new ResourceNotFoundException("Monitoring", "id", monitoring.getId());
        }
        
        Monitoring existing = existingOpt.get();
        
        // Verificar que pertenezca a la corporación del usuario
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (existing.getCorporation() == null || !existing.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalStateException("You do not have permission to update this monitoring");
        }
        
        // Verificar que no exista otro monitoreo para la misma estación y fecha (si cambió)
        if (!existing.getMonitoringStation().getId().equals(monitoring.getMonitoringStation().getId()) ||
            !existing.getMonitoringDate().equals(monitoring.getMonitoringDate())) {
            if (existsByStationAndDate(monitoring.getMonitoringStation().getId(), monitoring.getMonitoringDate())) {
                throw new DuplicateResourceException("Monitoring", "monitoringStation and monitoringDate", monitoring.getMonitoringStation().getId() + "/" + monitoring.getMonitoringDate());
            }
        }
        
        // Verificar que la estación de monitoreo pertenezca a la corporación
        if (monitoring.getMonitoringStation() != null) {
            Optional<MonitoringStation> stationOpt = monitoringStationRepository.findById(monitoring.getMonitoringStation().getId());
            // Comparar por ID para evitar problemas con proxies de Hibernate
            if (stationOpt.isEmpty() || stationOpt.get().getCorporation() == null || !stationOpt.get().getCorporation().getId().equals(corporation.getId())) {
                throw new IllegalStateException("Monitoring station does not belong to your corporation");
            }
        }
        
        existing.setMonitoringStation(monitoring.getMonitoringStation());
        existing.setMonitoringDate(monitoring.getMonitoringDate());
        existing.setWaterTemperature(monitoring.getWaterTemperature());
        existing.setAirTemperature(monitoring.getAirTemperature());
        
        // Guardar el valor anterior de ph para comparar
        BigDecimal previousPh = existing.getPh();
        existing.setPh(monitoring.getPh());
        
        // Calcular iph basado en el valor de ph solo si ph cambió
        if (monitoring.getPh() != null && hasPhChanged(previousPh, monitoring.getPh())) {
            existing.setIph(calculateIph(monitoring.getPh()));
        }
        
        // Guardar el valor anterior de od para comparar
        BigDecimal previousOd = existing.getOd();
        existing.setOd(monitoring.getOd());
        
        // Calcular iod basado en el valor de od solo si od cambió
        if (monitoring.getOd() != null && hasOdChanged(previousOd, monitoring.getOd())) {
            existing.setIod(calculateIod(monitoring.getOd()));
        }
        
        // Guardar el valor anterior de sst para comparar
        BigDecimal previousSst = existing.getSst();
        existing.setSst(monitoring.getSst());
        
        // Calcular isst basado en el valor de sst solo si sst cambió
        if (monitoring.getSst() != null && hasSstChanged(previousSst, monitoring.getSst())) {
            existing.setIsst(calculateIsst(monitoring.getSst()));
        }
        
        // Guardar el valor anterior de dqo para comparar
        BigDecimal previousDqo = existing.getDqo();
        existing.setDqo(monitoring.getDqo());
        
        // Calcular idqo basado en el valor de dqo solo si dqo cambió
        if (monitoring.getDqo() != null && hasDqoChanged(previousDqo, monitoring.getDqo())) {
            existing.setIdqo(calculateIdqo(monitoring.getDqo()));
        }
        
        // Guardar el valor anterior de ce para comparar
        BigDecimal previousCe = existing.getCe();
        existing.setCe(monitoring.getCe());
        
        // Calcular ice basado en el valor de ce solo si ce cambió
        if (monitoring.getCe() != null && hasCeChanged(previousCe, monitoring.getCe())) {
            existing.setIce(calculateIce(monitoring.getCe()));
        }
        
        // Guardar los valores anteriores de n y p para comparar
        BigDecimal previousN = existing.getN();
        BigDecimal previousP = existing.getP();
        existing.setN(monitoring.getN());
        existing.setP(monitoring.getP());
        
        // Calcular rnp e irnp basados en los valores de n y p solo si alguno cambió
        boolean irnpChanged = hasNOrPChanged(previousN, previousP, monitoring.getN(), monitoring.getP());
        if (irnpChanged) {
            calculateRnpAndIrnp(existing);
            // Calcular numberIcaVariables solo si irnp cambió
            existing.setNumberIcaVariables(calculateNumberIcaVariables(existing));
        }
        
        // Calcular icaCoefficient siempre, ya que dependen de todos los índices
        existing.setIcaCoefficient(calculateIcaCoefficient(existing));
        
        // Calcular qualityClasification basado en icaCoefficient
        existing.setQualityClasification(calculateQualityClasification(existing.getIcaCoefficient()));
        
        existing.setPerformedBy(monitoring.getPerformedBy());
        existing.setNotes(monitoring.getNotes());
        existing.setWeatherConditions(monitoring.getWeatherConditions());
        existing.setCaudalVolumen(monitoring.getCaudalVolumen());
        existing.setUpdatedBy(currentUser);
        existing.setUpdatedAt(LocalDateTime.now());
        
        return monitoringRepository.save(existing);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Monitoring> getMonitoringById(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        // Buscar directamente por ID y corporationId para evitar problemas con lazy loading
        return monitoringRepository.findByIdAndCorporationId(id, corporation.getId());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Monitoring> getMyCorporationMonitorings(Pageable pageable) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        return monitoringRepository.findByCorporation(corporation, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Monitoring> getMonitoringsByStation(Long monitoringStationId, Pageable pageable) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        Optional<MonitoringStation> stationOpt = monitoringStationRepository.findById(monitoringStationId);
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (stationOpt.isEmpty() || stationOpt.get().getCorporation() == null || !stationOpt.get().getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalArgumentException("Monitoring station does not belong to your corporation");
        }
        
        return monitoringRepository.findByMonitoringStation(stationOpt.get(), pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Monitoring> getMonitoringsByStation(Long monitoringStationId) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        Optional<MonitoringStation> stationOpt = monitoringStationRepository.findById(monitoringStationId);
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (stationOpt.isEmpty() || stationOpt.get().getCorporation() == null || !stationOpt.get().getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalArgumentException("Monitoring station does not belong to your corporation");
        }
        
        return monitoringRepository.findByMonitoringStation(stationOpt.get());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Monitoring> getMonitoringsByDate(LocalDate monitoringDate) {
        return monitoringRepository.findByMonitoringDate(monitoringDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Monitoring> getMonitoringByStationAndDate(Long monitoringStationId, LocalDate monitoringDate) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        Optional<MonitoringStation> stationOpt = monitoringStationRepository.findById(monitoringStationId);
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (stationOpt.isEmpty() || stationOpt.get().getCorporation() == null || !stationOpt.get().getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalArgumentException("Monitoring station does not belong to your corporation");
        }
        
        return monitoringRepository.findByMonitoringStationAndMonitoringDate(stationOpt.get(), monitoringDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Monitoring> getMonitoringsByDateRange(LocalDate startDate, LocalDate endDate) {
        return monitoringRepository.findByMonitoringDateBetween(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Monitoring> getMonitoringsByStationAndDateRange(Long monitoringStationId, LocalDate startDate, LocalDate endDate) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        Optional<MonitoringStation> stationOpt = monitoringStationRepository.findById(monitoringStationId);
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (stationOpt.isEmpty() || stationOpt.get().getCorporation() == null || !stationOpt.get().getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalArgumentException("Monitoring station does not belong to your corporation");
        }
        
        return monitoringRepository.findByMonitoringStationAndMonitoringDateBetween(stationOpt.get(), startDate, endDate);
    }
        
    @Override
    @Transactional(readOnly = true)
    public Optional<Monitoring> getLatestMonitoringByStation(Long monitoringStationId) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        Optional<MonitoringStation> stationOpt = monitoringStationRepository.findById(monitoringStationId);
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (stationOpt.isEmpty() || stationOpt.get().getCorporation() == null || !stationOpt.get().getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalArgumentException("Monitoring station does not belong to your corporation");
        }
        
        return monitoringRepository.findLatestByMonitoringStation(stationOpt.get());
    }
        
    @Override
    @Transactional
    public boolean deleteMonitoring(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        Optional<Monitoring> monitoringOpt = monitoringRepository.findById(id);
        if (monitoringOpt.isEmpty()) {
            throw new ResourceNotFoundException("Monitoring", "id", id);
        }
        
        Monitoring monitoring = monitoringOpt.get();
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (monitoring.getCorporation() == null || !monitoring.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalArgumentException("You do not have permission to delete this monitoring");
        }
        
        monitoringRepository.delete(monitoring);
        return true;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByStationAndDate(Long monitoringStationId, LocalDate monitoringDate) {
        Optional<MonitoringStation> stationOpt = monitoringStationRepository.findById(monitoringStationId);
        if (stationOpt.isEmpty()) {
            return false;
        }
        return monitoringRepository.existsByMonitoringStationAndMonitoringDate(stationOpt.get(), monitoringDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public MonitoringStats getMyCorporationMonitoringStats() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        MonitoringStats stats = new MonitoringStats();
        
       
        // Estadísticas de estaciones activas
        List<MonitoringStation> activeStations = monitoringStationRepository.findByCorporationAndIsActiveTrue(corporation);
        long activeStationsCount = activeStations.size();
        
        stats.setActiveStations(activeStationsCount);
        
        return stats;
    }
    
    /**
     * Verifica si el valor de OD cambió comparando el valor anterior con el nuevo.
     * 
     * @param previousOd Valor anterior de OD
     * @param newOd Nuevo valor de OD
     * @return true si el valor cambió, false en caso contrario
     */
    private boolean hasOdChanged(BigDecimal previousOd, BigDecimal newOd) {
        if (previousOd == null && newOd == null) {
            return false;
        }
        if (previousOd == null || newOd == null) {
            return true;
        }
        return previousOd.compareTo(newOd) != 0;
    }
    
    /**
     * Verifica si el valor de SST cambió comparando el valor anterior con el nuevo.
     * 
     * @param previousSst Valor anterior de SST
     * @param newSst Nuevo valor de SST
     * @return true si el valor cambió, false en caso contrario
     */
    private boolean hasSstChanged(BigDecimal previousSst, BigDecimal newSst) {
        if (previousSst == null && newSst == null) {
            return false;
        }
        if (previousSst == null || newSst == null) {
            return true;
        }
        return previousSst.compareTo(newSst) != 0;
    }
    
    /**
     * Calcula el valor de IOD basado en el valor de OD.
     * 
     * @param od Valor de OD (Oxígeno Disuelto)
     * @return Valor calculado de IOD
     */
    private BigDecimal calculateIod(BigDecimal od) {
        if (od == null) {
            return null;
        }
        
        // Constantes para el cálculo
        BigDecimal one = BigDecimal.ONE;
        BigDecimal hundred = new BigDecimal("100");
        BigDecimal pointZeroOne = new BigDecimal("0.01");
        MathContext mathContext = new MathContext(11, RoundingMode.HALF_UP);
        
        // Calcular 0.01 * od
        BigDecimal pointZeroOneTimesOd = pointZeroOne.multiply(od, mathContext);
        
        // Comparar od con 100
        if (od.compareTo(hundred) > 0) {
            // Si od > 100: iod = 1 − (0,01 * od − 1)
            BigDecimal innerExpression = pointZeroOneTimesOd.subtract(one, mathContext);
            return one.subtract(innerExpression, mathContext);
        } else {
            // Si od <= 100: iod = 1 − (1 − 0,01 * od)
            BigDecimal innerExpression = one.subtract(pointZeroOneTimesOd, mathContext);
            return one.subtract(innerExpression, mathContext);
        }
    }
    
    /**
     * Calcula el valor de ISST basado en el valor de SST.
     * 
     * @param sst Valor de SST (Sólidos Suspendidos Totales)
     * @return Valor calculado de ISST
     */
    private BigDecimal calculateIsst(BigDecimal sst) {
        if (sst == null) {
            return null;
        }
        
        // Constantes para el cálculo
        BigDecimal one = BigDecimal.ONE;
        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal fourPointFive = new BigDecimal("4.5");
        BigDecimal threeHundredTwenty = new BigDecimal("320");
        BigDecimal zeroPointZeroZeroThree = new BigDecimal("0.003");
        MathContext mathContext = new MathContext(11, RoundingMode.HALF_UP);
        
        // Si sst <= 4.5: isst = 1
        if (sst.compareTo(fourPointFive) <= 0) {
            return one;
        }
        
        // Si sst >= 320: isst = 0
        if (sst.compareTo(threeHundredTwenty) >= 0) {
            return zero;
        }
        
        // Si 4.5 < sst < 320: isst = 1 − (−0,02 + 0,003 * sst)
        // Simplificado: isst = 1 + 0.02 - 0.003 * sst = 1.02 - 0.003 * sst
        BigDecimal onePointZeroTwo = new BigDecimal("1.02");
        BigDecimal zeroPointZeroZeroThreeTimesSst = zeroPointZeroZeroThree.multiply(sst, mathContext);
        return onePointZeroTwo.subtract(zeroPointZeroZeroThreeTimesSst, mathContext);
    }
    
    /**
     * Verifica si el valor de DQO cambió comparando el valor anterior con el nuevo.
     * 
     * @param previousDqo Valor anterior de DQO
     * @param newDqo Nuevo valor de DQO
     * @return true si el valor cambió, false en caso contrario
     */
    private boolean hasDqoChanged(BigDecimal previousDqo, BigDecimal newDqo) {
        if (previousDqo == null && newDqo == null) {
            return false;
        }
        if (previousDqo == null || newDqo == null) {
            return true;
        }
        return previousDqo.compareTo(newDqo) != 0;
    }
    
    /**
     * Calcula el valor de IDQO basado en el valor de DQO según rangos específicos.
     * 
     * @param dqo Valor de DQO (Demanda Química de Oxígeno)
     * @return Valor calculado de IDQO
     */
    private BigDecimal calculateIdqo(BigDecimal dqo) {
        if (dqo == null) {
            return null;
        }
        
        // Constantes para los rangos y valores
        BigDecimal twenty = new BigDecimal("20");
        BigDecimal twentyFive = new BigDecimal("25");
        BigDecimal forty = new BigDecimal("40");
        BigDecimal eighty = new BigDecimal("80");
        
        BigDecimal value091 = new BigDecimal("0.91");
        BigDecimal value071 = new BigDecimal("0.71");
        BigDecimal value051 = new BigDecimal("0.51");
        BigDecimal value026 = new BigDecimal("0.26");
        BigDecimal value0125 = new BigDecimal("0.125");
        
        // Si dqo <= 20: idqo = 0.91
        if (dqo.compareTo(twenty) <= 0) {
            return value091;
        }
        
        // Si 20 < dqo <= 25: idqo = 0.71
        if (dqo.compareTo(twenty) > 0 && dqo.compareTo(twentyFive) <= 0) {
            return value071;
        }
        
        // Si 25 < dqo <= 40: idqo = 0.51
        if (dqo.compareTo(twentyFive) > 0 && dqo.compareTo(forty) <= 0) {
            return value051;
        }
        
        // Si 40 < dqo <= 80: idqo = 0.26
        if (dqo.compareTo(forty) > 0 && dqo.compareTo(eighty) <= 0) {
            return value026;
        }
        
        // Si dqo > 80: idqo = 0.125
        return value0125;
    }
    
    /**
     * Verifica si el valor de CE cambió comparando el valor anterior con el nuevo.
     * 
     * @param previousCe Valor anterior de CE
     * @param newCe Nuevo valor de CE
     * @return true si el valor cambió, false en caso contrario
     */
    private boolean hasCeChanged(BigDecimal previousCe, BigDecimal newCe) {
        if (previousCe == null && newCe == null) {
            return false;
        }
        if (previousCe == null || newCe == null) {
            return true;
        }
        return previousCe.compareTo(newCe) != 0;
    }
    
    /**
     * Calcula el valor de ICE basado en el valor de CE.
     * Fórmula: ice = 1 - 10^(-3.26 + 1.34 * log10(ce))
     * Si el resultado es < 0, se ajusta a 0.
     * 
     * @param ce Valor de CE (Conductividad Eléctrica)
     * @return Valor calculado de ICE
     */
    private BigDecimal calculateIce(BigDecimal ce) {
        if (ce == null) {
            return null;
        }
        
        // Convertir BigDecimal a double para usar funciones matemáticas
        double ceValue = ce.doubleValue();
        
        // Validar que ce sea positivo para el logaritmo
        if (ceValue <= 0) {
            throw new IllegalArgumentException("CE value must be greater than 0 for logarithm calculation");
        }
        
        // Calcular log10(ce)
        double log10Ce = Math.log10(ceValue);
        
        // Calcular -3.26 + 1.34 * log10(ce)
        double exponent = -3.26 + 1.34 * log10Ce;
        
        // Calcular 10^exponent
        double tenToExponent = Math.pow(10, exponent);
        
        // Calcular 1 - 10^exponent
        double iceValue = 1.0 - tenToExponent;
        
        // Si el resultado es < 0, ajustar a 0
        if (iceValue < 0) {
            iceValue = 0.0;
        }
        
        // Convertir de vuelta a BigDecimal con precisión adecuada
        MathContext mathContext = new MathContext(11, RoundingMode.HALF_UP);
        return new BigDecimal(iceValue, mathContext);
    }
    
    /**
     * Verifica si el valor de PH cambió comparando el valor anterior con el nuevo.
     * 
     * @param previousPh Valor anterior de PH
     * @param newPh Nuevo valor de PH
     * @return true si el valor cambió, false en caso contrario
     */
    private boolean hasPhChanged(BigDecimal previousPh, BigDecimal newPh) {
        if (previousPh == null && newPh == null) {
            return false;
        }
        if (previousPh == null || newPh == null) {
            return true;
        }
        return previousPh.compareTo(newPh) != 0;
    }
    
    /**
     * Calcula el valor de IPH basado en el valor de PH según rangos específicos.
     * 
     * @param ph Valor de PH
     * @return Valor calculado de IPH
     */
    private BigDecimal calculateIph(BigDecimal ph) {
        if (ph == null) {
            return null;
        }
        
        // Convertir BigDecimal a double para usar funciones matemáticas
        double phValue = ph.doubleValue();
        
        // Constantes para los rangos y valores
        BigDecimal four = new BigDecimal("4");
        BigDecimal seven = new BigDecimal("7");
        BigDecimal eight = new BigDecimal("8");
        BigDecimal eleven = new BigDecimal("11");
        BigDecimal zeroPointOne = new BigDecimal("0.1");
        BigDecimal one = BigDecimal.ONE;
        
        MathContext mathContext = new MathContext(11, RoundingMode.HALF_UP);
        
        // Si ph < 4: iph = 0.1
        if (ph.compareTo(four) < 0) {
            return zeroPointOne;
        }
        
        // Si 4 <= ph <= 7: iph = 0.02628419 * e^(ph * 0.520025)
        if (ph.compareTo(four) >= 0 && ph.compareTo(seven) <= 0) {
            double exponent = phValue * 0.520025;
            double eToExponent = Math.exp(exponent);
            double iphValue = 0.02628419 * eToExponent;
            return new BigDecimal(iphValue, mathContext);
        }
        
        // Si 7 < ph <= 8: iph = 1
        if (ph.compareTo(seven) > 0 && ph.compareTo(eight) <= 0) {
            return one;
        }
        
        // Si 8 < ph <= 11: iph = 1 * e^((ph - 8) * -0.5187742)
        if (ph.compareTo(eight) > 0 && ph.compareTo(eleven) <= 0) {
            double exponent = (phValue - 8.0) * -0.5187742;
            double eToExponent = Math.exp(exponent);
            double iphValue = 1.0 * eToExponent;
            return new BigDecimal(iphValue, mathContext);
        }
        
        // Si ph > 11: iph = 0.1
        return zeroPointOne;
    }
    
    /**
     * Verifica si alguno de los valores de N o P cambió comparando los valores anteriores con los nuevos.
     * 
     * @param previousN Valor anterior de N
     * @param previousP Valor anterior de P
     * @param newN Nuevo valor de N
     * @param newP Nuevo valor de P
     * @return true si alguno de los valores cambió, false en caso contrario
     */
    private boolean hasNOrPChanged(BigDecimal previousN, BigDecimal previousP, BigDecimal newN, BigDecimal newP) {
        // Verificar si N cambió
        boolean nChanged = false;
        if (previousN == null && newN == null) {
            nChanged = false;
        } else if (previousN == null || newN == null) {
            nChanged = true;
        } else {
            nChanged = previousN.compareTo(newN) != 0;
        }
        
        // Verificar si P cambió
        boolean pChanged = false;
        if (previousP == null && newP == null) {
            pChanged = false;
        } else if (previousP == null || newP == null) {
            pChanged = true;
        } else {
            pChanged = previousP.compareTo(newP) != 0;
        }
        
        return nChanged || pChanged;
    }
    
    /**
     * Calcula RNP (división de N y P) e IRNP basado en RNP.
     * Si tanto N como P son nulos, RNP e IRNP se setean a nulo.
     * 
     * @param monitoring Objeto Monitoring al que se le calcularán rnp e irnp
     */
    private void calculateRnpAndIrnp(Monitoring monitoring) {
        BigDecimal n = monitoring.getN();
        BigDecimal p = monitoring.getP();
        
        // Si tanto n como p son nulos, setear rnp e irnp a nulo
        if (n == null && p == null) {
            monitoring.setRnp(null);
            monitoring.setIrnp(null);
            return;
        }
        
        // Si alguno es nulo, no se puede calcular rnp
        if (n == null || p == null) {
            monitoring.setRnp(null);
            monitoring.setIrnp(null);
            return;
        }
        
        // Validar que p no sea cero para evitar división por cero
        if (p.compareTo(BigDecimal.ZERO) == 0) {
            monitoring.setRnp(null);
            monitoring.setIrnp(null);
            return;
        }
        
        // Calcular rnp = n / p
        MathContext mathContext = new MathContext(11, RoundingMode.HALF_UP);
        BigDecimal rnp = n.divide(p, mathContext);
        monitoring.setRnp(rnp);
        
        // Calcular irnp basado en rnp
        monitoring.setIrnp(calculateIrnp(rnp));
    }
    
    /**
     * Calcula el valor de IRNP basado en el valor de RNP según rangos específicos.
     * 
     * @param rnp Valor de RNP (Relación N/P)
     * @return Valor calculado de IRNP
     */
    private BigDecimal calculateIrnp(BigDecimal rnp) {
        if (rnp == null) {
            return null;
        }
        
        // Constantes para los rangos y valores
        BigDecimal five = new BigDecimal("5");
        BigDecimal ten = new BigDecimal("10");
        BigDecimal fifteen = new BigDecimal("15");
        BigDecimal twenty = new BigDecimal("20");
        
        BigDecimal value035 = new BigDecimal("0.35");
        BigDecimal value06 = new BigDecimal("0.6");
        BigDecimal value08 = new BigDecimal("0.8");
        BigDecimal value015 = new BigDecimal("0.15");
        
        // Si rnp > 5 y <= 10: irnp = 0.35
        if (rnp.compareTo(five) > 0 && rnp.compareTo(ten) <= 0) {
            return value035;
        }
        
        // Si rnp > 10 y < 15: irnp = 0.6
        if (rnp.compareTo(ten) > 0 && rnp.compareTo(fifteen) < 0) {
            return value06;
        }
        
        // Si rnp >= 15 y <= 20: irnp = 0.8
        if (rnp.compareTo(fifteen) >= 0 && rnp.compareTo(twenty) <= 0) {
            return value08;
        }
        
        // Si rnp <= 5 o > 20: irnp = 0.15
        return value015;
    }
    
    /**
     * Calcula el número de variables ICA que tienen valor (no son nulas).
     * Evalúa los siguientes atributos: iod, isst, idqo, ice, iph, irnp
     * 
     * @param monitoring Objeto Monitoring del cual se calculará el número de variables ICA
     * @return Número de variables ICA que tienen valor (0-6)
     */
    private Integer calculateNumberIcaVariables(Monitoring monitoring) {
        if (monitoring == null) {
            return 0;
        }
        
        int count = 0;
        
        // Contar cuántos atributos son diferentes de null
        if (monitoring.getIod() != null) {
            count++;
        }
        if (monitoring.getIsst() != null) {
            count++;
        }
        if (monitoring.getIdqo() != null) {
            count++;
        }
        if (monitoring.getIce() != null) {
            count++;
        }
        if (monitoring.getIph() != null) {
            count++;
        }
        if (monitoring.getIrnp() != null) {
            count++;
        }
        
        return count;
    }
    
    /**
     * Calcula el coeficiente ICA basado en el número de variables ICA y los índices correspondientes.
     * 
     * @param monitoring Objeto Monitoring del cual se calculará el coeficiente ICA
     * @return Valor calculado del coeficiente ICA
     * @throws IllegalArgumentException si numberIcaVariables no es 5 ni 6
     */
    private BigDecimal calculateIcaCoefficient(Monitoring monitoring) {
        if (monitoring == null) {
            return null;
        }
        
        Integer numberIcaVariables = monitoring.getNumberIcaVariables();
        
        if (numberIcaVariables == null) {
            return null;
        }
        
        MathContext mathContext = new MathContext(3, RoundingMode.HALF_UP);
        
        if (numberIcaVariables == 5) {
            // Fórmula: (iod*0.2)+(isst*0.2)+(idqo*0.2)+(ice*0.2)+(iph*0.2)
            BigDecimal weight = new BigDecimal("0.2");
            BigDecimal result = BigDecimal.ZERO;
            
            if (monitoring.getIod() != null) {
                result = result.add(monitoring.getIod().multiply(weight, mathContext), mathContext);
            }
            if (monitoring.getIsst() != null) {
                result = result.add(monitoring.getIsst().multiply(weight, mathContext), mathContext);
            }
            if (monitoring.getIdqo() != null) {
                result = result.add(monitoring.getIdqo().multiply(weight, mathContext), mathContext);
            }
            if (monitoring.getIce() != null) {
                result = result.add(monitoring.getIce().multiply(weight, mathContext), mathContext);
            }
            if (monitoring.getIph() != null) {
                result = result.add(monitoring.getIph().multiply(weight, mathContext), mathContext);
            }
            
            return result;
        } else if (numberIcaVariables == 6) {
            // Fórmula: (iod*0.17)+(isst*0.17)+(idqo*0.17)+(ice*0.17)+(iph*0.15)+(irnp*0.17)
            BigDecimal weight017 = new BigDecimal("0.17");
            BigDecimal weight015 = new BigDecimal("0.15");
            BigDecimal result = BigDecimal.ZERO;
            
            if (monitoring.getIod() != null) {
                result = result.add(monitoring.getIod().multiply(weight017, mathContext), mathContext);
            }
            if (monitoring.getIsst() != null) {
                result = result.add(monitoring.getIsst().multiply(weight017, mathContext), mathContext);
            }
            if (monitoring.getIdqo() != null) {
                result = result.add(monitoring.getIdqo().multiply(weight017, mathContext), mathContext);
            }
            if (monitoring.getIce() != null) {
                result = result.add(monitoring.getIce().multiply(weight017, mathContext), mathContext);
            }
            if (monitoring.getIph() != null) {
                result = result.add(monitoring.getIph().multiply(weight015, mathContext), mathContext);
            }
            if (monitoring.getIrnp() != null) {
                result = result.add(monitoring.getIrnp().multiply(weight017, mathContext), mathContext);
            }
            
            return result;
        } else {
            throw new IllegalArgumentException("numberIcaVariables must be 5 or 6, but was: " + numberIcaVariables);
        }
    }
    
    /**
     * Calcula la clasificación de calidad basada en el coeficiente ICA.
     * 
     * @param icaCoefficient Valor del coeficiente ICA
     * @return Clasificación de calidad correspondiente
     * @throws IllegalArgumentException si el icaCoefficient está fuera del rango válido (0-1)
     */
    private Monitoring.QualityClasification calculateQualityClasification(BigDecimal icaCoefficient) {
        if (icaCoefficient == null) {
            return null;
        }
        
        // Constantes para los rangos
        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal zeroPoint25 = new BigDecimal("0.25");
        BigDecimal zeroPoint5 = new BigDecimal("0.5");
        BigDecimal zeroPoint7 = new BigDecimal("0.7");
        BigDecimal zeroPoint9 = new BigDecimal("0.9");
        BigDecimal one = BigDecimal.ONE;
        
        // Si icaCoefficient >= 0 y <= 0.25: MUY_MALA
        if (icaCoefficient.compareTo(zero) >= 0 && icaCoefficient.compareTo(zeroPoint25) <= 0) {
            return Monitoring.QualityClasification.MUY_MALA;
        }
        
        // Si icaCoefficient > 0.25 y <= 0.5: MALA
        if (icaCoefficient.compareTo(zeroPoint25) > 0 && icaCoefficient.compareTo(zeroPoint5) <= 0) {
            return Monitoring.QualityClasification.MALA;
        }
        
        // Si icaCoefficient > 0.5 y <= 0.7: REGULAR
        if (icaCoefficient.compareTo(zeroPoint5) > 0 && icaCoefficient.compareTo(zeroPoint7) <= 0) {
            return Monitoring.QualityClasification.REGULAR;
        }
        
        // Si icaCoefficient > 0.7 y <= 0.9: ACEPTABLE
        if (icaCoefficient.compareTo(zeroPoint7) > 0 && icaCoefficient.compareTo(zeroPoint9) <= 0) {
            return Monitoring.QualityClasification.ACEPTABLE;
        }
        
        // Si icaCoefficient > 0.9 y <= 1: BUENA
        if (icaCoefficient.compareTo(zeroPoint9) > 0 && icaCoefficient.compareTo(one) <= 0) {
            return Monitoring.QualityClasification.BUENA;
        }
        
        // Para otros valores: lanzar IllegalArgumentException
        throw new IllegalArgumentException("icaCoefficient must be between 0 and 1, but was: " + icaCoefficient);
    }
}

package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.dto.InvoiceResponse;
import com.univercloud.hydro.entity.Invoice;
import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.User;
import com.univercloud.hydro.entity.Discharge;
import com.univercloud.hydro.entity.SequenceType;
import com.univercloud.hydro.entity.DischargeParameter;
import com.univercloud.hydro.entity.ProjectProgress;
import com.univercloud.hydro.entity.MinimumTariff;
import com.univercloud.hydro.enums.QualityClasification;
import com.univercloud.hydro.dto.DischargeInvoiceDto;
import com.univercloud.hydro.dto.DischargeParameterInvoiceDto;
import com.univercloud.hydro.dto.DischargeMonitoringInvoiceDto;
import com.univercloud.hydro.dto.MinimumTariffDto;
import com.univercloud.hydro.dto.ProjectProgressDto;
import com.univercloud.hydro.exception.ResourceNotFoundException;
import com.univercloud.hydro.repository.InvoiceRepository;
import com.univercloud.hydro.repository.DischargeRepository;
import com.univercloud.hydro.repository.MinimumTariffRepository;
import com.univercloud.hydro.repository.ProjectProgressRepository;
import com.univercloud.hydro.service.ConsecutiveSequenceService;
import com.univercloud.hydro.service.InvoiceService;
import com.univercloud.hydro.util.AuthorizationUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del servicio para la gestión de Facturas.
 * Proporciona operaciones CRUD y lógica de negocio para facturas.
 */
@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {
    
    @Autowired
    private InvoiceRepository invoiceRepository;
    
    @Autowired
    private DischargeRepository dischargeRepository;
    
    @Autowired
    private MinimumTariffRepository minimumTariffRepository;
    
    @Autowired
    private ProjectProgressRepository projectProgressRepository;
    
    @Autowired
    private AuthorizationUtils authorizationUtils;

    @Autowired
    private ConsecutiveSequenceService consecutiveSequenceService;
    
    private static final MathContext MATH_CONTEXT = new MathContext(10, RoundingMode.HALF_UP);
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Invoice> getInvoiceById(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        // Buscar directamente por ID y corporationId para evitar problemas con lazy loading
        return invoiceRepository.findByIdAndCorporationId(id, corporation.getId());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Invoice> getMyCorporationInvoices(Pageable pageable) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        return invoiceRepository.findByCorporation(corporation, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Invoice> getInvoicesByDischarge(Long dischargeId) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        Optional<Discharge> dischargeOpt = dischargeRepository.findById(dischargeId);
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (dischargeOpt.isEmpty() || dischargeOpt.get().getCorporation() == null || !dischargeOpt.get().getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalStateException("Discharge does not belong to your corporation");
        }
        
        return invoiceRepository.findByDischarge(dischargeOpt.get());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Invoice> getInvoiceByNumber(Integer number) {
        return invoiceRepository.findByNumber(number);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Invoice> searchInvoicesByDischargeAndNumber(Long dischargeId, Integer number) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        Optional<Discharge> dischargeOpt = dischargeRepository.findById(dischargeId);
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (dischargeOpt.isEmpty() || dischargeOpt.get().getCorporation() == null || !dischargeOpt.get().getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalStateException("Discharge does not belong to your corporation");
        }
        
        return invoiceRepository.findByDischargeAndNumber(dischargeOpt.get(), number);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Invoice> getInvoicesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return invoiceRepository.findByCreatedAtBetween(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countInvoicesByDischarge(Long dischargeId) {
        return invoiceRepository.countByDischargeId(dischargeId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countMyCorporationInvoices() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        return invoiceRepository.countByCorporationId(corporation.getId());
    }
        
    @Override
    @Transactional(readOnly = true)
    public boolean existsByNumber(int number) {
        if (number < 1 || number > 999999999) {
            throw new IllegalArgumentException("Invoice number must be between 1 and 999999999");
        }
        return invoiceRepository.existsByNumber(number);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Invoice> getInvoicesOrderByCreatedAtDesc() {
        return invoiceRepository.findAllOrderByCreatedAtDesc();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Invoice> getInvoicesByDischargeOrderByCreatedAtDesc(Long dischargeId) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        Optional<Discharge> dischargeOpt = dischargeRepository.findById(dischargeId);
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (dischargeOpt.isEmpty() || dischargeOpt.get().getCorporation() == null || !dischargeOpt.get().getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalStateException("Discharge does not belong to your corporation");
        }
        
        return invoiceRepository.findByDischargeOrderByCreatedAtDesc(dischargeOpt.get());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Invoice> getInvoicesByYear(int year) {
        return invoiceRepository.findByYear(year);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Invoice> getInvoicesByDischargeAndYear(Long dischargeId, int year) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        Optional<Discharge> dischargeOpt = dischargeRepository.findById(dischargeId);
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (dischargeOpt.isEmpty() || dischargeOpt.get().getCorporation() == null || !dischargeOpt.get().getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalStateException("Discharge does not belong to your corporation");
        }
        
        return invoiceRepository.findByDischargeAndYear(dischargeOpt.get(), year);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceResponse> getActiveInvoicesByYear(int year, Long dischargeUserId, Pageable pageable) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();

        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }

        Page<Invoice> invoices = dischargeUserId != null
                ? invoiceRepository.findActiveByYearAndCorporationIdAndDischargeUserId(year, corporation.getId(), dischargeUserId, pageable)
                : invoiceRepository.findActiveByYearAndCorporationId(year, corporation.getId(), pageable);

        return invoices.map(InvoiceResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceStats getMyCorporationInvoiceStats() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        InvoiceStats stats = new InvoiceStats();
        
        // Estadísticas básicas
        List<Invoice> allInvoices = invoiceRepository.findByCorporation(corporation);
        long totalInvoices = allInvoices.size();
        
        // Calcular totales y promedios
        BigDecimal totalAmount = allInvoices.stream()
            .map(Invoice::getTotalAmountToPay)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal averageAmount = totalInvoices > 0 ? 
            totalAmount.divide(BigDecimal.valueOf(totalInvoices), 2, RoundingMode.HALF_UP) : 
            BigDecimal.ZERO;
        
        BigDecimal minAmount = allInvoices.stream()
            .map(Invoice::getTotalAmountToPay)
            .min(BigDecimal::compareTo)
            .orElse(BigDecimal.ZERO);
        
        BigDecimal maxAmount = allInvoices.stream()
            .map(Invoice::getTotalAmountToPay)
            .max(BigDecimal::compareTo)
            .orElse(BigDecimal.ZERO);
        
        // Estadísticas por año y mes
        int currentYear = LocalDateTime.now().getYear();
        int currentMonth = LocalDateTime.now().getMonthValue();
        
        long invoicesThisYear = allInvoices.stream()
            .filter(invoice -> invoice.getCreatedAt().getYear() == currentYear)
            .count();
        
        long invoicesThisMonth = allInvoices.stream()
            .filter(invoice -> invoice.getCreatedAt().getYear() == currentYear && 
                              invoice.getCreatedAt().getMonthValue() == currentMonth)
            .count();
        
        // Estadísticas de descargas
        stats.setTotalInvoices(totalInvoices);
        stats.setTotalAmount(totalAmount);
        stats.setAverageAmount(averageAmount);
        stats.setMinAmount(minAmount);
        stats.setMaxAmount(maxAmount);
        stats.setInvoicesThisYear(invoicesThisYear);
        stats.setInvoicesThisMonth(invoicesThisMonth);
        
        return stats;
    }
    
    @Override
    @Transactional
    public Invoice generateInvoiceFromDischarge(Long dischargeId) {
        // 1. Validar usuario y corporación
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        // 2. Cargar DischargeInvoiceDto (con validación de pertenencia a corporación)
        DischargeInvoiceDto dischargeInvoiceDto = loadDischargeInvoiceDto(dischargeId, corporation.getId());
        
        // 3. Cargar MinimumTariffDto (por año y corporación)
        MinimumTariffDto minimumTariffDto = loadMinimumTariffDto(dischargeInvoiceDto.getYear(), corporation);
        
        // 4. Cargar ProjectProgressDto (si dischargeUserIsPublicServiceCompany es true)
        ProjectProgressDto projectProgressDto = null;
        if (Boolean.TRUE.equals(dischargeInvoiceDto.getDischargeUserIsPublicServiceCompany())) {
            projectProgressDto = loadProjectProgressDto(
                corporation.getId(),
                dischargeInvoiceDto.getYear(),
                dischargeInvoiceDto.getDischargeUserId()
            );
        }
        
        // 5. Validar datos requeridos (dischargeMonitorings[0] existe, etc.)
        if (dischargeInvoiceDto.getDischargeMonitorings() == null || dischargeInvoiceDto.getDischargeMonitorings().isEmpty()) {
            throw new IllegalArgumentException("La descarga debe tener al menos un monitoreo");
        }
        
        DischargeMonitoringInvoiceDto firstMonitoring = dischargeInvoiceDto.getDischargeMonitorings().get(0);
        if (firstMonitoring.getQualityClasification() == null || firstMonitoring.getCaudalVolumen() == null) {
            throw new IllegalArgumentException("El monitoreo debe tener clasificación de calidad y caudal/volumen");
        }
        
        // 6. Ejecutar cálculos (23 variables)
        BigDecimal cnbi = calculateCnbi(dischargeInvoiceDto.getMunicipalityNbi());
        BigDecimal socioeconomicVariable = calculateSocioeconomicVariable(
            dischargeInvoiceDto.getMunicipalityCategoryValue(),
            cnbi
        );
        
        BigDecimal cci = calculatePercentageCoefficient(
            projectProgressDto != null ? projectProgressDto.getCciPercentage() : null
        );
        BigDecimal cev = calculatePercentageCoefficient(
            projectProgressDto != null ? projectProgressDto.getCevPercentage() : null
        );
        BigDecimal cds = calculatePercentageCoefficient(
            projectProgressDto != null ? projectProgressDto.getCdsPercentage() : null
        );
        BigDecimal ccs = calculatePercentageCoefficient(
            projectProgressDto != null ? projectProgressDto.getCcsPercentage() : null
        );
        
        BigDecimal economicVariable = calculateEconomicVariable(cci, cev, cds, ccs);
        
        BigDecimal icaCoefficient = calculateIcaCoefficient(firstMonitoring.getQualityClasification());
        
        BigDecimal rq = calculateRq(dischargeInvoiceDto.getDischargeParameters(), firstMonitoring.getCaudalVolumen());
        BigDecimal rCoefficient = calculateRCoefficient(rq);
        
        BigDecimal rb = calculateRb(dischargeInvoiceDto.getDischargeParameters(), dischargeInvoiceDto.getDqo());
        BigDecimal bCoefficient = calculateBCoefficient(rb);
        
        BigDecimal environmentalVariable = calculateEnvironmentalVariable(icaCoefficient, rCoefficient, bCoefficient);
        
        BigDecimal regionalFactor = calculateRegionalFactor(
            environmentalVariable,
            socioeconomicVariable,
            economicVariable
        );
        
        BigDecimal amountToPayDbo = calculateAmountToPayDbo(
            regionalFactor,
            minimumTariffDto.getDboValue(),
            dischargeInvoiceDto.getCcDboTotal()
        ).setScale(0, RoundingMode.HALF_UP);
        
        BigDecimal amountToPaySst = calculateAmountToPaySst(
            regionalFactor,
            minimumTariffDto.getSstValue(),
            dischargeInvoiceDto.getCcSstTotal()
        ).setScale(0, RoundingMode.HALF_UP);
        
        BigDecimal totalAmountToPay = amountToPayDbo.add(amountToPaySst, MATH_CONTEXT);
        
        // 7. Verificar factura activa existente para el dischargeId
        Optional<Invoice> activeInvoiceOpt = invoiceRepository.findActiveByDischargeId(dischargeId);
        
        // Si la factura activa existe y su totalAmountToPay es igual al nuevo valor, retornarla sin persistir
        if (activeInvoiceOpt.isPresent() && activeInvoiceOpt.get().getTotalAmountToPay().compareTo(totalAmountToPay) == 0) {
            return activeInvoiceOpt.get();
        }
        
        // 8. En caso contrario (no existe o monto difiere): marcar la anterior como inactiva si existe, consumir consecutivo y persistir nueva
        activeInvoiceOpt.ifPresent(activeInvoice -> {
            activeInvoice.setActive(false);
            activeInvoice.setUpdatedBy(currentUser);
            activeInvoice.setUpdatedAt(LocalDateTime.now());
            invoiceRepository.save(activeInvoice);
        });
        
        // 9. Crear nueva factura con todos los valores
        Discharge discharge = dischargeRepository.findById(dischargeId)
            .orElseThrow(() -> new ResourceNotFoundException("Descarga no encontrada con id: " + dischargeId));
        
        Invoice invoice = new Invoice();
        invoice.setDischarge(discharge);
        invoice.setYear(dischargeInvoiceDto.getYear());
        invoice.setEnvironmentalVariable(environmentalVariable);
        invoice.setSocioeconomicVariable(socioeconomicVariable);
        invoice.setEconomicVariable(economicVariable);
        invoice.setRegionalFactor(regionalFactor);
        invoice.setCcDbo(dischargeInvoiceDto.getCcDboTotal());
        invoice.setCcSst(dischargeInvoiceDto.getCcSstTotal());
        invoice.setMinimumTariffDbo(minimumTariffDto.getDboValue());
        invoice.setMinimumTariffSst(minimumTariffDto.getSstValue());
        invoice.setAmountToPayDbo(amountToPayDbo);
        invoice.setAmountToPaySst(amountToPaySst);
        invoice.setTotalAmountToPay(totalAmountToPay);
        invoice.setNumberIcaVariables(firstMonitoring.getNumberIcaVariables());
        invoice.setIcaCoefficient(icaCoefficient);
        invoice.setrCoefficient(rCoefficient);
        invoice.setbCoefficient(bCoefficient);
        invoice.setActive(true);
        invoice.setCorporation(corporation);
        invoice.setCreatedBy(currentUser);
        invoice.setUpdatedBy(currentUser);
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setUpdatedAt(LocalDateTime.now());
        
        // 10. Asignar número desde ConsecutiveSequenceService y guardar
        int nextNumber = consecutiveSequenceService.getNextConsecutive(
            corporation.getId(),
            dischargeInvoiceDto.getYear(),
            SequenceType.INVOICE
        );
        invoice.setNumber(nextNumber);
        
        return invoiceRepository.save(invoice);
    }
    
    // Métodos privados para cargar DTOs
    
    private DischargeInvoiceDto loadDischargeInvoiceDto(Long dischargeId, Long corporationId) {
        Optional<Discharge> dischargeOpt = dischargeRepository.findByIdAndCorporationId(dischargeId, corporationId);
        
        if (dischargeOpt.isEmpty()) {
            throw new ResourceNotFoundException("Descarga no encontrada con id: " + dischargeId);
        }
        
        Discharge discharge = dischargeOpt.get();
        
        // Cargar dischargeParameters manualmente (no está en EntityGraph para evitar MultipleBagFetchException)
        Hibernate.initialize(discharge.getDischargeParameters());
        
        DischargeInvoiceDto dto = new DischargeInvoiceDto();
        dto.setId(discharge.getId());
        dto.setYear(discharge.getYear());
        dto.setCcDboTotal(discharge.getCcDboTotal());
        dto.setCcSstTotal(discharge.getCcSstTotal());
        dto.setDqo(discharge.getDqo());
        
        if (discharge.getDischargeUser() != null) {
            dto.setDischargeUserId(discharge.getDischargeUser().getId());
            dto.setDischargeUserIsPublicServiceCompany(discharge.getDischargeUser().isPublicServiceCompany());
            
            if (discharge.getDischargeUser().getMunicipality() != null) {
                dto.setMunicipalityNbi(discharge.getDischargeUser().getMunicipality().getNbi());
                
                if (discharge.getDischargeUser().getMunicipality().getCategory() != null) {
                    dto.setMunicipalityCategoryValue(discharge.getDischargeUser().getMunicipality().getCategory().getValue());
                }
            }
        }
        
        // Filtrar dischargeParameters por origin = VERTIMIENTO y mapear a DTO
        List<DischargeParameterInvoiceDto> parameterDtos = discharge.getDischargeParameters().stream()
            .filter(dp -> dp.getOrigin() == DischargeParameter.Origin.VERTIMIENTO)
            .map(dp -> new DischargeParameterInvoiceDto(dp.getConcDbo(), dp.getCaudalVolumen()))
            .collect(Collectors.toList());
        dto.setDischargeParameters(parameterDtos);
        
        // Mapear dischargeMonitorings a DTO
        List<DischargeMonitoringInvoiceDto> monitoringDtos = discharge.getDischargeMonitorings().stream()
            .map(dm -> new DischargeMonitoringInvoiceDto(
                dm.getNumberIcaVariables(),
                dm.getQualityClasification(),
                dm.getCaudalVolumen()
            ))
            .collect(Collectors.toList());
        dto.setDischargeMonitorings(monitoringDtos);
        
        return dto;
    }
    
    private MinimumTariffDto loadMinimumTariffDto(Integer year, Corporation corporation) {
        List<MinimumTariff> tariffs = minimumTariffRepository.findByCorporationAndYear(corporation, year);
        
        if (tariffs == null || tariffs.isEmpty()) {
            throw new ResourceNotFoundException("Tarifa mínima no encontrada para el año: " + year);
        }
        
        // Tomar la primera tarifa encontrada (asumiendo que hay una por año)
        MinimumTariff tariff = tariffs.get(0);
        return new MinimumTariffDto(tariff.getDboValue(), tariff.getSstValue());
    }
    
    private ProjectProgressDto loadProjectProgressDto(Long corporationId, Integer year, Long dischargeUserId) {
        Optional<ProjectProgress> progressOpt = projectProgressRepository.findByCorporationIdAndYearAndDischargeUserId(
            corporationId,
            year,
            dischargeUserId
        );
        
        if (progressOpt.isEmpty()) {
            return null;
        }
        
        ProjectProgress progress = progressOpt.get();
        return new ProjectProgressDto(
            progress.getCciPercentage(),
            progress.getCevPercentage(),
            progress.getCdsPercentage(),
            progress.getCcsPercentage()
        );
    }
    
    // Métodos privados para cálculos
    
    private BigDecimal calculateCnbi(BigDecimal nbi) {
        if (nbi == null) {
            throw new IllegalArgumentException("El NBI del municipio no puede ser nulo");
        }
        
        int nbiValue = nbi.intValue();
        if (nbiValue >= 0 && nbiValue <= 20) {
            return new BigDecimal("5.50");
        } else if (nbiValue > 20 && nbiValue <= 40) {
            return new BigDecimal("4.37");
        } else if (nbiValue > 40 && nbiValue <= 60) {
            return new BigDecimal("3.25");
        } else if (nbiValue > 60 && nbiValue <= 80) {
            return new BigDecimal("2.12");
        } else {
            return new BigDecimal("1.00");
        }
    }
    
    private BigDecimal calculateSocioeconomicVariable(BigDecimal municipalityCategoryValue, BigDecimal cnbi) {
        if (municipalityCategoryValue == null) {
            throw new IllegalArgumentException("El valor de categoría del municipio no puede ser nulo");
        }
        
        BigDecimal categoryPart = municipalityCategoryValue.multiply(new BigDecimal("0.1"), MATH_CONTEXT);
        BigDecimal cnbiPart = cnbi.multiply(new BigDecimal("0.1"), MATH_CONTEXT);
        return categoryPart.add(cnbiPart, MATH_CONTEXT);
    }
    
    private BigDecimal calculatePercentageCoefficient(BigDecimal percentage) {
        if (percentage == null) {
            return BigDecimal.ZERO;
        }
        
        int percentageValue = percentage.intValue();
        if (percentageValue >= 0 && percentageValue <= 20) {
            return BigDecimal.ZERO;
        } else if (percentageValue > 20 && percentageValue <= 40) {
            return new BigDecimal("0.25");
        } else if (percentageValue > 40 && percentageValue <= 60) {
            return new BigDecimal("0.50");
        } else if (percentageValue > 60 && percentageValue <= 80) {
            return new BigDecimal("0.75");
        } else {
            return new BigDecimal("1.00");
        }
    }
    
    private BigDecimal calculateEconomicVariable(BigDecimal cci, BigDecimal cev, BigDecimal cds, BigDecimal ccs) {
        BigDecimal cciPart = cci.multiply(new BigDecimal("0.25"), MATH_CONTEXT);
        BigDecimal cevPart = cev.multiply(new BigDecimal("0.25"), MATH_CONTEXT);
        BigDecimal cdsPart = cds.multiply(new BigDecimal("0.10"), MATH_CONTEXT);
        BigDecimal ccsPart = ccs.multiply(new BigDecimal("0.40"), MATH_CONTEXT);
        
        return cciPart.add(cevPart, MATH_CONTEXT)
            .add(cdsPart, MATH_CONTEXT)
            .add(ccsPart, MATH_CONTEXT);
    }
    
    private BigDecimal calculateIcaCoefficient(QualityClasification quality) {
        if (quality == null) {
            throw new IllegalArgumentException("La clasificación de calidad no puede ser nula");
        }
        
        switch (quality) {
            case BUENA:
                return new BigDecimal("1.00");
            case ACEPTABLE:
                return new BigDecimal("1.60");
            case REGULAR:
                return new BigDecimal("2.80");
            case MALA:
                return new BigDecimal("4.00");
            case MUY_MALA:
                return new BigDecimal("5.50");
            default:
                throw new IllegalArgumentException("Clasificación de calidad desconocida: " + quality);
        }
    }
    
    private BigDecimal calculateRq(List<DischargeParameterInvoiceDto> dischargeParameters, BigDecimal monitoringCaudalVolumen) {
        if (dischargeParameters == null || dischargeParameters.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        if (monitoringCaudalVolumen == null || monitoringCaudalVolumen.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("El caudal/volumen del monitoreo no puede ser cero o nulo");
        }
        
        BigDecimal sumCaudalVolumen = dischargeParameters.stream()
            .filter(dp -> dp.getCaudalVolumen() != null)
            .map(DischargeParameterInvoiceDto::getCaudalVolumen)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        if (sumCaudalVolumen.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal averageCaudalVolumen = sumCaudalVolumen.divide(
            new BigDecimal(dischargeParameters.size()),
            MATH_CONTEXT
        );
        
        return averageCaudalVolumen.divide(monitoringCaudalVolumen, MATH_CONTEXT);
    }
    
    private BigDecimal calculateRCoefficient(BigDecimal rq) {
        if (rq == null) {
            throw new IllegalArgumentException("El coeficiente RQ no puede ser nulo");
        }
        
        if (rq.compareTo(BigDecimal.ZERO) >= 0 && rq.compareTo(new BigDecimal("0.2")) <= 0) {
            return new BigDecimal("1.00");
        } else if (rq.compareTo(new BigDecimal("0.2")) > 0 && rq.compareTo(new BigDecimal("0.4")) <= 0) {
            return new BigDecimal("2.12");
        } else if (rq.compareTo(new BigDecimal("0.4")) > 0 && rq.compareTo(new BigDecimal("0.6")) <= 0) {
            return new BigDecimal("3.25");
        } else if (rq.compareTo(new BigDecimal("0.6")) > 0 && rq.compareTo(new BigDecimal("0.8")) <= 0) {
            return new BigDecimal("4.37");
        } else {
            return new BigDecimal("5.50");
        }
    }
    
    private BigDecimal calculateRb(List<DischargeParameterInvoiceDto> dischargeParameters, BigDecimal dqo) {
        if (dischargeParameters == null || dischargeParameters.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        if (dqo == null || dqo.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("El DQO no puede ser cero o nulo");
        }
        
        BigDecimal sumConcDbo = dischargeParameters.stream()
            .filter(dp -> dp.getConcDbo() != null)
            .map(DischargeParameterInvoiceDto::getConcDbo)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        if (sumConcDbo.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal averageConcDbo = sumConcDbo.divide(
            new BigDecimal(dischargeParameters.size()),
            MATH_CONTEXT
        );
        
        return averageConcDbo.divide(dqo, MATH_CONTEXT);
    }
    
    private BigDecimal calculateBCoefficient(BigDecimal rb) {
        if (rb == null) {
            throw new IllegalArgumentException("El coeficiente RB no puede ser nulo");
        }
        
        if (rb.compareTo(BigDecimal.ZERO) >= 0 && rb.compareTo(new BigDecimal("0.2")) <= 0) {
            return new BigDecimal("5.50");
        } else if (rb.compareTo(new BigDecimal("0.2")) > 0 && rb.compareTo(new BigDecimal("0.4")) <= 0) {
            return new BigDecimal("4.0");
        } else if (rb.compareTo(new BigDecimal("0.4")) > 0 && rb.compareTo(new BigDecimal("0.6")) <= 0) {
            return new BigDecimal("2.5");
        } else {
            return new BigDecimal("1.00");
        }
    }
    
    private BigDecimal calculateEnvironmentalVariable(BigDecimal icaCoefficient, BigDecimal rCoefficient, BigDecimal bCoefficient) {
        BigDecimal icaPart = icaCoefficient.multiply(new BigDecimal("0.16"), MATH_CONTEXT);
        BigDecimal rPart = rCoefficient.multiply(new BigDecimal("0.16"), MATH_CONTEXT);
        BigDecimal bPart = bCoefficient.multiply(new BigDecimal("0.48"), MATH_CONTEXT);
        
        return icaPart.add(rPart, MATH_CONTEXT).add(bPart, MATH_CONTEXT);
    }
    
    private BigDecimal calculateRegionalFactor(BigDecimal environmentalVariable, BigDecimal socioeconomicVariable, BigDecimal economicVariable) {
        return environmentalVariable.add(socioeconomicVariable, MATH_CONTEXT)
            .subtract(economicVariable, MATH_CONTEXT);
    }
    
    private BigDecimal calculateAmountToPayDbo(BigDecimal regionalFactor, BigDecimal dboValue, BigDecimal ccDboTotal) {
        return regionalFactor.multiply(dboValue, MATH_CONTEXT)
            .multiply(ccDboTotal, MATH_CONTEXT);
    }
    
    private BigDecimal calculateAmountToPaySst(BigDecimal regionalFactor, BigDecimal sstValue, BigDecimal ccSstTotal) {
        return regionalFactor.multiply(sstValue, MATH_CONTEXT)
            .multiply(ccSstTotal, MATH_CONTEXT);
    }
}

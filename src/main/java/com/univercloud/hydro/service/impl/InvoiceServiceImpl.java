package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.entity.Invoice;
import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.User;
import com.univercloud.hydro.entity.Discharge;
import com.univercloud.hydro.repository.InvoiceRepository;
import com.univercloud.hydro.repository.DischargeRepository;
import com.univercloud.hydro.service.InvoiceService;
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
    private AuthorizationUtils authorizationUtils;
    
    @Override
    @Transactional
    public Invoice createInvoice(Invoice invoice) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        // Verificar que el número de factura no exista
        if (existsByNumber(invoice.getNumber())) {
            throw new IllegalArgumentException("Ya existe una factura con el número: " + invoice.getNumber());
        }
        
        // Verificar que la descarga pertenezca a la corporación
        if (invoice.getDischarge() != null) {
            Optional<Discharge> dischargeOpt = dischargeRepository.findById(invoice.getDischarge().getId());
            if (dischargeOpt.isEmpty() || !dischargeOpt.get().getCorporation().equals(corporation)) {
                throw new IllegalArgumentException("La descarga no pertenece a su corporación");
            }
        }
        
        invoice.setCorporation(corporation);
        invoice.setCreatedBy(currentUser);
        invoice.setUpdatedBy(currentUser);
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setUpdatedAt(LocalDateTime.now());
        
        return invoiceRepository.save(invoice);
    }
    
    @Override
    @Transactional
    public Invoice updateInvoice(Invoice invoice) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<Invoice> existingOpt = invoiceRepository.findById(invoice.getId());
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la factura con ID: " + invoice.getId());
        }
        
        Invoice existing = existingOpt.get();
        
        // Verificar que pertenezca a la corporación del usuario
        if (!existing.getCorporation().equals(corporation)) {
            throw new IllegalArgumentException("No tiene permisos para actualizar esta factura");
        }
        
        // Verificar que el número de factura no exista (si cambió)
        if (!existing.getNumber().equals(invoice.getNumber()) && existsByNumber(invoice.getNumber())) {
            throw new IllegalArgumentException("Ya existe una factura con el número: " + invoice.getNumber());
        }
        
        // Verificar que la descarga pertenezca a la corporación
        if (invoice.getDischarge() != null) {
            Optional<Discharge> dischargeOpt = dischargeRepository.findById(invoice.getDischarge().getId());
            if (dischargeOpt.isEmpty() || !dischargeOpt.get().getCorporation().equals(corporation)) {
                throw new IllegalArgumentException("La descarga no pertenece a su corporación");
            }
        }
        
        existing.setNumber(invoice.getNumber());
        existing.setTotalAmountToPay(invoice.getTotalAmountToPay());
        existing.setDischarge(invoice.getDischarge());
        existing.setYear(invoice.getYear());
        existing.setEnvironmentalVariable(invoice.getEnvironmentalVariable());
        existing.setSocioeconomicVariable(invoice.getSocioeconomicVariable());
        existing.setEconomicVariable(invoice.getEconomicVariable());
        existing.setRegionalFactor(invoice.getRegionalFactor());
        existing.setCcDbo(invoice.getCcDbo());
        existing.setCcSst(invoice.getCcSst());
        existing.setMinimumTariffDbo(invoice.getMinimumTariffDbo());
        existing.setMinimumTariffSst(invoice.getMinimumTariffSst());
        existing.setAmountToPayDbo(invoice.getAmountToPayDbo());
        existing.setAmountToPaySst(invoice.getAmountToPaySst());
        existing.setTotalAmountToPay(invoice.getTotalAmountToPay());
        existing.setNumberIcaVariables(invoice.getNumberIcaVariables());
        existing.setIcaCoefficient(invoice.getIcaCoefficient());
        existing.setrCoefficient(invoice.getrCoefficient());
        existing.setbCoefficient(invoice.getbCoefficient());
        existing.setActive(invoice.isActive());
        existing.setUpdatedBy(currentUser);
        existing.setUpdatedAt(LocalDateTime.now());
        
        return invoiceRepository.save(existing);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Invoice> getInvoiceById(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
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
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        return invoiceRepository.findByCorporation(corporation, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Invoice> getAllMyCorporationInvoices() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        return invoiceRepository.findByCorporation(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Invoice> getInvoicesByDischarge(Long dischargeId) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<Discharge> dischargeOpt = dischargeRepository.findById(dischargeId);
        if (dischargeOpt.isEmpty() || !dischargeOpt.get().getCorporation().equals(corporation)) {
            throw new IllegalArgumentException("La descarga no pertenece a su corporación");
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
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<Discharge> dischargeOpt = dischargeRepository.findById(dischargeId);
        if (dischargeOpt.isEmpty() || !dischargeOpt.get().getCorporation().equals(corporation)) {
            throw new IllegalArgumentException("La descarga no pertenece a su corporación");
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
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        return invoiceRepository.countByCorporationId(corporation.getId());
    }
        
    @Override
    @Transactional
    public boolean deleteInvoice(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(id);
        if (invoiceOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la factura con ID: " + id);
        }
        
        Invoice invoice = invoiceOpt.get();
        if (!invoice.getCorporation().equals(corporation)) {
            throw new IllegalArgumentException("No tiene permisos para eliminar esta factura");
        }
        
        invoiceRepository.delete(invoice);
        return true;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByNumber(int number) {
        if (number < 1 || number > 999999999) {
            throw new IllegalArgumentException("El número de factura debe estar entre 1 y 999999999");
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
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<Discharge> dischargeOpt = dischargeRepository.findById(dischargeId);
        if (dischargeOpt.isEmpty() || !dischargeOpt.get().getCorporation().equals(corporation)) {
            throw new IllegalArgumentException("La descarga no pertenece a su corporación");
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
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<Discharge> dischargeOpt = dischargeRepository.findById(dischargeId);
        if (dischargeOpt.isEmpty() || !dischargeOpt.get().getCorporation().equals(corporation)) {
            throw new IllegalArgumentException("La descarga no pertenece a su corporación");
        }
        
        return invoiceRepository.findByDischargeAndYear(dischargeOpt.get(), year);
    }
    
    @Override
    @Transactional(readOnly = true)
    public InvoiceStats getMyCorporationInvoiceStats() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
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
            totalAmount.divide(BigDecimal.valueOf(totalInvoices), 2, BigDecimal.ROUND_HALF_UP) : 
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
        List<Discharge> allDischarges = dischargeRepository.findByCorporation(corporation);
        long dischargesWithInvoices = allDischarges.stream()
            .filter(discharge -> invoiceRepository.countByDischargeId(discharge.getId()) > 0)
            .count();
        long dischargesWithoutInvoices = allDischarges.size() - dischargesWithInvoices;
        
        stats.setTotalInvoices(totalInvoices);
        stats.setTotalAmount(totalAmount);
        stats.setAverageAmount(averageAmount);
        stats.setMinAmount(minAmount);
        stats.setMaxAmount(maxAmount);
        stats.setInvoicesThisYear(invoicesThisYear);
        stats.setInvoicesThisMonth(invoicesThisMonth);
        stats.setDischargesWithInvoices(dischargesWithInvoices);
        stats.setDischargesWithoutInvoices(dischargesWithoutInvoices);
        
        return stats;
    }
}

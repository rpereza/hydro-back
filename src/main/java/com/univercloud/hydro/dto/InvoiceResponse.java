package com.univercloud.hydro.dto;

import com.univercloud.hydro.entity.Invoice;

import java.math.BigDecimal;

public class InvoiceResponse {

    private Long id;
    private String companyName;
    private Integer year;
    private Integer number;
    private String dischargePoint;
    private BigDecimal amountToPayDbo;
    private BigDecimal amountToPaySst;
    private BigDecimal totalAmountToPay;

    public InvoiceResponse() {}

    public InvoiceResponse(Invoice invoice) {
        this.id = invoice.getId();
        this.year = invoice.getYear();
        this.number = invoice.getNumber();
        this.amountToPayDbo = invoice.getAmountToPayDbo();
        this.amountToPaySst = invoice.getAmountToPaySst();
        this.totalAmountToPay = invoice.getTotalAmountToPay();
        if (invoice.getDischarge() != null) {
            this.dischargePoint = invoice.getDischarge().getDischargePoint();
            if (invoice.getDischarge().getDischargeUser() != null) {
                this.companyName = invoice.getDischarge().getDischargeUser().getCompanyName();
            }
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public Integer getNumber() { return number; }
    public void setNumber(Integer number) { this.number = number; }

    public String getDischargePoint() { return dischargePoint; }
    public void setDischargePoint(String dischargePoint) { this.dischargePoint = dischargePoint; }

    public BigDecimal getAmountToPayDbo() { return amountToPayDbo; }
    public void setAmountToPayDbo(BigDecimal amountToPayDbo) { this.amountToPayDbo = amountToPayDbo; }

    public BigDecimal getAmountToPaySst() { return amountToPaySst; }
    public void setAmountToPaySst(BigDecimal amountToPaySst) { this.amountToPaySst = amountToPaySst; }

    public BigDecimal getTotalAmountToPay() { return totalAmountToPay; }
    public void setTotalAmountToPay(BigDecimal totalAmountToPay) { this.totalAmountToPay = totalAmountToPay; }
}

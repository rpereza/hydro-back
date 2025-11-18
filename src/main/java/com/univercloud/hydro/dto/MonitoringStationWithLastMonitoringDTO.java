package com.univercloud.hydro.dto;

import java.math.BigDecimal;

/**
 * DTO para representar una estación de monitoreo con su último monitoreo.
 */
public class MonitoringStationWithLastMonitoringDTO {
    
    private Long id;
    private String name;
    private String description;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private LastMonitoringDTO lastMonitoring;
    
    // Constructors
    public MonitoringStationWithLastMonitoringDTO() {}
    
    public MonitoringStationWithLastMonitoringDTO(Long id, String name, String description, 
                                                  BigDecimal latitude, BigDecimal longitude, 
                                                  LastMonitoringDTO lastMonitoring) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lastMonitoring = lastMonitoring;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getLatitude() {
        return latitude;
    }
    
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }
    
    public BigDecimal getLongitude() {
        return longitude;
    }
    
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }
    
    public LastMonitoringDTO getLastMonitoring() {
        return lastMonitoring;
    }
    
    public void setLastMonitoring(LastMonitoringDTO lastMonitoring) {
        this.lastMonitoring = lastMonitoring;
    }
    
    /**
     * DTO interno para representar el último monitoreo.
     */
    public static class LastMonitoringDTO {
        private Long id;
        private BigDecimal od;
        private BigDecimal sst;
        private BigDecimal dqo;
        private BigDecimal ce;
        private BigDecimal ph;
        private BigDecimal n;
        private BigDecimal p;
        private BigDecimal caudalVolumen;
        
        // Constructors
        public LastMonitoringDTO() {}
        
        public LastMonitoringDTO(Long id, BigDecimal od, BigDecimal sst, BigDecimal dqo, 
                                BigDecimal ce, BigDecimal ph, BigDecimal n, BigDecimal p, 
                                BigDecimal caudalVolumen) {
            this.id = id;
            this.od = od;
            this.sst = sst;
            this.dqo = dqo;
            this.ce = ce;
            this.ph = ph;
            this.n = n;
            this.p = p;
            this.caudalVolumen = caudalVolumen;
        }
        
        // Getters and Setters
        public Long getId() {
            return id;
        }
        
        public void setId(Long id) {
            this.id = id;
        }
        
        public BigDecimal getOd() {
            return od;
        }
        
        public void setOd(BigDecimal od) {
            this.od = od;
        }
        
        public BigDecimal getSst() {
            return sst;
        }
        
        public void setSst(BigDecimal sst) {
            this.sst = sst;
        }
        
        public BigDecimal getDqo() {
            return dqo;
        }
        
        public void setDqo(BigDecimal dqo) {
            this.dqo = dqo;
        }
        
        public BigDecimal getCe() {
            return ce;
        }
        
        public void setCe(BigDecimal ce) {
            this.ce = ce;
        }
        
        public BigDecimal getPh() {
            return ph;
        }
        
        public void setPh(BigDecimal ph) {
            this.ph = ph;
        }
        
        public BigDecimal getN() {
            return n;
        }
        
        public void setN(BigDecimal n) {
            this.n = n;
        }
        
        public BigDecimal getP() {
            return p;
        }
        
        public void setP(BigDecimal p) {
            this.p = p;
        }
        
        public BigDecimal getCaudalVolumen() {
            return caudalVolumen;
        }
        
        public void setCaudalVolumen(BigDecimal caudalVolumen) {
            this.caudalVolumen = caudalVolumen;
        }
    }
}


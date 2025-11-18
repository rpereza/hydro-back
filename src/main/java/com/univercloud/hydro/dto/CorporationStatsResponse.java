package com.univercloud.hydro.dto;

import java.time.LocalDateTime;

/**
 * DTO para las estadísticas de una Corporación.
 */
public class CorporationStatsResponse {
    
    private Long corporationId;
    private String corporationName;
    private int totalUsers;
    private int activeUsers;
    private int totalDischarges;
    private int totalMonitorings;
    private int totalInvoices;
    private LocalDateTime createdAt;
    private LocalDateTime lastActivity;
    
    // Constructors
    public CorporationStatsResponse() {}
    
    public CorporationStatsResponse(Long corporationId, String corporationName, 
                                  int totalUsers, int activeUsers, LocalDateTime createdAt) {
        this.corporationId = corporationId;
        this.corporationName = corporationName;
        this.totalUsers = totalUsers;
        this.activeUsers = activeUsers;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public Long getCorporationId() {
        return corporationId;
    }
    
    public void setCorporationId(Long corporationId) {
        this.corporationId = corporationId;
    }
    
    public String getCorporationName() {
        return corporationName;
    }
    
    public void setCorporationName(String corporationName) {
        this.corporationName = corporationName;
    }
    
    public int getTotalUsers() {
        return totalUsers;
    }
    
    public void setTotalUsers(int totalUsers) {
        this.totalUsers = totalUsers;
    }
    
    public int getActiveUsers() {
        return activeUsers;
    }
    
    public void setActiveUsers(int activeUsers) {
        this.activeUsers = activeUsers;
    }
    
    public int getTotalDischarges() {
        return totalDischarges;
    }
    
    public void setTotalDischarges(int totalDischarges) {
        this.totalDischarges = totalDischarges;
    }
    
    public int getTotalMonitorings() {
        return totalMonitorings;
    }
    
    public void setTotalMonitorings(int totalMonitorings) {
        this.totalMonitorings = totalMonitorings;
    }
    
    public int getTotalInvoices() {
        return totalInvoices;
    }
    
    public void setTotalInvoices(int totalInvoices) {
        this.totalInvoices = totalInvoices;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getLastActivity() {
        return lastActivity;
    }
    
    public void setLastActivity(LocalDateTime lastActivity) {
        this.lastActivity = lastActivity;
    }
    
    @Override
    public String toString() {
        return "CorporationStatsResponse{" +
                "corporationId=" + corporationId +
                ", corporationName='" + corporationName + '\'' +
                ", totalUsers=" + totalUsers +
                ", activeUsers=" + activeUsers +
                ", totalDischarges=" + totalDischarges +
                ", totalMonitorings=" + totalMonitorings +
                ", totalInvoices=" + totalInvoices +
                ", createdAt=" + createdAt +
                ", lastActivity=" + lastActivity +
                '}';
    }
}

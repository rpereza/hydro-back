package com.univercloud.hydro.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "discharge_users")
public class DischargeUser implements Auditable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Company name is required")
    @Size(max = 200, message = "Company name cannot exceed 200 characters")
    @Column(name = "company_name", nullable = false)
    private String companyName;
    
    @NotBlank(message = "Code is required")
    @Size(max = 8, message = "Code cannot exceed 8 characters")
    @Column(nullable = false)
    private String code;
    
    @Size(max = 50, message = "Document number cannot exceed 50 characters")
    @Column(name = "document_number")
    private String documentNumber;
    
    @NotNull(message = "Document type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    private DocumentType documentType;
    
    @NotBlank(message = "Contact person is required")
    @Size(max = 150, message = "Contact person name cannot exceed 150 characters")
    @Column(name = "contact_person", nullable = false)
    private String contactPerson;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email cannot exceed 255 characters")
    @Column(nullable = false)
    private String email;
    
    @NotBlank(message = "Phone is required")
    @Size(max = 10, message = "Phone cannot exceed 10 characters")
    @Column(nullable = false)
    private String phone;
    
    @Email(message = "Alternative email should be valid")
    @Size(max = 255, message = "Alternative email cannot exceed 255 characters")
    @Column(name = "alternative_email")
    private String alternativeEmail;
    
    @Size(max = 10, message = "Alternative phone cannot exceed 10 characters")
    @Column(name = "alternative_phone")
    private String alternativePhone;
    
    @NotBlank(message = "Address is required")
    @Size(max = 200, message = "Address cannot exceed 200 characters")
    @Column(nullable = false)
    private String address;
    
    @NotBlank(message = "File number is required")
    @Size(max = 20, message = "File number cannot exceed 20 characters")
    @Column(name = "file_number", nullable = false)
    private String fileNumber;
    
    @Column(name = "has_ptar", nullable = false)
    private boolean hasPtar = false;
    
    @Column(name = "efficiency_percentage", precision = 7, scale = 2)
    private BigDecimal efficiencyPercentage;
    
    @NotNull(message = "Municipality is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipality_id", nullable = false)
    private Municipality municipality;
    
    @NotNull(message = "Economic activity is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "economic_activity_id", nullable = false)
    private EconomicActivity economicActivity;
    
    @NotNull(message = "Authorization type is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorization_type_id", nullable = false)
    private AuthorizationType authorizationType;
    
    @Column(name = "is_public_service_company", nullable = false)
    private boolean isPublicServiceCompany = false;
    
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "corporation_id", nullable = false)
    private Corporation corporation;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdBy;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by_user_id")
    private User updatedBy;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "dischargeUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Discharge> discharges = new ArrayList<>();
    
    @OneToMany(mappedBy = "dischargeUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjectProgress> projectProgresses = new ArrayList<>();
    
    // Constructors
    public DischargeUser() {
        this.createdAt = LocalDateTime.now();
    }
    
    public DischargeUser(String companyName, String code, String documentNumber, DocumentType documentType,
                        String contactPerson, String email, String phone, String address, String fileNumber,
                        Municipality municipality, EconomicActivity economicActivity, 
                        AuthorizationType authorizationType) {
        this();
        this.companyName = companyName;
        this.code = code;
        this.documentNumber = documentNumber;
        this.documentType = documentType;
        this.contactPerson = contactPerson;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.fileNumber = fileNumber;
        this.municipality = municipality;
        this.economicActivity = economicActivity;
        this.authorizationType = authorizationType;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getDocumentNumber() {
        return documentNumber;
    }
    
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }
    
    public DocumentType getDocumentType() {
        return documentType;
    }
    
    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }
    
    public String getContactPerson() {
        return contactPerson;
    }
    
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getAlternativeEmail() {
        return alternativeEmail;
    }
    
    public void setAlternativeEmail(String alternativeEmail) {
        this.alternativeEmail = alternativeEmail;
    }
    
    public String getAlternativePhone() {
        return alternativePhone;
    }
    
    public void setAlternativePhone(String alternativePhone) {
        this.alternativePhone = alternativePhone;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getFileNumber() {
        return fileNumber;
    }
    
    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }
    
    public boolean isHasPtar() {
        return hasPtar;
    }
    
    public void setHasPtar(boolean hasPtar) {
        this.hasPtar = hasPtar;
    }
    
    public BigDecimal getEfficiencyPercentage() {
        return efficiencyPercentage;
    }
    
    public void setEfficiencyPercentage(BigDecimal efficiencyPercentage) {
        this.efficiencyPercentage = efficiencyPercentage;
    }
    
    public Municipality getMunicipality() {
        return municipality;
    }
    
    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
    }
    
    public EconomicActivity getEconomicActivity() {
        return economicActivity;
    }
    
    public void setEconomicActivity(EconomicActivity economicActivity) {
        this.economicActivity = economicActivity;
    }
    
    public AuthorizationType getAuthorizationType() {
        return authorizationType;
    }
    
    public void setAuthorizationType(AuthorizationType authorizationType) {
        this.authorizationType = authorizationType;
    }
    
    public boolean isPublicServiceCompany() {
        return isPublicServiceCompany;
    }
    
    public void setPublicServiceCompany(boolean publicServiceCompany) {
        isPublicServiceCompany = publicServiceCompany;
    }
    
    @JsonProperty("isActive")
    public boolean isActive() {
        return isActive;
    }
    
    @JsonProperty("isActive")
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @JsonIgnore
    public List<Discharge> getDischarges() {
        return discharges;
    }
    
    public void setDischarges(List<Discharge> discharges) {
        this.discharges = discharges;
    }
    
    @JsonIgnore
    public List<ProjectProgress> getProjectProgresses() {
        return projectProgresses;
    }
    
    public void setProjectProgresses(List<ProjectProgress> projectProgresses) {
        this.projectProgresses = projectProgresses;
    }
    
    // Auditable interface implementation
    @Override
    @JsonIgnore
    public Corporation getCorporation() {
        return corporation;
    }
    
    @Override
    public void setCorporation(Corporation corporation) {
        this.corporation = corporation;
    }
    
    @Override
    @JsonIgnore
    public User getCreatedBy() {
        return createdBy;
    }
    
    @Override
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
    
    @Override
    @JsonIgnore
    public User getUpdatedBy() {
        return updatedBy;
    }
    
    @Override
    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "DischargeUser{" +
                "id=" + id +
                ", companyName='" + companyName + '\'' +
                ", code='" + code + '\'' +
                ", documentNumber='" + documentNumber + '\'' +
                ", documentType=" + documentType +
                ", contactPerson='" + contactPerson + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", alternativeEmail='" + alternativeEmail + '\'' +
                ", alternativePhone='" + alternativePhone + '\'' +
                ", address='" + address + '\'' +
                ", fileNumber='" + fileNumber + '\'' +
                ", hasPtar=" + hasPtar +
                ", efficiencyPercentage=" + efficiencyPercentage +
                ", municipality=" + (municipality != null ? municipality.getName() : null) +
                ", economicActivity=" + (economicActivity != null ? economicActivity.getName() : null) +
                ", authorizationType=" + (authorizationType != null ? authorizationType.getName() : null) +
                ", isPublicServiceCompany=" + isPublicServiceCompany +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
    
    // Enum for document types
    public enum DocumentType {
        NIT("NIT"),
        CC("Cédula de Ciudadanía"),
        CE("Cédula de Extranjería");
        
        private final String displayName;
        
        DocumentType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}

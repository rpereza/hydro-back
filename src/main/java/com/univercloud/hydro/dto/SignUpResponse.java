package com.univercloud.hydro.dto;

public class SignUpResponse {
    
    private String message;
    private boolean success;
    
    // Constructors
    public SignUpResponse() {}
    
    public SignUpResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
    
    // Getters and Setters
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
}

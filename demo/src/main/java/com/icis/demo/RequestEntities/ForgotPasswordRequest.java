package com.icis.demo.RequestEntities;

public class ForgotPasswordRequest {
    private String email;


    public ForgotPasswordRequest(String email, String password) {
        this.email = email;
    }

    public ForgotPasswordRequest() {
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // getters and setters
}

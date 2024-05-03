package com.icis.demo.RequestEntities;

public class ResetPasswordRequest {
    private String email;
    private String password;
    private int emailCode;


    public ResetPasswordRequest(String email, String password, int emailCode) {
        this.email = email;
        this.password = password;
        this.emailCode = emailCode;
    }

    public ResetPasswordRequest() {
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public int getEmailCode() {
        return emailCode;
    }

    public void setEmailCode(int emailCode) {
        this.emailCode = emailCode;
    }
    // getters and setters
}

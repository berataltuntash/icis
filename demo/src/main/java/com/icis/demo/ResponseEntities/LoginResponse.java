package com.icis.demo.ResponseEntities;

public class LoginResponse {
    private String jwtToken;
    private String message;

    public LoginResponse(String jwtToken, String message) {
        this.jwtToken = jwtToken;
        this.message = message;
    }

    public LoginResponse() {
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

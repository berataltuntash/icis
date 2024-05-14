package com.icis.demo.RequestEntities;

public class SessionRequest {
    private String token;

    public SessionRequest(String token) {
        this.token = token;
    }

    public SessionRequest() {
    }

    public String getJwt() {
        return token;
    }

    public void setJwt(String token) {
        this.token = token;
    }


}

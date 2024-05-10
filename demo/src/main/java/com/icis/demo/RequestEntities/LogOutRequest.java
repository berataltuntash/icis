package com.icis.demo.RequestEntities;

public class LogOutRequest {

    private String jwt;

    public LogOutRequest(String jwt) {
        this.jwt = jwt;
    }
    public LogOutRequest() {
    }
    public String getJwt() {
        return jwt;
    }
    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

}

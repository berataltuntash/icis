package com.icis.demo.RequestEntities;

public class ShowHomePageRequest {
    private String jwt;

    public ShowHomePageRequest(String jwt) {
        this.jwt = jwt;
    }

    public ShowHomePageRequest() {
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}

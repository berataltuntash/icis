package com.icis.demo.RequestEntities;

public class AuthenticationRequestCompany {
    private String name;
    private String email;
    private String password;
    private String isForeign;


    public AuthenticationRequestCompany(String isForeign,String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isForeign = isForeign;
    }

    public AuthenticationRequestCompany() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getIsForeign() {
        return isForeign;
    }

    public void setIsForeign(String isForeign) {
        this.isForeign = isForeign;
    }
}

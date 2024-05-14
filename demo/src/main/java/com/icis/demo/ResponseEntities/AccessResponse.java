package com.icis.demo.ResponseEntities;

public class AccessResponse {
    private String UserType;
    private String Name;

    public AccessResponse(String UserType, String Name) {
        this.UserType = UserType;
        this.Name = Name;
    }

    public AccessResponse() {
    }

    public String getUsertype() {
        return UserType;
    }

    public void setUsertype(String usertype) {
        this.UserType = usertype;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }
}

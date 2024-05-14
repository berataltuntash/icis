package com.icis.demo.ResponseEntities;

public class OfferDetailsResponse {
    private String offername;
    private String companyname;
    private String description;

    public OfferDetailsResponse(String offername, String companyname, String description) {
        this.offername = offername;
        this.companyname = companyname;
        this.description = description;
    }

    public OfferDetailsResponse() {
    }

    public String getOffername() {
        return offername;
    }

    public void setOffername(String offername) {
        this.offername = offername;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

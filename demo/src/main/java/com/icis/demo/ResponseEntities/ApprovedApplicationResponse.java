package com.icis.demo.ResponseEntities;

public class ApprovedApplicationResponse {
    private int id;
    private String name;
    private String companyName;
    private String offerName;

    public ApprovedApplicationResponse(int id, String name, String companyName, String offerName) {
        this.id = id;
        this.name = name;
        this.companyName = companyName;
        this.offerName = offerName;
    }

    public ApprovedApplicationResponse() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }


}

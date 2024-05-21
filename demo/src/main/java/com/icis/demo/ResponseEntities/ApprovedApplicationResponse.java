package com.icis.demo.ResponseEntities;

public class ApprovedApplicationResponse {
    private int applicationId;
    private String companyName;
    private String offerName;

    public ApprovedApplicationResponse(int id, String companyName, String offerName) {
        this.applicationId = id;
        this.companyName = companyName;
        this.offerName = offerName;
    }

    public ApprovedApplicationResponse() {
    }

    public int getId() {
        return applicationId;
    }

    public void setId(int id) {
        this.applicationId = id;
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

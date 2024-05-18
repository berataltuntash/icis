package com.icis.demo.RequestEntities;

public class PostOfferRequest {
    String offerName;
    String companyName;
    String description;

    public PostOfferRequest(String offerName, String companyName, String description) {
        this.offerName = offerName;
        this.companyName = companyName;
        this.description = description;
    }

    public PostOfferRequest() {
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

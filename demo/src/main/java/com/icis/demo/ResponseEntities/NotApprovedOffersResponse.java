package com.icis.demo.ResponseEntities;

public class NotApprovedOffersResponse {
    private String offername;
    private int offerid;

    public NotApprovedOffersResponse(String offername, int offerid) {
        this.offername = offername;
        this.offerid = offerid;
    }

    public NotApprovedOffersResponse() {
    }

    public String getOffername() {
        return offername;
    }

    public void setOffername(String offername) {
        this.offername = offername;
    }

    public int getOfferid() {
        return offerid;
    }

    public void setOfferid(int offerid) {
        this.offerid = offerid;
    }
}

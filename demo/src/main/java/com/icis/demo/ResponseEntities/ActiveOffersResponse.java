package com.icis.demo.ResponseEntities;

public class ActiveOffersResponse {
    private String offername;
    private int offerid;

    public ActiveOffersResponse(String offername, int offerid) {
        this.offername = offername;
        this.offerid = offerid;
    }

    public ActiveOffersResponse() {
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

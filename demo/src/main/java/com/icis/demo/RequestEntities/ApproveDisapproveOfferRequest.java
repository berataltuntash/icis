package com.icis.demo.RequestEntities;

public class ApproveDisapproveOfferRequest {
    private boolean offerApprove;

    public ApproveDisapproveOfferRequest() {
    }

    public ApproveDisapproveOfferRequest(boolean offerApprove) {
        this.offerApprove = offerApprove;
    }

    public boolean isOfferApprove() {
        return offerApprove;
    }

    public void setOfferApprove(boolean offerApprove) {
        this.offerApprove = offerApprove;
    }
}

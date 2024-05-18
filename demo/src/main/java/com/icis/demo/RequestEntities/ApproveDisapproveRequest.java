package com.icis.demo.RequestEntities;

public class ApproveDisapproveRequest {
    private boolean offerApprove;

    public ApproveDisapproveRequest() {
    }

    public ApproveDisapproveRequest(boolean offerApprove) {
        this.offerApprove = offerApprove;
    }

    public boolean isApprove() {
        return offerApprove;
    }

    public void setApprove(boolean offerApprove) {
        this.offerApprove = offerApprove;
    }
}

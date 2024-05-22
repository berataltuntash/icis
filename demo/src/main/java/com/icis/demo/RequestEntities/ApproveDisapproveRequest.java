package com.icis.demo.RequestEntities;

public class ApproveDisapproveRequest {
    private boolean isApproved;

    public ApproveDisapproveRequest(boolean isApproved) {
        this.isApproved = isApproved;
    }

    public ApproveDisapproveRequest() {
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }
}

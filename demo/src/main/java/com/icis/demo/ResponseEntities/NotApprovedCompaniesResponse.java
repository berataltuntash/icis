package com.icis.demo.ResponseEntities;

public class NotApprovedCompaniesResponse {
    private String companyName;
    private int companyId;

    public NotApprovedCompaniesResponse(String companyName, int companyId) {
        this.companyName = companyName;
        this.companyId = companyId;
    }

    public NotApprovedCompaniesResponse() {
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}

package com.icis.demo.ResponseEntities;

public class StartedInternshipsResponse {
    private int applicationId;
    private String studentName;
    private String studentSurname;
    private String offerName;

    public StartedInternshipsResponse(int applicationId, String studentName, String studentSurname, String offerName) {
        this.applicationId = applicationId;
        this.studentName = studentName;
        this.studentSurname = studentSurname;
        this.offerName = offerName;
    }

    public StartedInternshipsResponse() {
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentSurname() {
        return studentSurname;
    }

    public void setStudentSurname(String studentSurname) {
        this.studentSurname = studentSurname;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }
}

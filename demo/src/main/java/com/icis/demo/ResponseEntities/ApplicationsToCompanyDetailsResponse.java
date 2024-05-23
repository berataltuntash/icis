package com.icis.demo.ResponseEntities;

public class ApplicationsToCompanyDetailsResponse {
    private String id;
    private String studentName;
    private String studentSurname;
    private String offerName;
    private String grade;

    public ApplicationsToCompanyDetailsResponse(String id,String studentName, String offerName, String grade, String studentSurname) {
        this.id = id;
        this.studentName = studentName;
        this.studentSurname = studentSurname;
        this.offerName = offerName;
        this.grade = grade;
    }

    public ApplicationsToCompanyDetailsResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getStudentSurname() {
        return studentSurname;
    }

    public void setStudentSurname(String studentSurname) {
        this.studentSurname = studentSurname;
    }
}

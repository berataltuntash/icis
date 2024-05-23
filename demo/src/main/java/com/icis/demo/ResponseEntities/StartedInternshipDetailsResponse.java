package com.icis.demo.ResponseEntities;

public class StartedInternshipDetailsResponse {
    private String studentId;
    private String studentName;
    private String studentSurname;
    private String offerName;
    private String grade;
    private String companyName;

    public StartedInternshipDetailsResponse(String studentId, String studentName, String studentSurname, String offerName, String grade, String companyName) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentSurname = studentSurname;
        this.offerName = offerName;
        this.grade = grade;
        this.companyName = companyName;
    }

    public StartedInternshipDetailsResponse() {
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
